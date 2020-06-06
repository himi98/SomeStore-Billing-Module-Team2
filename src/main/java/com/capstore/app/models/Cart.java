package com.capstore.app.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Cart")
public class Cart {

	@Id
	@Column(name = "cart_id")
	private int cartId; // (Primary Key)
	@Column(name = "user_id")
	private int userID; // (references cust_id)
	@Column(name = "type")
	private String type; // (“Wishlist”,”cart”)
	@Column(name = "product_id")
	private int productId;
	@Column(name = "product_quantity")
	private int quantity;

	public Cart() {
	}

	public int getCartId() {
		return cartId;
	}

	public int getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getType() {
		return type;
	}

	public int getUserID() {
		return userID;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

}
