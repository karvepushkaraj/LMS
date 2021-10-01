package com.app.lms.controller;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;

import org.hibernate.TransactionException;
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
import com.app.lms.model.PackageSection;
import com.app.lms.model.SubscriptionPackage;
import com.app.lms.service.LibrarySectionService;
import com.app.lms.service.SubscriptionPackageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Controller for Library Admin Operations.
 * 
 * @author karve
 *
 */

@RestController
@RequestMapping("api/v1/lms")
public class LibraryAdminController {

	@Autowired
	@Qualifier("LibraryAdminService")
	private LibrarySectionService librarySectionService;

	@Autowired
	@Qualifier("LibraryAdminService")
	private SubscriptionPackageService subpkgService;

	/**
	 * Get single Library Section.
	 * 
	 * @param id {@link LibrarySection} id
	 * @return {@link LibrarySection} or {@code null}
	 */
	@GetMapping("/section")
	public LibrarySection getLibrarySection(@RequestParam("id") String id) {
		if (id.length() != 3)
			throw new TransactionException("Invalid id : " + id);
		return librarySectionService.getLibrarySection(id);
	}

	/**
	 * Add new Library Section.
	 * 
	 * @param librarySection {@link LibrarySection}
	 */
	@PostMapping("/section")
	public void addLibrarySection(@Valid @RequestBody LibrarySection librarySection) {
		librarySectionService.addLibrarySection(librarySection);
	}

	/**
	 * Update existing Library Section.
	 * 
	 * @param librarySection {@link LibrarySection}
	 */
	@PutMapping("/section")
	public void updateLibrarySection(@Valid @RequestBody LibrarySection librarySection) {
		try {
			librarySectionService.updateLibrarySection(librarySection);
		} catch (NullPointerException e) {
			throw new TransactionException("Library Section does not exist");
		} catch (RuntimeException e) {
			throw new TransactionException("Invalid request");
		}
	}

	/**
	 * Delete Library Section.
	 * 
	 * @param id {@link LibrarySection} id
	 * @return String message
	 */
	@DeleteMapping("/section/{id}")
	public String deleteLibrarySection(@PathVariable("id") String id) {
		if (id.length() != 3)
			throw new TransactionException("Invalid id : " + id);
		try {
			boolean flag = librarySectionService.deleteLibrarySection(id);
			if (flag)
				return "Library Section deleted sucessfully";
		} catch (RuntimeException e) {
			throw new TransactionException("Library Section does not exist");
		}
		return "Transaction Failed";
	}

	/**
	 * Get single Subscription Package.
	 * 
	 * @param id {@link SubscriptionPackage} id
	 * @return {@link SubscriptionPackage} and array of {@link LibrarySection} id &
	 *         no. of Books
	 * @throws JsonProcessingException
	 */
	@GetMapping("/package")
	public String getSubscriptionPackage(@RequestParam("id") int id) {
		String result = null;
		ObjectMapper mapper = new ObjectMapper();
		SubscriptionPackage pkg = subpkgService.getSubscriptionPackage(id);
		try {
			ObjectNode objectNode = mapper.valueToTree(pkg);
			ArrayNode arrayNode = objectNode.putArray("sections");
			for (PackageSection ps : pkg.getPackageSection()) {
				ObjectNode node = mapper.createObjectNode();
				node.put("sectionId", ps.getSection().getSectionId());
				node.put("noOfBooks", ps.getNumberOfBooks());
				arrayNode.add(node);
			}
			result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
		} catch (JsonProcessingException | IllegalArgumentException | ClassCastException e) {
			throw new TransactionException("Package not found");
		}
		return result;
	}

	/**
	 * Add new Subscription Package.
	 * 
	 * @param input {@link SubscriptionPackage} id and array of
	 *              {@link LibrarySection} id & no. of Books
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@PostMapping("/package")
	public void addSubscriptionPackage(@RequestBody String input) {
		ObjectMapper mapper = new ObjectMapper();
		SubscriptionPackage pkg = null;
		Map<String, Integer> map = null;
		try {
			JsonNode jsonNode = mapper.readTree(input);
			pkg = mapper.treeToValue(jsonNode.get("package"), SubscriptionPackage.class);
			map = new HashMap<>();
			ArrayNode arrayNode = (ArrayNode) jsonNode.withArray("sections");
			for (JsonNode node : arrayNode) {
				map.put(node.get("sectionId").asText(), node.get("noOfBooks").asInt());
			}
			if (map.isEmpty())
				throw new NullPointerException();
			subpkgService.addSubscriptionPackage(pkg, map);
		} catch (JsonProcessingException e) {
			throw new TransactionException("Invalid Request");
		} catch (RuntimeException e) {
			throw new TransactionException("Invalid package or section");
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
		boolean flag = false;
		try {
			flag = subpkgService.deleteSubscriptionPackage(id);
		} catch (RuntimeException e) {
			throw new TransactionException("Subscription Package does not exist");
		}
		if (flag)
			return "Subscription Package deleted sucessfully";
		return "Transaction Failed";
	}
}
