package com.capstore.app.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import com.capstore.app.models.Product;

@Service
public interface ProductServiceInterface {

	public List<Product> allProductsList();

	/*
	 * public List<Product> sortAsc(); public List<Product> sortDesc(); public
	 * List<Product> sortRasc(); public List<Product> sortRdesc();
	 */
	List<Product> filterAndCategory(String category, String order);

	public List<Product> searchProducts(String category);

	public List<Product> specificCategoryProducts(String category);

	public List<Product> specificDiscountProducts(String category, String discount);
}
