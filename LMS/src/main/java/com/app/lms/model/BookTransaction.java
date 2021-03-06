package com.app.lms.model;

import java.util.Date;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.app.lms.util.ActivityStatusConverter;

/**
 * Entity class for Book Transaction with TransactionId as primary key.
 * 
 * @author karve
 *
 */

@Entity
public class BookTransaction {

	public static final int LATE_RETURN_DAYS = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookTransSequence")
	@SequenceGenerator(name = "bookTransSequence", initialValue = 1, allocationSize = 1)
	private int transactionId;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private Member member;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "copyId"), @JoinColumn(name = "titleId") })
	private BookCopy book;

	@Temporal(TemporalType.TIMESTAMP)
	private Date issueDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date returnDate;

	@Convert(converter = ActivityStatusConverter.class)
	private ActivityStatus status;

	public BookTransaction() {
		super();
	}

	public BookTransaction(Member member, BookCopy book, Date issueDate, Date returnDate, ActivityStatus status) {
		this.member = member;
		this.book = book;
		this.issueDate = issueDate;
		this.returnDate = returnDate;
		this.status = status;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public Member getMember() {
		return member;
	}

	public BookCopy getBook() {
		return book;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public ActivityStatus getStatus() {
		return status;
	}

	public void setStatus(ActivityStatus status) {
		this.status = status;
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
		BookTransaction other = (BookTransaction) obj;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BookTransaction [transactionId=" + transactionId + ", member=" + member + ", book=" + book
				+ ", issueDate=" + issueDate + ", returnDate=" + returnDate + ", status=" + status + "]";
	}

}
