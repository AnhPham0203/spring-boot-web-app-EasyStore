package com.apn.ecomercAP.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apn.ecomercAP.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
//	List<Category> findBy();

	List<Category> findByIsActiveTrue();
}
