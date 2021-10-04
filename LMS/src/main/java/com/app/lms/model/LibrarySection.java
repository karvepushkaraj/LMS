package com.app.lms.model;

import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity class for Library Section with SectionId as primary key.
 * 
 * @author karve
 *
 */

@Entity
public class LibrarySection {

	@Id
	@NotBlank
	@Pattern(regexp = "[\\w]{3}", message = "Invalid Section ID")
	@Column(length = 3)
	private String sectionId;

	@NotBlank
	@Pattern(regexp = "[\\w ]{1,50}", message = "Invalid section Name")
	@Column(length = 50, nullable = false, unique = true)
	private String sectionName;

	@JsonIgnore
	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<BookTitle> bookTitles;

	@JsonIgnore
	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<PackageSection> packageSection;

	public LibrarySection() {
		super();
	}

	public LibrarySection(String sectionId, String sectionName) {
		this.sectionId = sectionId;
		this.sectionName = sectionName;
	}

	public String getSectionId() {
		return sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Set<BookTitle> getBookTitles() {
		return Collections.unmodifiableSet(bookTitles);
	}

	public Set<PackageSection> getPackageSection() {
		return Collections.unmodifiableSet(packageSection);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sectionId == null) ? 0 : sectionId.hashCode());
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
		LibrarySection other = (LibrarySection) obj;
		if (sectionId == null) {
			if (other.sectionId != null)
				return false;
		} else if (!sectionId.equals(other.sectionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BookSection [sectionId=" + sectionId + ", sectionName=" + sectionName + "]";
	}

}
