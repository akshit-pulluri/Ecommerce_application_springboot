package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.exception.ProductNotfoundException;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.Product;
import com.example.demo.service.ProductCategoryService;
import com.example.demo.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductCategoryService categoryService;

	@RequestMapping("/home")
	public String viewAllProducts(Model model)
	{
		List<Product> products=service.findAll();
		model.addAttribute("products",products);
		model.addAttribute("categorys",categoryService.findAll());
		return "home";
	}
	
	@RequestMapping("/addcart")
	public String viewAllProducts(@RequestParam int id,Model model,HttpServletRequest request)
	{
		List<Integer> products=(List<Integer>)request.getSession().getAttribute("cart");
		
		if(products!=null)
		{
			System.out.println("in if");
			
			products.add(id);
			request.getSession().setAttribute("cart",products);
		}
		else
		{
			System.out.println("in else");
			
			List<Integer> list=new ArrayList<Integer>();
			list.add(id);
			request.getSession().setAttribute("cart",list);
		}
		
		model.addAttribute("message","product added to cart");
		return "redirect:/viewcart";
	}
	
	@RequestMapping("/viewcart")
	public String viewCart(Model model,HttpServletRequest request)
	{
		List<Integer> sessionProducts=(List<Integer>)request.getSession().getAttribute("cart");
		
		if(sessionProducts!=null)
		{
			List<Product> list=new ArrayList<Product>();
			
			for(Integer i : sessionProducts)
			{
				list.add(service.findById(i));
			}
			
			model.addAttribute("products",list);
			model.addAttribute("categorys",categoryService.findAll());
		}
		
				return "viewcart";
	}
	
	@RequestMapping("/deletecart")
	public String deletecart(@RequestParam int id,Model model,HttpServletRequest request)
	{
		List<Integer> sessionProducts=(List<Integer>)request.getSession().getAttribute("cart");
		
		if(sessionProducts!=null)
		{
			sessionProducts.remove(new Integer(id));
			request.getSession().setAttribute("cart",sessionProducts);
		}
		
		return "redirect:/viewcart";
	}
	
	
	@RequestMapping("/sorthigh")
	public String sortHigh(Model model)
	{
		List<Product> products=service.findAll();
		
		Collections.sort(products, new Comparator<Product>(){
		    public int compare(Product p1, Product p2) {
		        
		    	if(p1.getPrice()>p2.getPrice())
		    	{
		    		return -1;
		    	}
		    	else if(p1.getPrice()==p2.getPrice())
		    	{
		    		return 1;
		    	}
		    	else
		    	{
		    		return 1;
		    	}
		    }
		});

		model.addAttribute("products",products);
		model.addAttribute("categorys",categoryService.findAll());

		return "home";
	}
	
	@RequestMapping("/sortlow")
	public String sortLow(Model model)
	{
		List<Product> products=service.findAll();
		
		Collections.sort(products, new Comparator<Product>(){
		    public int compare(Product p1, Product p2) {
		        
		    	if(p1.getPrice()>p2.getPrice())
		    	{
		    		return 1;
		    	}
		    	else if(p1.getPrice()==p2.getPrice())
		    	{
		    		return 1;
		    	}
		    	else
		    	{
		    		return -1;
		    	}
		    }
		});

		model.addAttribute("products",products);
		model.addAttribute("categorys",categoryService.findAll());

		return "home";
	}
	
	@RequestMapping("/category")
	public String top(@RequestParam String catg,Model model)
	{
		List<Product> products=service.findAll();
		List<Product> list=new ArrayList<Product>();
		
		for(Product product : products)
		{
			if(product.getCategory().equals(catg))
			{
				list.add(product);
			}
		}
		
		model.addAttribute("products",list);
		model.addAttribute("categorys",categoryService.findAll());

		return "home";
	}
	
	@RequestMapping("/search")
	public String search(@RequestParam String keyword,Model model)
	{
		List<Product> products=service.findAll();
		List<Product> list=new ArrayList<Product>();
		
		for(Product product : products)
		{
			if(product.getName().contains(keyword))
			{
				list.add(product);
			}
		}
		
		model.addAttribute("products",list);
		model.addAttribute("categorys",categoryService.findAll());

		return "home";
	}
	
	@RequestMapping("/colorselection")
	public String colorSelection(@RequestParam String color,Model model)
	{
		List<Product> products=service.findAll();
		List<Product> list=new ArrayList<Product>();
		
		for(Product product : products)
		{
			if(product.getColor().equals(color))
			{
				list.add(product);
			}
		}
		
		model.addAttribute("products",list);
		model.addAttribute("categorys",categoryService.findAll());

		return "home";
	}
	
	@RequestMapping("/view")
	public String viewProducts(@RequestParam int id,Model model)
	{
		model.addAttribute("product",service.findById(id));
		model.addAttribute("order",new ProductOrder());
		return "viewproduct";
	}

	@RequestMapping("/add")
	public String addProduct(Model model)
	{
		model.addAttribute("product",new Product());
		model.addAttribute("categorys",categoryService.findAll());
		return "addproduct";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String addProduct(HttpServletRequest request, Model model,@ModelAttribute("product") Product product)
	{
		// Root Directory.
		String uploadRootPath = "C:\\Users\\Akshit\\Downloads\\SpringBoot_Ecommerce\\src\\main\\resources\\static\\productimages";
		System.out.println("uploadRootPath=" + uploadRootPath);

		File uploadRootDir = new File(uploadRootPath);
		// Create directory if it not exists.
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}

		MultipartFile image =product.getImage();
		
		String name = image.getOriginalFilename();
		System.out.println("Client File Name = " + name);

		if (name != null && name.length() > 0) {
			try {
				// Create the file at server
				File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(image.getBytes());
				stream.close();
				
				System.out.println("done");
				
			} catch (Exception e) {
				System.out.println("Error Write file: " + name);
			}
		}
		
		product.setImageName(name);
		service.save(product);
		return "redirect:/home";
	}

	@RequestMapping("/edit")
	public String update(@RequestParam int id,Model model)
	{
		model.addAttribute("product",service.findById(id));
		model.addAttribute("categorys",categoryService.findAll());
		return "update";
	}
	@RequestMapping("/update")
	public String update(HttpServletRequest request, Model model,@ModelAttribute("product") Product product)
	{
		// Root Directory.
		String uploadRootPath = "C:\\Users\\Akshit\\Downloads\\SpringBoot_Ecommerce\\src\\main\\resources\\static\\productimages";
		System.out.println("uploadRootPath=" + uploadRootPath);

		File uploadRootDir = new File(uploadRootPath);
		// Create directory if it not exists.
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}

		MultipartFile image =product.getImage();
		
		String name = image.getOriginalFilename();
		System.out.println("Client File Name = " + name);

		if (name != null && name.length() > 0) {
			try {
				// Create the file at server
				File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(image.getBytes());
				stream.close();
				
				System.out.println("done");
				
			} catch (Exception e) {
				System.out.println("Error Write file: " + name);
			}
		}
		
		product.setImageName(name);
		service.save(product);
		return "viewproduct";
	}
	
	@RequestMapping("/delete")
	public String deleteProduct(@RequestParam int id)
	{
		if(!service.isExist(id))throw new ProductNotfoundException();
		service.delete(id);
		return "redirect:/home";
	}
}

