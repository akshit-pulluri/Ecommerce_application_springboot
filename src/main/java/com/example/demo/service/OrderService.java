package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.model.ProductOrder;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repo;
	
	@Autowired
	private ProductRepository productRepo;
	
	public ProductOrder save(ProductOrder order)
	{
		return repo.save(order);
	}
	
	public Map<Integer,Product> findAll(String username)
	{
		Map<Integer,Product> orders=new HashMap<Integer,Product>();
		
		for(ProductOrder order : repo.findAll())
		{
			if(order.getUsername().equals(username))
			{
				orders.put(order.getId(),productRepo.findById(order.getPid()).get());
			}
		}
		
		return orders;
	}
	
	public ProductOrder findById(int id)
	{
		return repo.findById(id).get();
	}
	
	public void delete(int id)
	{
		repo.deleteById(id);
	}	
	
	public boolean isExist(int id)
	{
		return repo.existsById(id);
	}
}
