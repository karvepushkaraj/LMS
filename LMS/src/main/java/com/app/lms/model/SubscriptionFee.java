package com.app.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;

/**
 * Entity class for Subscription Fees with SubscriptionId as primary key.
 * 
 * @author karve
 *
 */

@Entity
public class SubscriptionFee {

	@Id
	private int subscriptionId;

	private int fees;

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
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

	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + subscriptionId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubscriptionFee other = (SubscriptionFee) obj;
		if (subscriptionId != other.subscriptionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubscriptionFees [subscription=" + subscription + ", fees=" + fees + ", receiptNumber=" + receiptNumber
				+ "]";
	}

}
