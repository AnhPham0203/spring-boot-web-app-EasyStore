package com.apn.ecomercAP.service;

import java.util.List;

import com.apn.ecomercAP.model.Category;

public interface CategoryService {
	List<Category> getAllCategories();
	
	List<Category> getAllActiveCategory();

	List<Category> findByIsActiveTrue();
}
