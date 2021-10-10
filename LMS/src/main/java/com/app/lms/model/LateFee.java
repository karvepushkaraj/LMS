package com.app.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;

/**
 * Entity class for Late Fees with TransactionId as primary key.
 * 
 * @author karve
 *
 */

@Entity
public class LateFee {

	public static final int LATE_FEE = 20;

	@Id
	private int transactionId;

	private int lateFees;

	@NotBlank
	@Column(length = 20, nullable = false, unique = true)
	private String receiptNumber;

	@OneToOne
	@PrimaryKeyJoinColumn
	private BookTransaction transaction;

	public LateFee() {
		super();
	}

	public LateFee(BookTransaction transaction, int lateFees, String receiptNumber) {
		this.transaction = transaction;
		this.lateFees = lateFees;
		this.receiptNumber = receiptNumber;
	}

	public BookTransaction getTransaction() {
		return transaction;
	}

	public int getLateFees() {
		return lateFees;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public static boolean isInvalid(LateFee fee) {
		return fee == null || fee.getLateFees() != LATE_FEE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + transactionId;
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
		LateFee other = (LateFee) obj;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LateFee [transaction=" + transaction + ", lateFees=" + lateFees + ", receiptNumber=" + receiptNumber
				+ "]";
	}

}
