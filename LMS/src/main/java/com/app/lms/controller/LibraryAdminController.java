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

@RestController
@RequestMapping("api/v1/lms")
public class LibraryAdminController {

	@Autowired
	@Qualifier("LibraryAdminService")
	private LibrarySectionService librarySectionService;

	@Autowired
	@Qualifier("LibraryAdminService")
	private SubscriptionPackageService subpkgService;

	@GetMapping("/section")
	public LibrarySection getLibrarySection(@RequestParam("id") String id) {
		if (id.length() != 3)
			throw new TransactionException("Invalid id : " + id);
		return librarySectionService.getLibrarySection(id);
	}

	@PostMapping("/section")
	public void addLibrarySection(@Valid @RequestBody LibrarySection librarySection) {
		librarySectionService.addLibrarySection(librarySection);
	}

	@PutMapping("/section")
	public void updateLibrarySection(@Valid @RequestBody LibrarySection librarySection) {
		librarySectionService.updateLibrarySection(librarySection);
	}

	@DeleteMapping("/section/{id}")
	public String deleteLibrarySection(@PathVariable("id") String id) {
		if (id.length() != 3)
			throw new TransactionException("Invalid id : " + id);
		boolean flag = librarySectionService.deleteLibrarySection(id);
		if (flag)
			return "Library Section deleted sucessfully";
		return "Transaction Failed";
	}

	@PostMapping("/package")
	public void addSubscriptionPackage(@RequestBody String input) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(input);
		SubscriptionPackage pkg = mapper.treeToValue(jsonNode.get("package"), SubscriptionPackage.class);
		Map<String, Integer> map = new HashMap<>();
		ArrayNode arrayNode = (ArrayNode) jsonNode.withArray("sections");
		for (JsonNode node : arrayNode) {
			map.put(node.get("sectionId").asText(), node.get("noOfBooks").asInt());
		}
		if (pkg == null || map.isEmpty())
			throw new TransactionException("Invalid Request");
		subpkgService.addSubscriptionPackage(pkg, map);
	}

	@GetMapping("/package")
	public String getSubscriptionPackage(@RequestParam("id") int id) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		SubscriptionPackage pkg = subpkgService.getSubscriptionPackage(id);
		if (pkg == null)
			throw new TransactionException("Package not found");
		ObjectNode objectNode = mapper.valueToTree(pkg);
		ArrayNode arrayNode = objectNode.putArray("sections");
		for (PackageSection ps : pkg.getPackageSection()) {
			ObjectNode node = mapper.createObjectNode();
			node.put("sectionId", ps.getSection().getSectionId());
			node.put("noOfBooks", ps.getNumberOfBooks());
			arrayNode.add(node);
		}
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
	}

	@DeleteMapping("/package/{id}")
	public String deleteSubscriptionPackage(@PathVariable("id") int id) {
		boolean flag = subpkgService.deleteSubscriptionPackage(id);
		if (flag)
			return "Subscription Package deleted sucessfully";
		return "Transaction Failed";
	}
}
