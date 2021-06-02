package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.model.ProductCategory;
import com.example.demo.service.ProductCategoryService;

@Controller
public class ProductCategoryController {

	@Autowired
	private ProductCategoryService service;
	
	@RequestMapping("/viewproductcategorys")
	public String viewProductCategorys(HttpServletRequest request,Model model)
	{
		model.addAttribute("productcategorys",service.findAll());
		return "viewproductCategorys";
	}
	
	@RequestMapping(value = "/productcategory",method = RequestMethod.GET)
	public String addProductCategory(Model model)
	{
		model.addAttribute("productcategory",new ProductCategory());
		return "addcategory";
	}

	@RequestMapping(value = "/productcategoryaction",method = RequestMethod.POST)
	public String addProductCategoryAction(Model model,@ModelAttribute("productCategory") ProductCategory productCategory)
	{
		if(!service.isExist(productCategory.getCategory()))
		{
			model.addAttribute("message","Category Added");
			service.save(productCategory);
		}
		else
		{
			model.addAttribute("message","Category is Allready Exist");
		}
		
		return "redirect:/home";
	}
}

