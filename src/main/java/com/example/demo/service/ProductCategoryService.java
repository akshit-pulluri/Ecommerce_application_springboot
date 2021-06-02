package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ProductCategory;
import com.example.demo.repository.ProductCategoryRepository;

@Service
public class ProductCategoryService {

	@Autowired
	private ProductCategoryRepository repo;
	
	public ProductCategory save(ProductCategory category)
	{
		return repo.save(category);
	}
	
	public List<ProductCategory> findAll()
	{
		return repo.findAll();
	}
	
	public ProductCategory findById(int id)
	{
		return repo.findById(id).get();
	}
	
	public void delete(int id)
	{
		repo.deleteById(id);
	}	
	
	public boolean isExist(String cat)
	{
		boolean isExist=false;
		
		for(ProductCategory category:findAll())
		{
			if(category.getCategory().toLowerCase().equals(cat.toLowerCase()))
			{
				isExist=true;
			}
		}
		
		return isExist;
	}
}
