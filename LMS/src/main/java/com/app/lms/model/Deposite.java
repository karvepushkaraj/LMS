package com.app.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

/**
 * Entity class for Deposite with TransactionId as primary key.
 * 
 * @author karve
 *
 */

@Entity
public class Deposite {

	private static int memberDespoite = 500;

	@Id
	@Column(length = 20)
	private String transactionId;

	private int deposite;

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String receiptNumber;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;

	public Deposite() {
		super();
	}

	public Deposite(String transactionId, int deposite, String receiptNumber, Member member) {
		this.transactionId = transactionId;
		this.deposite = deposite;
		this.receiptNumber = receiptNumber;
		this.member = member;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public int getDeposite() {
		return deposite;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public Member getMember() {
		return member;
	}

	public static int getMemberDespoite() {
		return memberDespoite;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Override
	public String toString() {
		return "Deposite [transactionId=" + transactionId + ", deposite=" + deposite + ", receiptNumber="
				+ receiptNumber + ", member=" + member + "]";
	}

}
