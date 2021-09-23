package com.app.lms.service;

import com.app.lms.model.LibrarySection;

public interface LibrarySectionService {
	
	LibrarySection getLibrarySection(String id);
	
	void addLibrarySection(LibrarySection bookSection);
	
	void updateLibrarySection(LibrarySection bookSection);
	
	void deleteLibrarySection(String id);
	
}
