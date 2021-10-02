package com.app.lms.service;

import com.app.lms.model.LibrarySection;
import com.app.lms.util.InvalidBusinessCondition;

/**
 * Service interface for all operations related to Library Sections.
 * 
 * @author karve
 *
 */

public interface LibrarySectionService {

	LibrarySection getLibrarySection(String id) throws InvalidBusinessCondition;

	void addLibrarySection(LibrarySection librarySection) throws InvalidBusinessCondition;

	void updateLibrarySection(LibrarySection librarySection) throws InvalidBusinessCondition;

	void deleteLibrarySection(String id) throws InvalidBusinessCondition;

}
