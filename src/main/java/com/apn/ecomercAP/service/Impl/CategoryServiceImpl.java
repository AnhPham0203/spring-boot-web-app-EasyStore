package com.apn.ecomercAP.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

	@Override
	public Page<Category> getAllCategoriesPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);

		Page<Category> pageCategory = categoryRepository.findAll(pageable);
		return pageCategory;
	}

	@Override
	public Boolean checkExitstCategoryName(String name) {
		return categoryRepository.existsByName(name);

	}

	@Override
	public Category saveCategory(Category category) {
	return categoryRepository.save(category);
		
	}

	@Override
	public Boolean deleteCategory(int id) {
		Category category= categoryRepository.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(category)) {
			categoryRepository.delete(category);
			return true;
		}
		return false ;
	}

	@Override
	public Category getCategoryById(Integer id) {
		// TODO Auto-generated method stub
		return categoryRepository.findById(id).orElse(null);
	}

}
