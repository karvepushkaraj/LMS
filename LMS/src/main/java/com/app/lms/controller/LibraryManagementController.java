package com.app.lms.controller;

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

import com.app.lms.model.BookCopy;
import com.app.lms.model.BookTitle;
import com.app.lms.model.Deposite;
import com.app.lms.model.LateFee;
import com.app.lms.model.LibrarySection;
import com.app.lms.model.Member;
import com.app.lms.model.SubscriptionFee;
import com.app.lms.model.SubscriptionPackage;
import com.app.lms.service.BookService;
import com.app.lms.service.BookTransactionService;
import com.app.lms.service.MemberService;
import com.app.lms.util.IllegalRequestException;
import com.app.lms.util.InvalidBusinessCondition;
import com.app.lms.util.LMSExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Controller for Library Management Operations. Note : Exceptions of this class
 * are handled by {@link LMSExceptionHandler}
 * 
 * @author karve
 *
 */

@RestController
@RequestMapping("api/v1/lms")
public class LibraryManagementController {

	private final BookService bookService;

	private final MemberService memberService;

	private final BookTransactionService bookTransactionService;

	@Autowired
	public LibraryManagementController(BookService bookService, MemberService memberService,
			BookTransactionService bookTransactionService) {
		this.bookService = bookService;
		this.memberService = memberService;
		this.bookTransactionService = bookTransactionService;
	}

