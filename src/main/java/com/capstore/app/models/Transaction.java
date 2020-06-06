package com.capstore.app.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {

	@Id
	@Column(name = "transaction_id")
	private int transactionId;
	@Column(name = "order_id")
	private int orderId;
	@Column(name = "transaction_date")
	private Date transactionDate;
	@Column(name = "transaction_money")
	private int transactionMoney;
	@Column(name = "transaction_method")
	private String transactionMethod; // (“Credit”,”Debit”,”UPI”,”Wallet”)
	@Column(name = "transaction_status")
	private String transactionStatus; // (“Success”,”Fail”,”Pending”)

	public Transaction() {
	}

	public int getOrderId() {
		return orderId;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public String getTransactionMethod() {
		return transactionMethod;
	}

	public int getTransactionMoney() {
		return transactionMoney;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public void setTransactionMethod(String transactionMethod) {
		this.transactionMethod = transactionMethod;
	}

	public void setTransactionMoney(int transactionMoney) {
		this.transactionMoney = transactionMoney;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

}
