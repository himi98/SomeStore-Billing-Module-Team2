package com.capstore.app.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coupon")
public class Coupon {

	@Id
	@Column(name = "coupon_id")
	private int couponId;
	@Column(name = "coupon_code")
	private String couponCode;
	@Column(name = "user_id")
	private int userId; // (Foreign Key)
	@Column(name = "end_date")
	private Date couponEndDate;
	@Column(name = "start_date")
	private Date couponStartDate;
	@Column(name = "coupon_amount")
	private int couponAmount;
	@Column(name = "min_order_amount")
	private int couponMinOrderAmount;
	@Column(name = "issued_by")
	private String issuedBy; // {“Admin”,”Merchant”}

	public Coupon() {
	}

	public int getCouponAmount() {
		return couponAmount;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public Date getCouponEndDate() {
		return couponEndDate;
	}

	public int getCouponId() {
		return couponId;
	}

	public int getCouponMinOrderAmount() {
		return couponMinOrderAmount;
	}

	public Date getCouponStartDate() {
		return couponStartDate;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public int getUserId() {
		return userId;
	}

	public void setCouponAmount(int couponAmount) {
		this.couponAmount = couponAmount;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public void setCouponEndDate(Date couponEndDate) {
		this.couponEndDate = couponEndDate;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public void setCouponMinOrderAmount(int couponMinOrderAmount) {
		this.couponMinOrderAmount = couponMinOrderAmount;
	}

	public void setCouponStartDate(Date couponStartDate) {
		this.couponStartDate = couponStartDate;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public void setUseId(int user_id) {
		this.userId = user_id;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
