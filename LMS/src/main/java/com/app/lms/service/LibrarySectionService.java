package com.app.lms.service;

import com.app.lms.model.LibrarySection;

public interface LibrarySectionService {

	LibrarySection getLibrarySection(String id);

	void addLibrarySection(LibrarySection librarySection);

	void updateLibrarySection(LibrarySection librarySection);

	boolean deleteLibrarySection(String id);

}
