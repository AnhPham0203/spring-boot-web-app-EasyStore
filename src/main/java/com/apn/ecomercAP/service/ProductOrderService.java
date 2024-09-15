package com.apn.ecomercAP.service;

import java.util.List;

import com.apn.ecomercAP.model.OrderRequest;
import com.apn.ecomercAP.model.ProductOrder;

public interface ProductOrderService {
	public void saveOrder(Integer userid, OrderRequest orderRequest);
	
	public List<ProductOrder> getOrdersByUser(Integer userId);
}
