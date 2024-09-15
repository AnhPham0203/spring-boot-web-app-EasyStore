package com.apn.ecomercAP.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.apn.ecomercAP.model.Category;

public interface CategoryService {
	List<Category> getAllCategories();

	Page<Category> getAllCategoriesPagination(Integer pageNo, Integer pageSize);

	List<Category> getAllActiveCategory();

	Category getCategoryById(Integer id);

	List<Category> findByIsActiveTrue();

	Boolean checkExitstCategoryName(String name);

	Category saveCategory(Category category);

	Boolean deleteCategory(int id);
}
