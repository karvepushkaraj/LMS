package com.app.lms.service;

public interface BookTransactionService {

	int issueBook(String bookid, int memberid);

	boolean returnBook(String bookid, int memberid);

}
