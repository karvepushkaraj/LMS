package com.app.lms.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.lms.dao.BasicDao;
import com.app.lms.model.BookCopy;
import com.app.lms.model.BookTitle;
import com.app.lms.model.CopyId;
import com.app.lms.model.LibrarySection;
import com.app.lms.model.Member;

@Service("LibraryManagementService")
//@Scope("prototype")
@Transactional
public class LibraryManagementServiceImpl implements BookService, MemberService {
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<BookTitle, Integer> basicDaoBookTitle;
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<BookCopy, CopyId> basicDaoBookCopy;
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<Member, Integer> basicDaoMember;
	
	@Autowired
	@Qualifier("LibraryAdminService")
	private LibrarySectionService librarySectionService;
	
	@PostConstruct
	public void setClazz() {
		basicDaoBookTitle.setClazz(BookTitle.class);
		basicDaoBookCopy.setClazz(BookCopy.class);
		basicDaoMember.setClazz(Member.class);
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

	@Override
	public void deleteBook(String bookId) {
		BookTitle bt = getBookTitle(bookId.substring(0, 4));
		BookCopy bc = null;
		if(bt!=null) {
			CopyId key = new CopyId(bt, Integer.valueOf(bookId.substring(4)));
			bc = basicDaoBookCopy.getById(key);
			basicDaoBookCopy.delete(bc);
		}
		if(bt.getBookCopies().isEmpty()) {
			basicDaoBookTitle.delete(bt);
		}
	}
	
	@Override
	public Member getMember(int memberId) {
		return basicDaoMember.getById(memberId);
	}

	@Override
	public void addMember(Member member) {
		basicDaoMember.add(member);
	}

//	@Override
//	public void updateMember(Member member) {
//		// TODO Auto-generated method stub
//		Member m1 = basicDao.getById(member.getMemberId());
//		if(m1!=null)
//			m1
//	}

	@Override
	public void deleteMember(int memberId) {
		Member m1 = basicDaoMember.getById(memberId);
		if(m1!=null)
			basicDaoMember.delete(m1);
	}

}
