package com.app.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(PackageSectionId.class)
public class PackageSection {

	@Id
	@ManyToOne
	@JoinColumn(name = "sectionId")
	private LibrarySection section;

	@Id
	@ManyToOne
	@JoinColumn(name = "packageId")
	private SubscriptionPackage pkg;

	@Column(nullable = false)
	private int numberOfBooks;

	public PackageSection() {
		super();
	}

	public PackageSection(LibrarySection section, SubscriptionPackage pkg, int numberOfBooks) {
		this.section = section;
		this.pkg = pkg;
		this.numberOfBooks = numberOfBooks;
	}

	public LibrarySection getSection() {
		return section;
	}

	public SubscriptionPackage getPkg() {
		return pkg;
	}

	public int getNumberOfBooks() {
		return numberOfBooks;
	}

	public void setNumberOfBooks(int numberOfBooks) {
		this.numberOfBooks = numberOfBooks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pkg == null) ? 0 : pkg.hashCode());
		result = prime * result + ((section == null) ? 0 : section.hashCode());
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
		PackageSection other = (PackageSection) obj;
		if (pkg == null) {
			if (other.pkg != null)
				return false;
		} else if (!pkg.equals(other.pkg))
			return false;
		if (section == null) {
			if (other.section != null)
				return false;
		} else if (!section.equals(other.section))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PackageSection [section=" + section + ", pkg=" + pkg + ", numberOfBooks=" + numberOfBooks + "]";
	}

}
