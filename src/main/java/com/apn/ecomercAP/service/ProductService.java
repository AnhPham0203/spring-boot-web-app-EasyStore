package com.apn.ecomercAP.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.apn.ecomercAP.model.Product;

public interface ProductService {

	public Product saveProduct(Product product);

	public List<Product> getAllProducts();

	Page<Product> findTop4ActiveProducts(Integer pageNo, Integer pageSize);

//	List<Product> findByTitleContainingIgnoreCase(String ch);

	public Boolean deleteProduct(Integer id);

//
	public Product getProductById(Integer id);

//
	public Product updateProduct(Product product, MultipartFile file);

//
	public List<Product> getAllActiveProducts(String category);

//
	public List<Product> searchProduct(String ch, String ch2);

//
	public Page<Product> getAllActiveProductPagination(Integer pageNo, Integer pageSize, String category);

//
	public Page<Product> searchProductPagination(Integer pageNo, Integer pageSize, String ch);

//
	public Page<Product> getAllProductsPagination(Integer pageNo, Integer pageSize);

//
	public Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize, String ch);

	public Page<Product> searchDiscountPricePagination(Integer pageNo, Integer pageSize, String sort);

}
