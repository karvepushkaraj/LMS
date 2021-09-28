package com.app.lms.service;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.lms.dao.AuxiliaryDao;
import com.app.lms.dao.BasicDao;
import com.app.lms.model.BookCopy;
import com.app.lms.model.BookTitle;
import com.app.lms.model.BookTransaction;
import com.app.lms.model.CopyId;
import com.app.lms.model.LibrarySection;
import com.app.lms.model.Member;
import com.app.lms.model.Subscription;
import com.app.lms.model.SubscriptionPackage;
import com.app.lms.model.TransactionStatus;

@Service("LibraryManagementService")
@Transactional
public class LibraryManagementService implements BookService, MemberService, BookTransactionService {
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<BookTitle, Integer> bookTitleDao;
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<BookCopy, CopyId> bookCopyDao;
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<Member, Integer> memberDao;
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<Subscription, Integer> subscriptionDao;
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<BookTransaction, Integer> bookTransactionDao;
	
	@Autowired
	@Qualifier("AuxiliaryDao")
	private AuxiliaryDao auxiliaryDao;
	
	@Autowired
	@Qualifier("LibraryAdminService")
	private LibrarySectionService librarySectionService;
	
	@Autowired
	@Qualifier("LibraryAdminService")
	private SubscriptionPackageService subpkgService;
	
	@PostConstruct
	public void setClazz() {
		bookTitleDao.setClazz(BookTitle.class);
		bookCopyDao.setClazz(BookCopy.class);
		memberDao.setClazz(Member.class);
		subscriptionDao.setClazz(Subscription.class);
		bookTransactionDao.setClazz(BookTransaction.class);
	}

	@Override
	public void addBook(String sectionId, BookTitle bookTitle, BookCopy bookCopy) {
		LibrarySection librarySection = librarySectionService.getLibrarySection(sectionId);
		if(librarySection!=null) {
			bookTitle.setSection(librarySection);
			bookTitleDao.add(bookTitle);
			bookCopy.setTitle(bookTitle);
			bookCopyDao.add(bookCopy);
		}
	}

	@Override
	public BookTitle getBookTitle(String titleId) {
		return bookTitleDao.getById(Integer.valueOf(titleId));
	}

	@Override
	public BookCopy getBookCopy(String copyId) {
		BookTitle bt = getBookTitle(copyId.substring(0, 4));
		BookCopy bc = null;
		if(bt!=null) {
			CopyId key = new CopyId(bt, Integer.valueOf(copyId.substring(4)));
			bc = bookCopyDao.getById(key);
		}
		return bc;
	}

	@Override
	public void deleteBook(String bookId) {
		BookTitle bt = getBookTitle(bookId.substring(0, 4));
		BookCopy bc = null;
		if(bt!=null) {
			CopyId key = new CopyId(bt, Integer.valueOf(bookId.substring(4)));
			bc = bookCopyDao.getById(key);
			bookCopyDao.delete(bc);
		}
		if(bt.getBookCopies().isEmpty()) {
			bookTitleDao.delete(bt);
		}
	}
	
	@Override
	public Member getMember(int memberId) {
		return memberDao.getById(memberId);
	}

	@Override
	public void addMember(Member member) {
		memberDao.add(member);
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
		Member m1 = memberDao.getById(memberId);
		if(m1!=null)
			memberDao.delete(m1);
	}

	@Override
	public void addSubscription(int memberId, int pkgId) {
		Member member = getMember(memberId);
		SubscriptionPackage pkg = subpkgService.getSubscriptionPackage(pkgId);
		Subscription sub = new Subscription(member, pkg, null, TransactionStatus.ACTIVE);
		subscriptionDao.add(sub);
	}

	@Override
	public void issueBook(String bookid, int memberid) {
		String freebksecid = auxiliaryDao.getFreeBookSection(bookid);
		List<String> freememsecids = auxiliaryDao.getFreeMemberSections(memberid);
		if(freebksecid!=null && freememsecids!=null && freememsecids.contains(freebksecid)) {
			BookCopy bc = getBookCopy(bookid);
			Member m = getMember(memberid);
			Timestamp issueDate = new Timestamp(System.currentTimeMillis());
			BookTransaction bt = new BookTransaction(m, bc, issueDate, null, TransactionStatus.ACTIVE);
			bookTransactionDao.add(bt);
			bc.setMember(m);
			m.addBook(bc);
		}
	}

	@Override
	public void returnBook(String bookid, int memberid) {
		BookTransaction bktrans = auxiliaryDao.getActBkTrans(bookid, memberid);
		if(bktrans!=null) {
			BookCopy bc = getBookCopy(bookid);
			Member m = getMember(memberid);
			bktrans.setStatus(TransactionStatus.EXPIRED);
			bktrans.setReturnDate(new Timestamp(System.currentTimeMillis()));
			bc.setMember(null);
			m.removeBook(bc);
		}
	}

}