	/**
	 * Get single Book Title if id length is 4 or Book Copy if id length is 5.
	 * 
	 * @param bookId {@link BookTitle} id or {@link BookCopy} id
	 * @return String of {@link BookTitle} or {@link BookCopy}
	 */
	@GetMapping("/books")
	public String getBook(@RequestParam("id") String bookId) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (bookId.length() == 4) { // return BookTitle if bookId length is 4
				BookTitle bookTitle = bookService.getBookTitle(Integer.parseInt(bookId));
				ObjectNode objectNode = mapper.valueToTree(bookTitle); // add BookTitle
				ArrayNode arrayNode = objectNode.putArray("bookCopies"); // create array named bookCopies
				for (BookCopy bookCopy : bookTitle.getBookCopies()) {
					ObjectNode node = mapper.createObjectNode();
					node.put("copyId", bookCopy.getCopyId());
					node.put("price", bookCopy.getPrice());
					arrayNode.add(node); // add BookCopy to array
				}
				return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
			} else if (bookId.length() == 5) { // return BookCopy if bookId length is 5
				BookCopy bookCopy = bookService.getBookCopy(bookId);
				ObjectNode objectNode = mapper.valueToTree(bookCopy); // add BookCopy
				objectNode.put("sectionId", bookCopy.getTitle().getSection().getSectionId()); // add sectionId
				Member member = bookCopy.getMember();
				// put memberId or 0 if member does not exist
				objectNode.put("member", member == null ? 0 : member.getMemberId());
				return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
			} else
				throw new IllegalRequestException("Invalid book Id");
		} catch (JsonProcessingException | IllegalArgumentException | InvalidBusinessCondition e) {
			throw new IllegalRequestException(e.getMessage(), e);
		}
	}

	/**
	 * Add Book with new Book Title.
	 * 
	 * @param input {@link LibrarySection} id, {@link BookTitle} and
	 *              {@link BookCopy}
	 * @return String message
	 */
	@PostMapping("/books")
	public String addBook(@RequestBody String input) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(input);
			String sectionId = jsonNode.get("sectionId").asText(); // read sectionId
			BookTitle bookTitle = mapper.treeToValue(jsonNode.get("BookTitle"), BookTitle.class); // read BookTitle
			BookCopy bookCopy = mapper.treeToValue(jsonNode.get("BookCopy"), BookCopy.class); // read BookCopy
			String bookId = bookService.addBook(sectionId, bookTitle, bookCopy);
			return "Book added successfully. Book Id : " + bookId;
		} catch (JsonProcessingException | IllegalArgumentException | NullPointerException
				| InvalidBusinessCondition e) {
			throw new IllegalRequestException(e.getMessage(), e);
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
		if (bookId.length() != 5) // fail fast
			throw new IllegalRequestException("Invalid book Id");
		try {
			bookService.deleteBook(bookId);
			return "Book deleted successfully";
		} catch (InvalidBusinessCondition e) {
			throw new IllegalRequestException(e.getMessage(), e);
		}
	}

	/**
	 * Get Member.
	 * 
	 * @param memberId {@link Member} id
	 * @return String of {@link Member}
	 */
	@GetMapping("/member")
	public String getMember(@RequestParam("id") int memberId) {
		if (memberId < 10000) // fail fast
			throw new IllegalRequestException("Invalid Request Parameter");
		try {
			Member member = memberService.getMember(memberId);
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode node = mapper.valueToTree(member); // add member
			ArrayNode arrayNode = node.putArray("book"); // create array named book
			// add bookIds of books subscribed by the member to array
			member.getBook().stream().forEach(bc -> arrayNode.add("" + bc.getTitle().getTitleId() + bc.getCopyId()));
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
		} catch (JsonProcessingException | IllegalArgumentException | InvalidBusinessCondition e) {
			throw new IllegalRequestException(e.getMessage(), e);
		}
	}

	/**
	 * Add new Member.
	 * 
	 * @param member {@link Member}
	 * @return String message
	 */
	@PostMapping("/member")
	public String addMember(@RequestBody String input) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode node = mapper.readTree(input);
			Member member = mapper.treeToValue(node.get("member"), Member.class); // read member
			Deposite deposite = mapper.treeToValue(node.get("deposite"), Deposite.class); // read deposite
			int memberId = memberService.addMember(member, deposite);
			return "Member added successfully. Member Id : " + memberId;
		} catch (JsonProcessingException | IllegalArgumentException | NullPointerException
				| InvalidBusinessCondition e) {
			throw new IllegalRequestException(e.getMessage(), e);
		}
	}

	/**
	 * Update existing Member details.
	 * 
	 * @param member {@link Member}
	 * @return String message
	 */
	@PutMapping("/member")
	public String updateMember(@Valid @RequestBody Member member) {
		try {
			memberService.updateMember(member);
			return "Member updated successfully";
		} catch (InvalidBusinessCondition e) {
			throw new IllegalRequestException(e.getMessage(), e);
		}
	}

	/**
	 * Delete Member.
	 * 
	 * @param memberId {@link Member} id
	 * @return String message
	 */
	@DeleteMapping("/member")
	public String deleteMember(@RequestBody String input) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode jsonNode = mapper.readTree(input);
			int memberId = jsonNode.get("memberId").asInt(); // read memberId
			Deposite deposite = mapper.treeToValue(jsonNode.get("deposite"), Deposite.class); // read deposite
			memberService.deleteMember(memberId, deposite);
			return "Member deleted successfully";
		} catch (JsonProcessingException | IllegalArgumentException | NullPointerException
				| InvalidBusinessCondition e) {
			throw new IllegalRequestException(e.getMessage(), e);
		}
	}

	/**
	 * Add new Subscription of a Member to Subscription Package.
	 * 
	 * @param input {@link Member} id and {@link SubscriptionPackage} id
	 * @return String message
	 */
	@PostMapping("/subscribe")
	public String addSubscription(@RequestBody String input) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode node = mapper.readTree(input);
			int memberId = node.get("memberId").asInt(); // read memberId
			int packageId = node.get("packageId").asInt(); // read packageId
			// read SubscriptionFee
			SubscriptionFee fee = mapper.treeToValue(node.get("subscriptionFee"), SubscriptionFee.class);
			int subId = memberService.addSubscription(memberId, packageId, fee);
			return "Subscription added successfully. Subscription Id : " + subId;
		} catch (JsonProcessingException | IllegalArgumentException | NullPointerException
				| InvalidBusinessCondition e) {
			throw new IllegalRequestException(e.getMessage(), e);
		}
	}

	/**
	 * Issue Book Copy to Member.
	 * 
	 * @param input {@link Member} id and {@link BookCopy} id
	 * @return String message
	 */
	@PostMapping("/issue")
	public String issueBook(@RequestBody String input) {
		try {
			JsonNode node = new ObjectMapper().readTree(input);
			String bookId = node.get("bookid").asText(); // read bookId
			int memberId = node.get("memberid").asInt(); // read memberId
			int response = bookTransactionService.issueBook(bookId, memberId);
			return "Transaction Successful. Transaction Id : " + response;
		} catch (JsonProcessingException | NullPointerException | InvalidBusinessCondition e) {
			throw new IllegalRequestException(e.getMessage(), e);
		}
	}

	/**
	 * Return issued Book Copy from Member.
	 * 
	 * @param input {@link Member} id and {@link BookCopy} id
	 * @return String message
	 */
	@PostMapping("/return")
	public String returnBook(@RequestBody String input) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode node = mapper.readTree(input);
			String bookId = node.get("bookid").asText(); // read bookId
			int memberId = node.get("memberid").asInt(); // read memberId
			LateFee fee = mapper.treeToValue(node.get("lateFee"), LateFee.class); // read LateFee
			int response = bookTransactionService.returnBook(bookId, memberId, fee);
			return "Transaction Successful. Transaction Id : " + response;
		} catch (JsonProcessingException | IllegalArgumentException | NullPointerException
				| InvalidBusinessCondition e) {
			throw new IllegalRequestException(e.getMessage(), e);
		}
	}

}
