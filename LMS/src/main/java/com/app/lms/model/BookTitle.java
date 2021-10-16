package com.app.lms.model;

import java.util.Collections;
import java.util.List;
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
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity class for Book Title with TitleId as primary key.
 * 
 * @author karve
 *
 */

@Entity
public class BookTitle {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "titleIdSequence")
	@SequenceGenerator(name = "titleIdSequence", initialValue = 1000, allocationSize = 1)
	private int titleId;

	@NotBlank
	@Column(length = 50, nullable = false, unique = true)
	private String title;

	@Column(length = 50)
	private String author, publication;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "sectionId")
	private LibrarySection section;

	@JsonIgnore
	@OneToMany(mappedBy = "title", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<BookCopy> bookCopies;

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

	public List<BookCopy> getBookCopies() {
		return Collections.unmodifiableList(bookCopies);
	}

	public boolean removeBookCopy(BookCopy bookCopy) {
		return bookCopies.remove(bookCopy);
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
