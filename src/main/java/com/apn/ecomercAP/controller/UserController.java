package com.apn.ecomercAP.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apn.ecomercAP.model.Cart;
import com.apn.ecomercAP.model.Category;
import com.apn.ecomercAP.model.OrderRequest;
import com.apn.ecomercAP.model.ProductOrder;
import com.apn.ecomercAP.model.UserDtls;
import com.apn.ecomercAP.service.CartService;
import com.apn.ecomercAP.service.CategoryService;
import com.apn.ecomercAP.service.ProductOrderService;
import com.apn.ecomercAP.service.UserService;
import com.apn.ecomercAP.util.CommonUtil;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private ProductOrderService orderService;

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

	@GetMapping("/addCart")
	public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {

		Cart saveCart = cartService.saveCart(pid, uid);

		if (ObjectUtils.isEmpty(saveCart)) {
			session.setAttribute("errorMsg", "Product add to cart failed");
		} else {
			session.setAttribute("succMsg", "Product added to cart");
		}
		return "redirect:/product/" + pid;
	}

	@GetMapping("/cart")
	public String viewCart(Model model, Principal p) {
		UserDtls user = commonUtil.getLoggedInUserDetails(p);
		List<Cart> carts = cartService.getCartsByUser(user.getId());
		model.addAttribute("carts", carts);
		if (carts.size() > 0) {
			Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
			model.addAttribute("totalOrderPrice", totalOrderPrice);
		}

		return "user/cart";
	}

	@GetMapping("/cartQuantityUpdate")
	public String quantityUpdate(@RequestParam(name = "sy") String sy, @RequestParam(name = "cid") int cid) {
		cartService.updateQuantity(sy, cid);
		return "redirect:/user/cart";
	}

	@GetMapping("/orders")
	public String order(Model model, Principal p) {
		UserDtls user = commonUtil.getLoggedInUserDetails(p);
		List<Cart> carts = cartService.getCartsByUser(user.getId());
		model.addAttribute("carts", carts);
		if (carts.size() > 0) {
			Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
			Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice() + 250 + 100;
			model.addAttribute("totalOrderPrice", totalOrderPrice);
			model.addAttribute("orderPrice", orderPrice);
		}
		return "/user/order";
	}
	
	@PostMapping("/save-order")
	public String saveOrder(@ModelAttribute OrderRequest orderRequest, Principal p) {
		UserDtls user = commonUtil.getLoggedInUserDetails(p);
		orderService.saveOrder(user.getId(), orderRequest);
		return "redirect:/user/success";
	}
	
	@GetMapping("/success")
	public String loadSuccess() {
		return "/user/success";
	}
	
	@GetMapping("/user-orders")
	public String myOrder(Principal p ,Model model) {
		UserDtls user = commonUtil.getLoggedInUserDetails(p);
		List<ProductOrder> orders=orderService.getOrdersByUser(user.getId());
		model.addAttribute("orders", orders);
		return "/user/my_orders";
	}
	

}
