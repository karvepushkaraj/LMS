package com.app.lms.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.app.lms.util.TransactionStatusConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@Entity
public class Subscription {

	@Id
	@Column(length = 20)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subIdSequence")
	@SequenceGenerator(name = "subIdSequence", initialValue = 1, allocationSize = 1)
	private int subscriptionId;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "packageId")
	private SubscriptionPackage pkg;

	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Convert(converter = TransactionStatusConverter.class)
	private ActivityStatus status;

	@JsonIgnore
	@OneToOne(mappedBy = "subscription", cascade = CascadeType.ALL)
	private SubscriptionFee subscriptionFee;

	public Subscription() {
		super();
	}

	public Subscription(Member member, SubscriptionPackage pkg, Date startDate, ActivityStatus status) {
		this.member = member;
		this.pkg = pkg;
		this.startDate = startDate;
		this.status = status;
	}

	public int getSubscriptionId() {
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

	public ActivityStatus getStatus() {
		return status;
	}

	public void setStatus(ActivityStatus status) {
		this.status = status;
	}

	public SubscriptionFee getSubscriptionFee() {
		return subscriptionFee;
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
		Subscription other = (Subscription) obj;
		if (subscriptionId != other.subscriptionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Subscription [subscriptionId=" + subscriptionId + ", member=" + member + ", SubscriptionPackage=" + pkg
				+ ", startDate=" + startDate + ", status=" + status + "]";
	}

}
