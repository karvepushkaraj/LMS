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
import com.app.lms.model.LibrarySection;
import com.app.lms.model.Member;
import com.app.lms.model.SubscriptionPackage;
import com.app.lms.service.BookService;
import com.app.lms.service.BookTransactionService;
import com.app.lms.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Controller for Library Management Operations.
 * 
 * @author karve
 *
 */

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

	/**
	 * Add Book with new Book Title.
	 * 
	 * @param input {@link LibrarySection} id, {@link BookTitle} and
	 *              {@link BookCopy}
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
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

	/**
	 * Get single Book Title if id length is 4 or Book Copy if id length is 5.
	 * 
	 * @param bookId {@link BookTitle} id or {@link BookCopy} id
	 * @return {@link BookTitle} or {@link BookCopy}
	 * @throws JsonProcessingException
	 */
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
			ObjectNode node = mapper.valueToTree(bc);
			node.put("sectionId", bc.getTitle().getSection().getSectionId());
			int memberId = bc.getMember() == null ? 0 : bc.getMember().getMemberId();
			node.put("member", memberId);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
		}
		throw new TransactionException("Invalid Request Parameter");
	}

	/**
	 * Delete BookCopy. Also deletes Book Title if there are no Book Copies
	 * available.
	 * 
	 * @param bookId {@link BookCopy} id
	 * @return String message
	 */
	@DeleteMapping("/books/{id}")
	public String deleteBook(@PathVariable("id") String bookId) {
		if (bookId.length() != 5)
			throw new TransactionException("Invalid book Id");
		boolean flag = bookService.deleteBook(bookId);
		if (flag)
			return "Book deleted sucessfully";
		return "Transaction Failed";
	}

	/**
	 * Get Member.
	 * 
	 * @param memberId {@link Member} id
	 * @return Member {@link Member}
	 * @throws JsonProcessingException
	 */
	@GetMapping("/member")
	public String getMember(@RequestParam("id") int memberId) throws JsonProcessingException {
		if (memberId < 10000)
			throw new TransactionException("Invalid Request Parameter");
		Member member = memberService.getMember(memberId);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.valueToTree(member);
		ArrayNode arrayNode = node.putArray("book");
		member.getBook().stream().forEach(bc -> arrayNode.add("" + bc.getTitle().getTitleId() + bc.getCopyId()));
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
	}

	/**
	 * Add new Member.
	 * 
	 * @param member {@link Member}
	 */
	@PostMapping("/member")
	public void addMember(@Valid @RequestBody Member member) {
		memberService.addMember(member);
	}

	/**
	 * Update existing Member details.
	 * 
	 * @param member {@link Member}
	 */
	@PutMapping("/member")
	public void updateMember(@Valid @RequestBody Member member) {
		memberService.updateMember(member);
	}

	/**
	 * Delete Member.
	 * 
	 * @param memberId {@link Member} id
	 * @return String message
	 */
	@DeleteMapping("/member/{id}")
	public String deleteMember(@PathVariable("id") int memberId) {
		if (memberId < 10000)
			throw new TransactionException("Invalid Request Parameter");
		boolean flag = memberService.deleteMember(memberId);
		if (flag)
			return "Member deleted sucessfully";
		return "Transaction Failed";
	}

	/**
	 * Add new Subscription of a Member to Subscription Package.
	 * 
	 * @param input {@link Member} id and {@link SubscriptionPackage} id
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@PostMapping("/subscribe")
	public void addSubscription(@RequestBody String input) throws JsonMappingException, JsonProcessingException {
		JsonNode node = new ObjectMapper().readTree(input);
		memberService.addSubscription(node.get("memberId").asInt(), node.get("packageId").asInt());
	}

	/**
	 * Issue Book Copy to Member.
	 * 
	 * @param input {@link Member} id and {@link BookCopy} id
	 * @return String message
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@PostMapping("/issue")
	public String issueBook(@RequestBody String input) throws JsonMappingException, JsonProcessingException {
		JsonNode node = new ObjectMapper().readTree(input);
		int response = bookTransactionService.issueBook(node.get("bookid").asText(), node.get("memberid").asInt());
		if (response > 0)
			return "Transaction Sucessful. Transaction Id : " + response;
		return "Transaction Failed";
	}

	/**
	 * Return issued Book Copy from Member.
	 * 
	 * @param input {@link Member} id and {@link BookCopy} id
	 * @return String message
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@PostMapping("/return")
	public String returnBook(@RequestBody String input) throws JsonMappingException, JsonProcessingException {
		JsonNode node = new ObjectMapper().readTree(input);
		boolean flag = bookTransactionService.returnBook(node.get("bookid").asText(), node.get("memberid").asInt());
		if (flag)
			return "Transaction Sucessful";
		return "Transaction Failed";
	}

}
