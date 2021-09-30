package com.app.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Deposite {

	@Id
	private String transactionId;

	private float deposite;

	@Column(length = 20)
	private String receiptNumber;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;

	public Deposite() {
		super();
	}

	public Deposite(String transactionId, float deposite, String receiptNumber, Member member) {
		this.transactionId = transactionId;
		this.deposite = deposite;
		this.receiptNumber = receiptNumber;
		this.member = member;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public float getDeposite() {
		return deposite;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public Member getMember() {
		return member;
	}

	@Override
	public String toString() {
		return "Deposite [transactionId=" + transactionId + ", deposite=" + deposite + ", receiptNumber="
				+ receiptNumber + ", member=" + member + "]";
	}

}
