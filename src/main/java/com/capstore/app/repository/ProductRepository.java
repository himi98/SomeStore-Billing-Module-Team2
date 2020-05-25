package main.java.com.capstore.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import main.java.com.capstore.app.models.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
	List<Product> findByOrderByProductPriceAsc();
	
	List<Product> findByOrderByProductPriceDesc();
	
	List<Product> findByOrderByProductRatingAsc();
	
	List<Product> findByOrderByProductRatingDesc();
}
