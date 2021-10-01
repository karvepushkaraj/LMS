package com.app.lms.controller;

import java.io.IOException;

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
	 * Get single Book Title if id length is 4 or Book Copy if id length is 5.
	 * 
	 * @param bookId {@link BookTitle} id or {@link BookCopy} id
	 * @return {@link BookTitle} or {@link BookCopy}
	 * @throws JsonProcessingException
	 */
	@GetMapping("/books")
	public String getBook(@RequestParam("id") String bookId) {
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		BookTitle bt = null;
		BookCopy bc = null;
		if (bookId.length() == 4) {
			try {
				bt = bookService.getBookTitle(Integer.parseInt(bookId));
				ObjectNode objectNode = mapper.valueToTree(bt);
				ArrayNode arrayNode = objectNode.putArray("bookCopies");
				for (BookCopy bookCopy : bt.getBookCopies()) {
					ObjectNode node = mapper.createObjectNode();
					node.put("copyId", bookCopy.getCopyId());
					node.put("price", bookCopy.getPrice());
					arrayNode.add(node);
				}
				result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
			} catch (RuntimeException | JsonProcessingException e) {
				throw new TransactionException("Book Not Found");
			}
		} else if (bookId.length() == 5) {
			try {
				bc = bookService.getBookCopy(bookId);
				ObjectNode node = mapper.valueToTree(bc);
				node.put("sectionId", bc.getTitle().getSection().getSectionId());
				int memberId = bc.getMember() == null ? 0 : bc.getMember().getMemberId();
				node.put("member", memberId);
				result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
			} catch (RuntimeException | JsonProcessingException e) {
				throw new TransactionException("Book Not Found");
			}
		}
		return result;
	}

	/**
	 * Add Book with new Book Title.
	 * 
	 * @param input {@link LibrarySection} id, {@link BookTitle} and
	 *              {@link BookCopy}
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@PostMapping("/books")
	public void addBook(@RequestBody String input) {
		String sectionId = null;
		BookTitle bt = null;
		BookCopy bc = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(input);
			sectionId = jsonNode.get("sectionId").asText();
			bt = mapper.treeToValue(jsonNode.get("BookTitle"), BookTitle.class);
			bc = mapper.treeToValue(jsonNode.get("BookCopy"), BookCopy.class);
			bookService.addBook(sectionId, bt, bc);
		} catch (JsonProcessingException | IllegalArgumentException e) {
			throw new TransactionException("Invalid request");
		} catch (NullPointerException e) {
			throw new TransactionException("Invalid section id, Book Title or Book Copy");
		}
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
		try {
			boolean flag = bookService.deleteBook(bookId);
			if (flag)
				return "Book deleted sucessfully";
		} catch (RuntimeException e) {
			throw new TransactionException("Book does not exist");
		}
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
	public String getMember(@RequestParam("id") int memberId) {
		if (memberId < 10000)
			throw new TransactionException("Invalid Request Parameter");
		String result = null;
		Member member = memberService.getMember(memberId);
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode node = mapper.valueToTree(member);
			ArrayNode arrayNode = node.putArray("book");
			member.getBook().stream().forEach(bc -> arrayNode.add("" + bc.getTitle().getTitleId() + bc.getCopyId()));
			result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
		} catch (JsonProcessingException | IllegalArgumentException | ClassCastException e) {
			throw new TransactionException("Member not found");
		}
		return result;
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
		try {
			memberService.updateMember(member);
		} catch (NullPointerException e) {
			throw new TransactionException("Member does not exist");
		}
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
		try {
			boolean flag = memberService.deleteMember(memberId);
			if (flag)
				return "Member deleted sucessfully";
		} catch (RuntimeException e) {
			throw new TransactionException("Member does not exist");
		}
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
	public void addSubscription(@RequestBody String input) {
		try {
			JsonNode node = new ObjectMapper().readTree(input);
			memberService.addSubscription(node.get("memberId").asInt(), node.get("packageId").asInt());
		} catch (JsonProcessingException e) {
			throw new TransactionException("Invalid request");
		} catch (NullPointerException e) {
			throw new TransactionException("Invalid memberId or packageId");
		}
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
	public String issueBook(@RequestBody String input) {
		int response;
		try {
			JsonNode node = new ObjectMapper().readTree(input);
			response = bookTransactionService.issueBook(node.get("bookid").asText(), node.get("memberid").asInt());
		} catch (JsonProcessingException e) {
			throw new TransactionException("Invalid request");
		} catch (NullPointerException e) {
			throw new TransactionException("Member or Book is not free");
		}
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
	public String returnBook(@RequestBody String input) {
		try {
			JsonNode node = new ObjectMapper().readTree(input);
			int response = bookTransactionService.returnBook(node.get("bookid").asText(), node.get("memberid").asInt());
				return "Transaction Sucessful. Transaction Id : " + response;
		} catch (JsonProcessingException e) {
			throw new TransactionException("Exception occured");
		}
	}

}
