package com.apn.ecomercAP.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.apn.ecomercAP.DTO.SearchPagin;
import com.apn.ecomercAP.model.Category;
import com.apn.ecomercAP.model.Product;
import com.apn.ecomercAP.model.UserDtls;
import com.apn.ecomercAP.service.CartService;
import com.apn.ecomercAP.service.CategoryService;
import com.apn.ecomercAP.service.ProductService;
import com.apn.ecomercAP.service.UserService;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;

@Controller
//@RestController
public class HomeController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CartService cartService;

	@ModelAttribute
	public void getUserDetails(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			UserDtls userDtls = userService.getUserByEmail(email);
			m.addAttribute("user", userDtls);
			Integer countCart = cartService.getCountCart(userDtls.getId());
			m.addAttribute("countCart", countCart);
		}

		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categories", allActiveCategory);
	}

	@GetMapping("/")
	public String index(Model model) {

		List<Category> allActiveCategory = categoryService.getAllActiveCategory().stream()
				.sorted((c1, c2) -> c2.getId().compareTo(c1.getId())).limit(6).toList();

		List<Product> allActiveProducts = productService.getAllActiveProducts("").stream()
				.sorted((p1, p2) -> p2.getId().compareTo(p1.getId())).limit(4).toList();
		model.addAttribute("category", allActiveCategory);
		model.addAttribute("products", allActiveProducts);
		return "index";
	}

	@GetMapping("/products")
	public String products(Model model, @RequestParam(value = "category", defaultValue = "") String category,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize,
			@RequestParam(value = "search", defaultValue = "") String search,
			@RequestParam(value = "sort", defaultValue = "") String sort) {
		System.out.println("search" + search);
		List<Category> categories = categoryService.findByIsActiveTrue();

		Page<Product> page = productService.findTop4ActiveProducts(pageNo, pageSize);
		model.addAttribute("categories", categories);
		model.addAttribute("paramValue", category);
		model.addAttribute("sort", sort);
		model.addAttribute("search", search);
		model.addAttribute("listProducts", page.getContent());
//		Page<Product> page = null;
//		if (StringUtils.isEmpty(search) && StringUtils.isEmpty(category)) {
//			page = productService.searchDiscountPricePagination(pageNo, pageSize, sort);
//
//		} else if (StringUtils.isEmpty(search)) {
//			page = productService.getAllActiveProductPagination(pageNo, pageSize, category);
//		} else {
//			page = productService.searchActiveProductPagination(pageNo, pageSize, category, search);
//		}
//
//		List<Product> products = page.getContent();
//
//		
//		model.addAttribute("productsSize", products.size());
//		model.addAttribute("pageNo", page.getNumber());
//		model.addAttribute("pageSize", pageSize);
//		model.addAttribute("totalElements", page.getTotalElements());
//		model.addAttribute("totalPages", page.getTotalPages());
//		model.addAttribute("isFirst", page.isFirst());
//		model.addAttribute("isLast", page.isLast());
		return "productAjax";
	}

	// load products by Ajax

	@GetMapping("/products/load")
	@ResponseBody
	public List<Product> productsAjax(Model model, @RequestParam(value = "category", defaultValue = "") String category,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "search", defaultValue = "") String search,
			@RequestParam(value = "sort", defaultValue = "") String sort) {
		System.out.println("search : " + search + "---pageNo :" + pageNo);
		List<Category> categories = categoryService.findByIsActiveTrue();
		model.addAttribute("categories", categories);
		model.addAttribute("paramValue", category);
		model.addAttribute("sort", sort);
		model.addAttribute("search", search);
		Page<Product> page = null;
		if (StringUtils.isEmpty(search)) {
			System.out.println("aloooooo");
			page = productService.getAllProductsPagination(pageNo, pageSize);
		} else {
			page = productService.searchActiveProductPagination(pageNo, pageSize, search);
		}

//		if (StringUtils.isEmpty(search) && StringUtils.isEmpty(category)) {
//			page = productService.searchDiscountPricePagination(pageNo, pageSize, sort);
//
//		} else if (StringUtils.isEmpty(search)) {
//			page = productService.getAllActiveProductPagination(pageNo, pageSize, category);
//		} else {
//			page = productService.searchActiveProductPagination(pageNo, pageSize, category, search);
//		}

		List<Product> products = page.getContent();
		System.out.println("size : " + products.size());

		return products;
	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/product/{id}")
	public String viewProduct(@PathVariable int id, Model model) {
		Product product = productService.getProductById(id);
		model.addAttribute("product", product);

		return "view_product";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, HttpSession session, @RequestParam("img") MultipartFile file)
			throws IOException {
		Boolean existsEmail = userService.checkExitsEmail(user.getEmail());

		if (existsEmail) {

			session.setAttribute("errorMsg", "Email already exists");
		} else {
			String imageName = file.isEmpty() ? "default.ap" : file.getOriginalFilename();
			user.setProfileImage(imageName);
			UserDtls saveUser = userService.saveUser(user);

			if (!ObjectUtils.isEmpty(saveUser)) {
				if (!file.isEmpty()) {
					File saveFile = new ClassPathResource("static/img").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
							+ file.getOriginalFilename());

					System.out.println("path :" + path);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				}
				session.setAttribute("succMsg", "Register successfully");
			} else {
				session.setAttribute("errorMsg", "something wrong on server");
			}
		}

		return "redirect:/register";
	}

//	@GetMapping("/search")
//	public String search(@RequestParam String search, Model model) {
//		List<Product> listProducts = productService.searchProduct(search, search);
//		System.out.println("search:" + search);
//		System.out.println(listProducts.size());
//		model.addAttribute("listProducts", listProducts);
//		return "product";
//	}
}
