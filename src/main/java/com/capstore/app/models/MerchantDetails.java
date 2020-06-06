package com.capstore.app.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.capstore.app.models.Coupon;
import com.capstore.app.models.Product;
import com.capstore.app.models.ProductFeedback;
import com.capstore.app.models.UserAddress;

@Entity
@Table(name = "merchant_details")
public class MerchantDetails extends User {

	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(name = "alternate_phone_number")
	private String alternatePhoneNumber;
	@Column(name = "alternate_email")
	private String alternateEmail;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Product.class)
	private Set<Product> products;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = UserAddress.class)
	private Set<UserAddress> addresses;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = ProductFeedback.class)
	private Set<ProductFeedback> pF;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Coupon.class)
	Set<Coupon> coupons;
	@Column(name = "is_approved")
	private boolean isApproved;
	@Column(name = "rating")
	private int rating;

	public MerchantDetails() {
		super();
		this.isApproved = false;
	}

	public Set<UserAddress> getAddresses() {
		return addresses;
	}

	public String getAlternateEmail() {
		return alternateEmail;
	}

	public String getAlternatePhoneNumber() {
		return alternatePhoneNumber;
	}

	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public Set<ProductFeedback> getpF() {
		return pF;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Set<ProductFeedback> getProductFeedback() {
		return pF;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public int getRating() {
		return rating;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setAddresses(Set<UserAddress> addresses) {
		this.addresses = addresses;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}

	public void setAlternatePhoneNumber(String alternatePhoneNumber) {
		this.alternatePhoneNumber = alternatePhoneNumber;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	public void setpF(Set<ProductFeedback> pF) {
		this.pF = pF;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setProductFeedback(Set<ProductFeedback> productFeedback) {
		this.pF = productFeedback;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
