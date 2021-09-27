package com.app.lms.model;

import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "booktitles")
public class BookTitle {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "titleIdSequence")
	@SequenceGenerator(name = "titleIdSequence", initialValue = 1000, allocationSize = 1)
	private int titleId;
	
	@Column(length = 50)
	private String title, author, publication;
	
	@ManyToOne
	@JoinColumn(name = "sectionId")
	private LibrarySection section;
	
	@JsonIgnore
	@OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<BookCopy> bookCopies;
	
	public BookTitle() {
		super();
	}

	public BookTitle(int titleId, LibrarySection section, String title, String author, String publication) {
		this.titleId = titleId;
		this.section = section;
		this.title = title;
		this.author = author;
		this.publication = publication;
	}

	public int getTitleId() {
		return titleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public LibrarySection getSection() {
		return section;
	}

	public void setSection(LibrarySection section) {
		this.section = section;
	}

	public Set<BookCopy> getBookCopies() {
		return Collections.unmodifiableSet(bookCopies);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + titleId;
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
		BookTitle other = (BookTitle) obj;
		if (titleId != other.titleId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BookTitle [titleId=" + titleId + ", title=" + title + ", author=" + author + ", publication="
				+ publication + "]";
	}

}
