package com.app.lms.model;

import java.io.Serializable;

/**
 * Id class for Book Copy.
 * 
 * @author karve
 *
 */

public class CopyId implements Serializable {

	private static final long serialVersionUID = 6689703140013128948L;
	private BookTitle title;
	private int copyId;

	public CopyId() {
		super();
	}

	public CopyId(BookTitle title, int copyId) {
		this.title = title;
		this.copyId = copyId;
	}

	public int getCopyId() {
		return copyId;
	}

	public BookTitle getTitle() {
		return title;
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
		CopyId other = (CopyId) obj;
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
		return "CopyId [title=" + title + ", copyId=" + copyId + "]";
	}

}
