package com.app.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.lms.model.BookCopy;
import com.app.lms.model.BookTitle;
import com.app.lms.model.Member;
import com.app.lms.service.BookService;
import com.app.lms.service.BookTransactionService;
import com.app.lms.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("api/v1/lms")
public class LibraryManagementController {

	@Autowired
	@Qualifier("LibraryManagementService")
	private BookService bookService;
	
	@Autowired
	@Qualifier("LibraryManagementService")
	private MemberService memberService;
	
	@Autowired
	@Qualifier("LibraryManagementService")
	private BookTransactionService bookTransactionService;
	
	@PostMapping("/books")
	public void addBook(@RequestBody String input) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(input);
		String sectionId = jsonNode.get("sectionId").asText();
		BookTitle bt = mapper.treeToValue(jsonNode.get("BookTitle"), BookTitle.class);
		BookCopy bc = mapper.treeToValue(jsonNode.get("BookCopy"), BookCopy.class);
		bookService.addBook(sectionId, bt, bc);
	}
	
	@GetMapping("/books")
	public String getBook(@RequestParam("id") String bookId) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		BookTitle bt = null;
		BookCopy bc = null;
		if(bookId.length()==4) {
			bt = bookService.getBookTitle(bookId);
			ObjectNode objectNode = mapper.valueToTree(bt);
			ArrayNode arrayNode = objectNode.putArray("bookCopies");
			for(BookCopy bookCopy : bt.getBookCopies()) {
				ObjectNode node = mapper.createObjectNode();
				node.put("copyId", bookCopy.getCopyId());
				node.put("price", bookCopy.getPrice());
				arrayNode.add(node);
			}
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
		}
		else if(bookId.length()==5) {
			bc = bookService.getBookCopy(bookId);
			return mapper.writeValueAsString(bc);
		}
		return null;
	}
	
	@DeleteMapping("/books/{id}")
	public void deleteBook(@PathVariable("id") String bookId) {
		bookService.deleteBook(bookId);
	}
	
	@GetMapping("/member")
	public Member getMember(@RequestParam("id") int memberId) {
		return memberService.getMember(memberId);
	}
	
	@PostMapping("/member")
	public void addMember(@RequestBody Member member) {
		memberService.addMember(member);
	}
	
	@DeleteMapping("/member/{id}")
	public void deleteMember(@PathVariable("id") int memberId) {
		memberService.deleteMember(memberId);
	}
	
	@PostMapping("/subscribe")
	public void addSubscription(@RequestBody String input) throws JsonMappingException, JsonProcessingException {
		JsonNode node = new ObjectMapper().readTree(input);
		memberService.addSubscription(node.get("memberId").asInt(), node.get("packageId").asInt());
	}
	
	@PostMapping("/issue")
	public void issueBook(@RequestBody String input) throws JsonMappingException, JsonProcessingException {
		JsonNode node = new ObjectMapper().readTree(input);
		bookTransactionService.issueBook(node.get("bookid").asText(), node.get("memberid").asInt());
	}
	
}
