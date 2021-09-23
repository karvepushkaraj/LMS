package com.app.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "subscriptions")
public class Subscription {

	@Id
	@Column(length = 20)
	private String subscriptionId;
	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;
	@ManyToOne
	@JoinColumn(name = "packageId")
	private SubscriptionPackage pkg;
	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Convert(converter = TransactionStatusConverter.class)
	private TransactionStatus status;
	@OneToOne(mappedBy = "subscription")
	private SubscriptionFee subscriptionFee;
	
	public Subscription() {
		super();
	}

	public Subscription(String subscriptionId, Member member, SubscriptionPackage pkg, Date startDate,
			TransactionStatus status) {
		this.subscriptionId = subscriptionId;
		this.member = member;
		this.pkg = pkg;
		this.startDate = startDate;
		this.status = status;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public Member getMember() {
		return member;
	}

	public SubscriptionPackage getSubscriptionPackage() {
		return pkg;
	}

	public Date getStartDate() {
		return startDate;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public SubscriptionFee getSubscriptionFee() {
		return subscriptionFee;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subscriptionId == null) ? 0 : subscriptionId.hashCode());
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
		Subscription other = (Subscription) obj;
		if (subscriptionId == null) {
			if (other.subscriptionId != null)
				return false;
		} else if (!subscriptionId.equals(other.subscriptionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Subscription [subscriptionId=" + subscriptionId + ", member=" + member + ", SubscriptionPackage=" + pkg
				+ ", startDate=" + startDate + ", status=" + status + "]";
	}

}
