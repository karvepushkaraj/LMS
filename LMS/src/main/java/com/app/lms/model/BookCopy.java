package com.app.lms.model;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bookcopy")
@IdClass(CopyId.class)
public class BookCopy {
	
	@Id
	@ManyToOne
	@JoinColumn(name = "titleId")
//	@JsonBackReference
	private BookTitle title;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "copyIdSequence")
	@SequenceGenerator(name = "copyIdSequence", initialValue = 1, allocationSize = 1)
	private int copyId;
	
	private float price;
	
	private Date purchaseDate;
	
	@OneToOne
	@JoinColumn(name = "memberId")
	private Member member;
	
	@JsonIgnore
	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
	private Set<BookTransaction> booktransactions;

	public BookCopy() {
		super();
	}

	public BookCopy(BookTitle title, int copyId, float price, Date purchaseDate) {
		this.title = title;
		this.copyId = copyId;
		this.price = price;
		this.purchaseDate = purchaseDate;
	}

	public int getCopyId() {
		return copyId;
	}
	
	public BookTitle getTitle() {
		return title;
	}
	
	public void setTitle(BookTitle title) {
		this.title = title;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Set<BookTransaction> getBooktransactions() {
		return Collections.unmodifiableSet(booktransactions);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + copyId;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		BookCopy other = (BookCopy) obj;
		if (copyId != other.copyId)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BookCopy [copyId=" + copyId + ", price=" + price + ", purchaseDate=" + purchaseDate
				+ "]";
	}

}
