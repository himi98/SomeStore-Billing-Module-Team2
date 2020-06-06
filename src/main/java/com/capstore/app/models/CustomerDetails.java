package com.capstore.app.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.capstore.app.models.Cart;
import com.capstore.app.models.CommonFeedback;
import com.capstore.app.models.ProductFeedback;
import com.capstore.app.models.UserAddress;

@Entity
@Table(name = "customer_details")
public class CustomerDetails extends User {

	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(name = "alternate_phone_number")
	private String alternatePhoneNumber;
	@Column(name = "alternate_email")
	private String alternateEmail;
	@Column(name = "address")
	private String address;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = CommonFeedback.class)
	Set<CommonFeedback> cCF;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = ProductFeedback.class)
	Set<ProductFeedback> cPF;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Order.class)
	Set<Order> orders;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Cart.class)
	private Set<Cart> cC;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = UserAddress.class)
	private Set<UserAddress> addresses;

	public CustomerDetails() {
	}

	public String getAddress() {
		return address;
	}

	Set<UserAddress> getAddresses() {
		return addresses;
	}

	public String getAlternateEmail() {
		return alternateEmail;
	}

	public String getAlternatePhoneNumber() {
		return alternatePhoneNumber;
	}

	public Set<Cart> getcC() {
		return cC;
	}

	public Set<CommonFeedback> getcCF() {
		return cCF;
	}

	public Set<ProductFeedback> getcPF() {
		return cPF;
	}

	public Set<Cart> getCustomerCarts() {
		return cC;
	}

	public Set<CommonFeedback> getFeedbacks() {
		return cCF;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Set<ProductFeedback> getProductFeedbacks() {
		return cPF;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public void setcC(Set<Cart> cC) {
		this.cC = cC;
	}

	public void setcCF(Set<CommonFeedback> cCF) {
		this.cCF = cCF;
	}

	public void setcPF(Set<ProductFeedback> cPF) {
		this.cPF = cPF;
	}

	public void setCustomerCarts(Set<Cart> customerCarts) {
		this.cC = customerCarts;
	}

	public void setFeedbacks(Set<CommonFeedback> feedbacks) {
		this.cCF = feedbacks;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setProductFeedbacks(Set<ProductFeedback> productFeedbacks) {
		this.cPF = productFeedbacks;
	}

}
