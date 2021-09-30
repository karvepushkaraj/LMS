package com.app.lms.service;

import com.app.lms.model.BookCopy;
import com.app.lms.model.BookTitle;

public interface BookService {

	void addBook(String sectionId, BookTitle bookTitle, BookCopy bookCopy);

	void addBookCopy(int titleId, BookCopy bookCopy);

	BookTitle getBookTitle(int titleId);

	BookCopy getBookCopy(String copyId);

	boolean deleteBook(String bookId);

}
