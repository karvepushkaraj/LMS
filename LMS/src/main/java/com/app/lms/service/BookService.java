package com.app.lms.service;

import com.app.lms.model.BookCopy;
import com.app.lms.model.BookTitle;
import com.app.lms.util.InvalidBusinessCondition;

/**
 * Service interface for all operations related to Books.
 * 
 * @author karve
 *
 */

public interface BookService {

	BookTitle getBookTitle(int titleId) throws InvalidBusinessCondition;

	BookCopy getBookCopy(String copyId) throws InvalidBusinessCondition;

	void addBook(String sectionId, BookTitle bookTitle, BookCopy bookCopy) throws InvalidBusinessCondition;

	void addBookCopy(int titleId, BookCopy bookCopy) throws InvalidBusinessCondition;

	void deleteBook(String bookId) throws InvalidBusinessCondition;

}
