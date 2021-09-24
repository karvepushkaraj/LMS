package com.app.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.lms.model.LibrarySection;
import com.app.lms.service.LibrarySectionService;

@RestController
@RequestMapping("api/v1/lms")
public class LibraryAdminController {
	
	@Autowired
	@Qualifier("LibraryAdminService")
	private LibrarySectionService librarySectionService;
	
	@GetMapping("/librarysection")
	public LibrarySection getLibrarySection(@RequestParam("id") String id) {
		return librarySectionService.getLibrarySection(id);
	}
		
	@PostMapping("/librarysection")
	public void addLibrarySection(@RequestBody LibrarySection librarySection) {
		librarySectionService.addLibrarySection(librarySection);
	}
	
	@PutMapping("/librarysection")
	public void updateLibrarySection(@RequestBody LibrarySection librarySection) {
		librarySectionService.updateLibrarySection(librarySection);
	}

	@DeleteMapping("/librarysection/{id}")
	public void deleteLibrarySection(@PathVariable("id") String id) {
		librarySectionService.deleteLibrarySection(id);
	}
}
