package com.apn.ecomercAP.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apn.ecomercAP.DTO.SearchPagin;
import com.apn.ecomercAP.model.Category;
import com.apn.ecomercAP.model.Product;
import com.apn.ecomercAP.model.UserDtls;
import com.apn.ecomercAP.service.CategoryService;
import com.apn.ecomercAP.service.ProductService;
import com.apn.ecomercAP.service.UserService;

import io.micrometer.common.util.StringUtils;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;

	@ModelAttribute
	public void getUserDetails(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			UserDtls userDtls = userService.getUserByEmail(email);
			m.addAttribute("user", userDtls);
//			Integer countCart = cartService.getCountCart(userDtls.getId());
//			m.addAttribute("countCart", countCart);
		}

		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categories", allActiveCategory);
	}
	
	@GetMapping("/productsJSON")
	public SearchPagin productsJSON(Model model, @RequestParam(value = "category", defaultValue = "") String category,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize,
			@RequestParam(value = "search", defaultValue = "") String search,
			@RequestParam(value = "sort", defaultValue = "") String sort) {

		List<Category> categories = categoryService.findByIsActiveTrue();
		model.addAttribute("categories", categories);
		model.addAttribute("paramValue", category);
		model.addAttribute("sort", sort);
		model.addAttribute("search", search);
		Page<Product> page = null;
		if (StringUtils.isEmpty(search) && StringUtils.isEmpty(category)) {
			page = productService.searchDiscountPricePagination(pageNo, pageSize, sort);

		} else if (StringUtils.isEmpty(search)) {
			page = productService.getAllActiveProductPagination(pageNo, pageSize, category);
		} else {
			page = productService.searchActiveProductPagination(pageNo, pageSize,  search);
		}

		List<Product> products = page.getContent();

		SearchPagin searchPagin = new SearchPagin();
		
		searchPagin.setListProducts(products);
		searchPagin.setProductsSize(products.size());
		searchPagin.setPageNo(page.getNumber());
		searchPagin.setPageSize(pageSize);
		searchPagin.setTotalElements(page.getTotalElements());
		searchPagin.setTotalPages(page.getTotalPages());
		searchPagin.setIsFirst(page.isFirst());
		searchPagin.setIsLast(page.isLast());
		return searchPagin;
	}
}
