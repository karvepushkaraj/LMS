package com.app.lms.service;

import com.app.lms.model.LateFee;
import com.app.lms.util.InvalidBusinessCondition;

/**
 * Service interface for Book transactions.
 * 
 * @author karve
 *
 */
public interface BookTransactionService {

	int issueBook(String bookid, int memberid) throws InvalidBusinessCondition;

	int returnBook(String bookid, int memberid, LateFee fee) throws InvalidBusinessCondition;

}
