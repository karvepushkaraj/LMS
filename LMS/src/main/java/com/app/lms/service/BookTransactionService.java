package com.app.lms.service;

/**
 * Service interface for Book transactions.
 * 
 * @author karve
 *
 */
public interface BookTransactionService {

	int issueBook(String bookid, int memberid);

	int returnBook(String bookid, int memberid);

}
