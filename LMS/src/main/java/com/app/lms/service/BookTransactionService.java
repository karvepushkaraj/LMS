package com.app.lms.service;

public interface BookTransactionService {

	void issueBook(String bookid, int memberid);

	void returnBook(String bookid, int memberid);

}
