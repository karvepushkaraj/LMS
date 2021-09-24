package com.app.lms.model;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "members")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "memberIdSequence")
	@SequenceGenerator(name = "memberIdSequence", initialValue = 10000, allocationSize = 1)
	private int memberId;
	
	@Column(length = 50)
	private String name, mobileNumber, emailId;
	
	@Temporal(TemporalType.DATE)
	private Date enrollmentDate;
	
	@Temporal(TemporalType.DATE)
	private Date exitDate;
	
	@Convert(converter = MemActStatusConverter.class)
	private MemberActivityStatus status;
	
	@JsonIgnore
	@OneToOne
	@JoinColumns({@JoinColumn(name = "titleId"), @JoinColumn(name = "copyId")})
	private BookCopy book;
	
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private Set<Subscription> subscriptions;
	
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private Set<BookTransaction> booktransactions;
	
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private Set<Deposite> deposite;

	public Member() {
		super();
	}

	public Member(int memberId, String name, String mobileNumber, String emailId, Date enrollmentDate, Date exitDate,
			MemberActivityStatus status) {
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

	public MemberActivityStatus getStatus() {
		return status;
	}

	public void setStatus(MemberActivityStatus status) {
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
