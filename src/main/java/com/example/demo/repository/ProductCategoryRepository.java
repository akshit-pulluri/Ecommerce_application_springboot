package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer>{

}