package com.app.lms.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.TransactionException;
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
		if (librarySection == null)
			throw new TransactionException("Invalid section id");
		bookTitle.setSection(librarySection);
		bookTitleDao.add(bookTitle);
		bookCopy.setCopyId(1);
		bookCopy.setTitle(bookTitle);
		bookCopyDao.add(bookCopy);
	}

	@Override
	public void addBookCopy(int titleId, BookCopy bookCopy) {
		BookTitle bookTitle = getBookTitle(titleId);
		if (bookTitle == null)
			throw new TransactionException("Invalid title id");
		List<BookCopy> list = bookTitle.getBookCopies();
		int copyId = list.get(list.size() - 1).getCopyId();
		bookCopy.setCopyId(++copyId);
		bookCopy.setTitle(bookTitle);
		bookCopyDao.add(bookCopy);
	}

	@Override
	public BookTitle getBookTitle(int titleId) {
		return bookTitleDao.getById(titleId);
	}

	@Override
	public BookCopy getBookCopy(String bookId) {
		BookTitle bt = getBookTitle(Integer.parseInt(bookId.substring(0, 4)));
		BookCopy bc = null;
		if (bt != null) {
			CopyId key = new CopyId(bt, Integer.valueOf(bookId.substring(4)));
			bc = bookCopyDao.getById(key);
		}
		return bc;
	}

	@Override
	public boolean deleteBook(String bookId) {
		BookTitle bt = getBookTitle(Integer.parseInt(bookId.substring(0, 4)));
		BookCopy bc = null;
		if (bt == null)
			throw new TransactionException("Book not found");
		CopyId key = new CopyId(bt, Integer.valueOf(bookId.substring(4)));
		bc = bookCopyDao.getById(key);
		if (bc == null)
			throw new TransactionException("Book not found");
		bookCopyDao.delete(bc);

		if (bt.getBookCopies().isEmpty()) {
			bookTitleDao.delete(bt);
		}
		if (getBookCopy(bookId) == null)
			return true;
		return false;
	}

	@Override
	public Member getMember(int memberId) {
		return memberDao.getById(memberId);
	}

	@Override
	public void addMember(Member member) {
		memberDao.add(member);
	}

	@Override
	public void updateMember(Member member) {
		Member m1 = memberDao.getById(member.getMemberId());
		if (m1 == null)
			throw new TransactionException("Member not found");
		m1.setName(member.getName());
		m1.setMobileNumber(member.getMobileNumber());
		m1.setEmailId(member.getEmailId());
	}

	@Override
	public boolean deleteMember(int memberId) {
		Member m1 = memberDao.getById(memberId);
		if (m1 == null)
			throw new TransactionException("Member not found");
		memberDao.delete(m1);
		if (getMember(memberId) == null)
			return true;
		return false;
	}

	@Override
	public void addSubscription(int memberId, int pkgId) {
		Member member = getMember(memberId);
		SubscriptionPackage pkg = subpkgService.getSubscriptionPackage(pkgId);
		if (member == null || pkg == null)
			throw new TransactionException("Invalid member id or package id");
		Subscription sub = new Subscription(member, pkg, new Date(System.currentTimeMillis()),
				TransactionStatus.ACTIVE);
		subscriptionDao.add(sub);
	}

	@Override
	public int issueBook(String bookid, int memberid) {
		String freebksecid = auxiliaryDao.getFreeBookSection(bookid);
		List<String> freememsecids = auxiliaryDao.getFreeMemberSections(memberid);
		if (freebksecid == null || freememsecids == null || !freememsecids.contains(freebksecid))
			return -1;
		BookCopy bc = getBookCopy(bookid);
		Member m = getMember(memberid);
		Timestamp issueDate = new Timestamp(System.currentTimeMillis());
		BookTransaction bt = new BookTransaction(m, bc, issueDate, null, TransactionStatus.ACTIVE);
		bookTransactionDao.add(bt);
		bc.setMember(m);
		m.addBook(bc);
		return auxiliaryDao.getActBkTrans(bookid, memberid).getTransactionId();
	}

	@Override
	public boolean returnBook(String bookid, int memberid) {
		BookTransaction bktrans = auxiliaryDao.getActBkTrans(bookid, memberid);
		if (bktrans == null)
			return false;
		BookCopy bc = getBookCopy(bookid);
		Member m = getMember(memberid);
		bktrans.setStatus(TransactionStatus.EXPIRED);
		bktrans.setReturnDate(new Timestamp(System.currentTimeMillis()));
		bc.setMember(null);
		m.removeBook(bc);
		return true;

	}

}
