package com.app.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class SubscriptionFee {

	@Id
	private int subscriptionId;

	private float fees;

	@Column(length = 20)
	private String receiptNumber;

	@OneToOne
	@PrimaryKeyJoinColumn
	private Subscription subscription;

	public SubscriptionFee() {
		super();
	}

	public SubscriptionFee(Subscription subscription, float fees, String receiptNumber) {
		this.subscription = subscription;
		this.fees = fees;
		this.receiptNumber = receiptNumber;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public float getFees() {
		return fees;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	@Override
	public String toString() {
		return "SubscriptionFees [subscription=" + subscription + ", fees=" + fees + ", receiptNumber=" + receiptNumber
				+ "]";
	}

}
