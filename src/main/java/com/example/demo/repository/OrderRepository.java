package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ProductOrder;

public interface OrderRepository extends JpaRepository<ProductOrder,Integer>{

}