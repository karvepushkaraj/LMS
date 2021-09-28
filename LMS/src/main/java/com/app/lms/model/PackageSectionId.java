package com.app.lms.model;

import java.io.Serializable;

public class PackageSectionId implements Serializable {

	private static final long serialVersionUID = -7759046928604702040L;
	private LibrarySection section;
	private SubscriptionPackage pkg;

	public PackageSectionId() {
		super();
	}

	public PackageSectionId(LibrarySection section, SubscriptionPackage pkg) {
		this.section = section;
		this.pkg = pkg;
	}

	public LibrarySection getSection() {
		return section;
	}

	public SubscriptionPackage getPkg() {
		return pkg;
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
		PackageSectionId other = (PackageSectionId) obj;
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
		return "PackageSectionId [section=" + section + ", pkg=" + pkg + "]";
	}

}
