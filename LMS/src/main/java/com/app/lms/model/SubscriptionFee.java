package com.app.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;

@Entity
public class SubscriptionFee {

	@Id
	private int subscriptionId;

	private int fees;

	@NotBlank
	@Column(length = 20)
	private String receiptNumber;

	@OneToOne
	@PrimaryKeyJoinColumn
	private Subscription subscription;

	public SubscriptionFee() {
		super();
	}

	public SubscriptionFee(Subscription subscription, int fees, String receiptNumber) {
		this.subscription = subscription;
		this.fees = fees;
		this.receiptNumber = receiptNumber;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public int getFees() {
		return fees;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}
	
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	@Override
	public String toString() {
		return "SubscriptionFees [subscription=" + subscription + ", fees=" + fees + ", receiptNumber=" + receiptNumber
				+ "]";
	}

}
