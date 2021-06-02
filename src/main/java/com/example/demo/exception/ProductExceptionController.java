package com.example.demo.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductExceptionController {
	
	@ExceptionHandler(value = ProductNotfoundException.class)
	
	public String exception(ProductNotfoundException exception,Model model)
	{
		model.addAttribute("message","product not found");
		return "redirect:/";
	}
}
