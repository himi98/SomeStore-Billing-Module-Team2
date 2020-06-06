package com.capstore.app.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product_feedback")
public class ProductFeedback {
	@Id
	@Column(name = "feedback_Id")
	private Integer feedbackId;
	@Column(name = "feedback_subject")
	private String feedbackSubject;
	@Column(name = "feedback_message")
	private String feedbackMessage;
	@Column(name = "product_id")
	private int productId;
	@Column(name = "user_id")
	private int userId;

	public ProductFeedback() {
	}

	public Integer getFeedbackId() {
		return feedbackId;
	}

	public String getFeedbackMessage() {
		return feedbackMessage;
	}

	public String getFeedbackSubject() {
		return feedbackSubject;
	}

	public int getProductId() {
		return productId;
	}

	public int getUserId() {
		return userId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public void setFeedbackMessage(String feedbackMessage) {
		this.feedbackMessage = feedbackMessage;
	}

	public void setFeedbackSubject(String feedbackSubject) {
		this.feedbackSubject = feedbackSubject;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
