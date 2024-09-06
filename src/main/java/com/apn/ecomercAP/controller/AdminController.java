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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.apn.ecomercAP.model.Category;
import com.apn.ecomercAP.model.Product;
import com.apn.ecomercAP.model.UserDtls;
import com.apn.ecomercAP.service.CategoryService;
import com.apn.ecomercAP.service.ProductService;
import com.apn.ecomercAP.service.UserService;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

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

	@GetMapping("/")
	public String hone() {
		return "admin/index";
	}

	// view product
	@GetMapping("/viewProducts")
	public String viewProduct(Model model, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize,
			@RequestParam(defaultValue = "") String search) {

		model.addAttribute("search", search);
		Page<Product> pageProduct = null;

		if (StringUtils.isEmpty(search)) {
			pageProduct = productService.getAllProductsPagination(pageNo, pageSize);
		} else {
			pageProduct = productService.searchProductPagination(pageNo, pageSize, search);
		}
//		List<Product> products = productService.getAllProducts();
		model.addAttribute("products", pageProduct.getContent());

		model.addAttribute("pageNo", pageNo);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalElements", pageProduct.getTotalElements());
		model.addAttribute("totalPages", pageProduct.getTotalPages());
		model.addAttribute("isFirst", pageProduct.isFirst());
		model.addAttribute("isLast", pageProduct.isLast());

		return "admin/products";
	}

	@GetMapping("/editProduct/{id}")
	public String editProducts(@PathVariable Integer id, Model model) {

		Product product = productService.getProductById(id);
		model.addAttribute("product", product);
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		return "admin/edit_product";
	}

	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file,
			HttpSession session, Model model) {
		System.out.println("stock :" + product.getStock());
		if (product.getStock() < 0) {
			session.setAttribute("errorMsg", "invalid stock");
		} else if (product.getDiscount() < 0 || product.getDiscount() > 100) {
			session.setAttribute("errorMsg", "invalid Discount");
		} else {
			Product updateProduct = productService.updateProduct(product, file);
			if (!ObjectUtils.isEmpty(updateProduct)) {
				session.setAttribute("succMsg", "Product update success");
			} else {
				session.setAttribute("errorMsg", "Something wrong on server");
			}
		}

		return "redirect:/admin/editProduct/" + product.getId();
	}

	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id, HttpSession session) {
		Boolean deleteProduct = productService.deleteProduct(id);
		if (deleteProduct) {
			session.setAttribute("succMsg", "Product delete success");
		} else {
			session.setAttribute("errorMsg", "Something wrong on server");
		}
		return "redirect:/admin/viewProducts";
	}

	@GetMapping("/loadAddProduct")
	public String loadAddProduct() {
		return "admin/add_product";
	}

	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product, HttpSession session,
			@RequestParam("file") MultipartFile file) throws IOException {
		String imageName = file.isEmpty() ? "default.ap" : file.getOriginalFilename();
		product.setImage(imageName);
		product.setDiscount(0);
		Product saveProduct = productService.saveProduct(product);
		if (!ObjectUtils.isEmpty(saveProduct)) {
			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
						+ file.getOriginalFilename());

				System.out.println("path :" + path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			session.setAttribute("succMsg", "Product saved Succcessfully!");
		} else {
			session.setAttribute("errorMsg", "something wrong on server!");
		}

		return "redirect:/admin/loadAddProduct";
	}
}
