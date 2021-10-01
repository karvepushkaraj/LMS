package com.app.lms.service;

import com.app.lms.model.LibrarySection;

/**
 * Service interface for all operations related to Library Sections.
 * 
 * @author karve
 *
 */

public interface LibrarySectionService {

	LibrarySection getLibrarySection(String id);

	void addLibrarySection(LibrarySection librarySection);

	void updateLibrarySection(LibrarySection librarySection);

	boolean deleteLibrarySection(String id);

}
