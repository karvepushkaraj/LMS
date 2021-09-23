package com.app.lms.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.lms.dao.BasicDao;
import com.app.lms.model.BookCopy;
import com.app.lms.model.BookTitle;
import com.app.lms.model.CopyId;
import com.app.lms.model.LibrarySection;

@Service("BookService")
@Transactional
public class BookServiceImpl implements BookService {
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<BookTitle, Integer> basicDaoBookTitle;
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<BookCopy, CopyId> basicDaoBookCopy;
	
	@Autowired
	@Qualifier("LibrarySectionService")
	private LibrarySectionService librarySectionService;
	
	@PostConstruct
	public void setClazz() {
		basicDaoBookTitle.setClazz(BookTitle.class);
		basicDaoBookCopy.setClazz(BookCopy.class);
	}

	@Override
	public void addBook(String sectionId, BookTitle bookTitle, BookCopy bookCopy) {
		LibrarySection librarySection = librarySectionService.getLibrarySection(sectionId);
		if(librarySection!=null) {
			bookTitle.setSection(librarySection);
			basicDaoBookTitle.add(bookTitle);
			bookCopy.setTitle(bookTitle);
			basicDaoBookCopy.add(bookCopy);
		}
	}

	@Override
	public BookTitle getBookTitle(String titleId) {
		return basicDaoBookTitle.getById(Integer.valueOf(titleId));
	}

	@Override
	public BookCopy getBookCopy(String copyId) {
		BookTitle bt = getBookTitle(copyId.substring(0, 4));
		BookCopy bc = null;
		if(bt!=null) {
			CopyId key = new CopyId(bt, Integer.valueOf(copyId.substring(4)));
			bc = basicDaoBookCopy.getById(key);
		}
		return bc;
	}

}
