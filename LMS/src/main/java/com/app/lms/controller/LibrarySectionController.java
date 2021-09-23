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
@RequestMapping("api/v1/lms/librarysection")
public class LibrarySectionController {
	
	@Autowired
	@Qualifier("LibrarySectionService")
	private LibrarySectionService librarySectionService;
	
	@GetMapping
	public LibrarySection getLibrarySection(@RequestParam("id") String id) {
		return librarySectionService.getLibrarySection(id);
	}
	
//	@GetMapping
//	public String getBookSection(@RequestParam("id") String id) throws JsonProcessingException {
//		BookSection bd = bookSectionService.getBookSection(id);
//		return new ObjectMapper().writeValueAsString(bd);
//	}
	
	@PostMapping
	public void addLibrarySection(@RequestBody LibrarySection bookSection) {
		librarySectionService.addLibrarySection(bookSection);
	}
	
//	@PostMapping
//	public void addBookSection(@RequestBody String bookSection) throws JsonMappingException, JsonProcessingException {
//		ObjectMapper obj = new ObjectMapper();
//		BookSection bs = obj.readValue(bookSection, BookSection.class);
//		bookSectionService.addBookSection(bs);
//	}
	
	@PutMapping
	public void updateLibrarySection(@RequestBody LibrarySection bookSection) {
		librarySectionService.updateLibrarySection(bookSection);
	}

	@DeleteMapping("/{id}")
	public void deleteLibrarySection(@PathVariable("id") String id) {
		librarySectionService.deleteLibrarySection(id);
	}
}
