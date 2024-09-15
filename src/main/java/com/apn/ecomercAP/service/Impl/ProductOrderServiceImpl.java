package com.apn.ecomercAP.service.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apn.ecomercAP.model.Cart;
import com.apn.ecomercAP.model.OrderAddress;
import com.apn.ecomercAP.model.OrderRequest;
import com.apn.ecomercAP.model.ProductOrder;
import com.apn.ecomercAP.repository.CartRepository;
import com.apn.ecomercAP.repository.ProductOrderRepository;

import com.apn.ecomercAP.service.ProductOrderService;
import com.apn.ecomercAP.util.CommonUtil;
import com.apn.ecomercAP.util.OrderStatus;

@Service
public class ProductOrderServiceImpl implements ProductOrderService{

	@Autowired
	private ProductOrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Override
	public void saveOrder(Integer userid, OrderRequest orderRequest) {
		List<Cart> carts = cartRepository.findByUserId(userid);

		for (Cart cart : carts) {

			ProductOrder order = new ProductOrder();

			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());

			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getDiscountPrice());

			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());

			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orderRequest.getPaymentType());

			OrderAddress address = new OrderAddress();
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNo(orderRequest.getMobileNo());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());

			order.setOrderAddress(address);

			ProductOrder saveOrder = orderRepository.save(order);
//			commonUtil.sendMailForProductOrder(saveOrder, "success");
		}
		
	}

	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		List<ProductOrder> productOrders=orderRepository.findByUserId(userId);
		return productOrders;
	}

}
