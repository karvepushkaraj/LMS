package com.app.lms.controller;

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
		if (sectionId == null || bt == null || bc == null)
			throw new TransactionException("Invalid request");
		bookService.addBook(sectionId, bt, bc);
	}

	@GetMapping("/books")
	public String getBook(@RequestParam("id") String bookId) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		BookTitle bt = null;
		BookCopy bc = null;
		if (bookId.length() == 4) {
			bt = bookService.getBookTitle(Integer.parseInt(bookId));
			if (bt == null)
				throw new TransactionException("Book Not Found");
			ObjectNode objectNode = mapper.valueToTree(bt);
			ArrayNode arrayNode = objectNode.putArray("bookCopies");
			for (BookCopy bookCopy : bt.getBookCopies()) {
				ObjectNode node = mapper.createObjectNode();
				node.put("copyId", bookCopy.getCopyId());
				node.put("price", bookCopy.getPrice());
				arrayNode.add(node);
			}
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
		} else if (bookId.length() == 5) {
			bc = bookService.getBookCopy(bookId);
			if (bc == null)
				throw new TransactionException("Book Not Found");
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bc);
		}
		throw new TransactionException("Invalid Request Parameter");
	}

	@DeleteMapping("/books/{id}")
	public String deleteBook(@PathVariable("id") String bookId) {
		if (bookId.length() != 5)
			throw new TransactionException("Invalid book Id");
		boolean flag = bookService.deleteBook(bookId);
		if (flag)
			return "Book deleted sucessfully";
		return "Transaction Failed";
	}

	@GetMapping("/member")
	public Member getMember(@RequestParam("id") int memberId) {
		if (memberId < 10000)
			throw new TransactionException("Invalid Request Parameter");
		return memberService.getMember(memberId);
	}

	@PostMapping("/member")
	public void addMember(@Valid @RequestBody Member member) {
		memberService.addMember(member);
	}

	@PutMapping("/member")
	public void updateMember(@Valid @RequestBody Member member) {
		memberService.updateMember(member);
	}

	@DeleteMapping("/member/{id}")
	public String deleteMember(@PathVariable("id") int memberId) {
		if (memberId < 10000)
			throw new TransactionException("Invalid Request Parameter");
		boolean flag = memberService.deleteMember(memberId);
		if (flag)
			return "Member deleted sucessfully";
		return "Transaction Failed";
	}

	@PostMapping("/subscribe")
	public void addSubscription(@RequestBody String input) throws JsonMappingException, JsonProcessingException {
		JsonNode node = new ObjectMapper().readTree(input);
		memberService.addSubscription(node.get("memberId").asInt(), node.get("packageId").asInt());
	}

	@PostMapping("/issue")
	public String issueBook(@RequestBody String input) throws JsonMappingException, JsonProcessingException {
		JsonNode node = new ObjectMapper().readTree(input);
		int response = bookTransactionService.issueBook(node.get("bookid").asText(), node.get("memberid").asInt());
		if (response > 0)
			return "Transaction Sucessful. Transaction Id : " + response;
		return "Transaction Failed";
	}

	@PostMapping("/return")
	public String returnBook(@RequestBody String input) throws JsonMappingException, JsonProcessingException {
		JsonNode node = new ObjectMapper().readTree(input);
		boolean flag = bookTransactionService.returnBook(node.get("bookid").asText(), node.get("memberid").asInt());
		if (flag)
			return "Transaction Sucessful";
		return "Transaction Failed";
	}

}
