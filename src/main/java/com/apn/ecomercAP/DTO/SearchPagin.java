package com.apn.ecomercAP.DTO;

import java.util.List;

import com.apn.ecomercAP.model.Product;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class SearchPagin {
	public List<Product> listProducts;
	public int productsSize;

	public int pageNo;
	public int pageSize;
	public long totalElements;
	public int totalPages;
	public boolean isLast;
	public boolean isFirst;

	public List<Product> getListProducts() {
		return listProducts;
	}

	public void setListProducts(List<Product> listProducts) {
		this.listProducts = listProducts;
	}

	public int getProductsSize() {
		return productsSize;
	}

	public void setProductsSize(int productsSize) {
		this.productsSize = productsSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}

	public boolean getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

}
