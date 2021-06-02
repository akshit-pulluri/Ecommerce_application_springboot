package com.example.demo.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.exception.ProductNotfoundException;
import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import com.example.demo.service.MailService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;

@Controller
public class OrderController {

	@Autowired
	private OrderService service;
	
	@Autowired
	private ProductService productService;

	@RequestMapping("/vieworders")
	public String viewOrders(HttpServletRequest request,Model model)
	{
		model.addAttribute("orders",service.findAll((String)request.getSession().getAttribute("username")));
		return "vieworders";
	}

	@RequestMapping(value = "/order",method = RequestMethod.POST)
	public String addOrder(HttpServletRequest request, Model model,@ModelAttribute("order") ProductOrder order)
	{
		request.getSession().setAttribute("pid",order.getPid());
		request.getSession().setAttribute("size",order.getSize());
		
		model.addAttribute("amount",productService.findById(order.getPid()).getPrice());
		
		return "payment";
	}
	
	@RequestMapping(value = "/ordercart")
	public String orderCart(HttpServletRequest request, Model model)
	{
		List<Integer> sessionProducts=(List<Integer>)request.getSession().getAttribute("cart");
		
		if(sessionProducts!=null)
		{
			float amount=0;
			
			for(Integer i : sessionProducts)
			{
				amount=amount+productService.findById(i).getPrice();
			}
			
			model.addAttribute("amount",amount);
			return "cartpayment";
		}
		else
		{
			return "redirect:/viewcart";
		}
	}
	
	@RequestMapping(value = "/ordersubmit",method = RequestMethod.POST)
	public String orderSubmit(HttpServletRequest request)
	{
		int pid=(Integer)request.getSession().getAttribute("pid");
		String size=(String)request.getSession().getAttribute("size");
		
		ProductOrder order=new ProductOrder();
		
		order.setPid(pid);
		order.setSize(size);
		order.setDate(new Date());
		order.setUsername((String)request.getSession().getAttribute("username"));
		
		Product product=productService.findById(pid);
		product.setCount(product.getCount()-1);
		
		service.save(order);
		productService.save(product);
		
		if(product.getCount()<5)
		{
			try {
				MailService.mailsend("Shortage of items  ","Product Name:"+product.getName()+"\nProduct Category:"+product.getCategory()+"\nProduct Id:"+product.getId()+"\nAvailable stock:"+product.getCount()+"\nPlease add the items to avoid out of stock.","akshit.pulluri@gmail.com");
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		
		return "redirect:/home";
	}
	@RequestMapping("/cancel")
	public String cancelorder(@RequestParam int id)
	{
		System.out.println("in order cancel");
		service.delete(id);
		return "redirect:/vieworders";
	}
	
	@RequestMapping(value = "/submitcart",method = RequestMethod.POST)
	public String submitCart(HttpServletRequest request)
	{
		List<Integer> sessionProducts=(List<Integer>)request.getSession().getAttribute("cart");
		
		if(sessionProducts!=null)
		{
			for(Integer i : sessionProducts)
			{
				Product p=productService.findById(i);
				
				ProductOrder order=new ProductOrder();
				
				order.setPid(p.getId());
				order.setSize("3");
				order.setDate(new Date());
				order.setUsername((String)request.getSession().getAttribute("username"));
				
				service.save(order);
			}
			request.getSession().removeAttribute("cart");
		}
		
		return "redirect:/viewcart";
	}
}

