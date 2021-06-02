package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.ProductCategoryService;

@Controller
public class IndexController {

	@Autowired
	private ProductCategoryService categoryService;
	
	@RequestMapping("/")
	public String index(Model model)
	{
		return "index";
	}
	
	@RequestMapping("/header")
	public String header(Model model)
	{
		return "header";
	}
	
	@RequestMapping("/footer")
	public String footer(Model model)
	{
		return "footer";
	}
	
	@RequestMapping("/menu1")
	public String menu1(Model model)
	{
		return "menu1";
	}
	
	@RequestMapping("/menu2")
	public String menu2(Model model)
	{
		model.addAttribute("categorys",categoryService.findAll());
		return "menu2";
	}
}

