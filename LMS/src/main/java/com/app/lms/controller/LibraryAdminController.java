package com.app.lms.controller;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.app.lms.model.SubscriptionPackage;
import com.app.lms.service.LibrarySectionService;
import com.app.lms.service.SubscriptionPackageService;
import com.app.lms.util.IllegalRequestException;
import com.app.lms.util.InvalidBusinessCondition;
import com.app.lms.util.LMSExceptionHandler;
import com.app.lms.util.SubPkgJsonSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Controller for Library Admin Operations. Note : Exceptions of this class are
 * handled by {@link LMSExceptionHandler}
 * 
 * @author karve
 *
 */

@RestController
@RequestMapping("api/v1/lms")
public class LibraryAdminController {

	@Autowired
	private LibrarySectionService librarySectionService;

	@Autowired
	private SubscriptionPackageService subpkgService;

	/**
	 * Get single or all Library Sections.
	 * 
	 * @param id {@link LibrarySection} id
	 * @return {@link LibrarySection} or {@code null}
	 */
	@GetMapping("/section")
	public Object getLibrarySection(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "all", required = false, defaultValue = "false") boolean flag) {
		if (flag) // return all Library Sections
			return librarySectionService.getLibrarySection();
		if (id.length() != 3) // fail fast
			throw new IllegalRequestException("Invalid id : " + id);
		try {
			return librarySectionService.getLibrarySection(id); // return single Library Section
		} catch (InvalidBusinessCondition e) {
			throw new IllegalRequestException(e);
		}
	}

	/**
	 * Add new Library Section.
	 * 
	 * @param librarySection {@link LibrarySection}
	 * @return String message
	 */
	@PostMapping("/section")
	public String addLibrarySection(@Valid @RequestBody LibrarySection librarySection) {
		try {
			librarySectionService.addLibrarySection(librarySection);
			return "Library Section added successfully";
		} catch (InvalidBusinessCondition e) {
			throw new IllegalRequestException(e);
		}
	}

	/**
	 * Update existing Library Section.
	 * 
	 * @param librarySection {@link LibrarySection}
	 * @return String message
	 */
	@PutMapping("/section")
	public String updateLibrarySection(@Valid @RequestBody LibrarySection librarySection) {
		try {
			librarySectionService.updateLibrarySection(librarySection);
			return "Library Section updated successfully";
		} catch (InvalidBusinessCondition e) {
			throw new IllegalRequestException(e);
		}
	}

	/**
	 * Get single or all Subscription Packages.
	 * 
	 * Note : JSON Serialization of {@link SubscriptionPackage} is handled by
	 * {@link SubPkgJsonSerializer}
	 * 
	 * @param id {@link SubscriptionPackage} id
	 * @return String of {@link SubscriptionPackage} and array of
	 *         {@link LibrarySection} id and no. of Books
	 */
	@GetMapping("/package")
	public Object getSubscriptionPackage(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
			@RequestParam(value = "all", required = false, defaultValue = "false") boolean flag) {
		if (flag) // return all Subscription Packages
			return subpkgService.getSubscriptionPackage();
		try {
			return subpkgService.getSubscriptionPackage(id); // return single Subscription Packages
		} catch (InvalidBusinessCondition e) {
			throw new IllegalRequestException(e);
		}
	}

	/**
	 * Add new Subscription Package.
	 * 
	 * @param input {@link SubscriptionPackage} id and array of
	 *              {@link LibrarySection} id & no. of Books
	 * @return String message
	 */
	@PostMapping("/package")
	public String addSubscriptionPackage(@RequestBody String input) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(input);
			@Valid // read & validate node named package
			SubscriptionPackage pkg = mapper.treeToValue(jsonNode.get("package"), SubscriptionPackage.class);
			Map<String, Integer> map = new HashMap<>();
			ArrayNode arrayNode = (ArrayNode) jsonNode.withArray("sections"); // read array named sections
			for (JsonNode node : arrayNode) // put array elements in map
				map.put(node.get("sectionId").asText(), node.get("noOfBooks").asInt());
			int pkgId = subpkgService.addSubscriptionPackage(pkg, map);
			return "Package added successfully. Package Id : " + pkgId;
		} catch (JsonProcessingException | IllegalArgumentException | InvalidBusinessCondition
				| NullPointerException e) {
			throw new IllegalRequestException(e);
		}
	}

	/**
	 * Delete Subscription Package.
	 * 
	 * @param id {@link SubscriptionPackage} id
	 * @return String message
	 */
	@DeleteMapping("/package/{id}")
	public String deleteSubscriptionPackage(@PathVariable("id") int id) {
		try {
			subpkgService.deleteSubscriptionPackage(id);
			return "Subscription Package deleted successfully";
		} catch (InvalidBusinessCondition e) {
			throw new IllegalRequestException(e);
		}
	}

}
