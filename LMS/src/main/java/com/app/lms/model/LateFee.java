package com.app.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "latefees")
public class LateFee {

	@Id
	private int transactionId;
	private float lateFees;
	@Column(length = 20)
	private String receiptNumber;
	@OneToOne
	@PrimaryKeyJoinColumn
	private BookTransaction transaction;
	
	public LateFee() {
		super();
	}

	public LateFee(BookTransaction transaction, float lateFees, String receiptNumber) {
		this.transaction = transaction;
		this.lateFees = lateFees;
		this.receiptNumber = receiptNumber;
	}

	public BookTransaction getTransaction() {
		return transaction;
	}

	public float getLateFees() {
		return lateFees;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	@Override
	public String toString() {
		return "LateFee [transaction=" + transaction + ", lateFees=" + lateFees + ", receiptNumber=" + receiptNumber
				+ "]";
	}

}
