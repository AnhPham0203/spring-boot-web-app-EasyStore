package com.apn.ecomercAP.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apn.ecomercAP.model.Category;
import com.apn.ecomercAP.repository.CategoryRepository;
import com.apn.ecomercAP.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
//	@Override
//	public List<Category> findAllCategories() {
//		// TODO Auto-generated method stub
//		return categoryRepository.findAllCategories();
//	}

	@Override
	public List<Category> findByIsActiveTrue() {
		// TODO Auto-generated method stub
		return categoryRepository.findByIsActiveTrue();
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}

	@Override
	public List<Category> getAllActiveCategory() {
		// TODO Auto-generated method stub
		return categoryRepository.findByIsActiveTrue();
	}

}
