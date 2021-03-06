package com.app.lms.model;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.app.lms.util.ActivityStatusConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity class for Member with MemberId as primary key.
 * 
 * @author karve
 *
 */

@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memberIdSequence")
	@SequenceGenerator(name = "memberIdSequence", initialValue = 10000, allocationSize = 1)
	private int memberId;

	@NotBlank
	@Length(max = 50)
	@Column(length = 50, nullable = false)
	private String name;

	@NotBlank
	@Pattern(regexp = "[0-9]{10}", message = "Invalid mobile number")
	@Column(length = 50, nullable = false)
	private String mobileNumber;

	@Email(message = "Invalid email id")
	@Column(length = 50)
	private String emailId;

	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date enrollmentDate;

	@JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy")
	@Temporal(TemporalType.DATE)
	private Date exitDate;

	@Convert(converter = ActivityStatusConverter.class)
	private ActivityStatus status;

	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<BookCopy> books;

	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Subscription> subscriptions;

	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<BookTransaction> booktransactions;

	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Deposite> deposite;

	public Member() {
		super();
	}

	public Member(int memberId, String name, String mobileNumber, String emailId, Date enrollmentDate, Date exitDate,
			ActivityStatus status) {
		this.memberId = memberId;
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.enrollmentDate = enrollmentDate;
		this.exitDate = exitDate;
		this.status = status;
	}

	public int getMemberId() {
		return memberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public ActivityStatus getStatus() {
		return status;
	}

	public void setStatus(ActivityStatus status) {
		this.status = status;
	}

	public Date getExitDate() {
		return exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	public Set<Subscription> getSubscriptions() {
		return Collections.unmodifiableSet(subscriptions);
	}

	public Set<BookTransaction> getBooktransactions() {
		return Collections.unmodifiableSet(booktransactions);
	}

	public Set<Deposite> getDeposite() {
		return Collections.unmodifiableSet(deposite);
	}

	public Set<BookCopy> getBook() {
		return Collections.unmodifiableSet(books);
	}

	public boolean addBook(BookCopy book) {
		return books.add(book);
	}

	public boolean removeBook(BookCopy book) {
		return books.remove(book);
	}

	public boolean isActive() {
		return status == ActivityStatus.ACTIVE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + memberId;
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
		Member other = (Member) obj;
		if (memberId != other.memberId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Member [memberId=" + memberId + ", name=" + name + ", mobileNumber=" + mobileNumber + ", emailId="
				+ emailId + ", enrollmentDate=" + enrollmentDate + ", exitDate=" + exitDate + ", status=" + status
				+ "]";
	}

}
