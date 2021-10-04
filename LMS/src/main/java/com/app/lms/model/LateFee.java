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

	private static int lateFeeAmount = 20;

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

	public static int getLateFeeAmount() {
		return lateFeeAmount;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	public String toString() {
		return "LateFee [transaction=" + transaction + ", lateFees=" + lateFees + ", receiptNumber=" + receiptNumber
				+ "]";
	}

}
