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

	public static final int MEMBER_DEPOSITE = 500;

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

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public static boolean isInvalid(Deposite deposite) {
		return deposite == null || deposite.getDeposite() != MEMBER_DEPOSITE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
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
		Deposite other = (Deposite) obj;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Deposite [transactionId=" + transactionId + ", deposite=" + deposite + ", receiptNumber="
				+ receiptNumber + ", member=" + member + "]";
	}

}
