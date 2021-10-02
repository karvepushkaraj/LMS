package com.app.lms.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.app.lms.model.MemberActivityStatus;
import com.app.lms.model.Subscription;
import com.app.lms.model.SubscriptionPackage;
import com.app.lms.model.TransactionStatus;
import com.app.lms.util.InvalidBusinessCondition;

/**
 * Implementation of {@link BookService}, {@link MemberService},
 * {@link BookTransactionService}
 * 
 * @author karve
 *
 */

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
	public BookTitle getBookTitle(int titleId) throws InvalidBusinessCondition {
		return bookTitleDao.getById(titleId).orElseThrow(() -> new InvalidBusinessCondition("Book does not exist"));
	}

	@Override
	public BookCopy getBookCopy(String bookId) throws InvalidBusinessCondition {
		BookTitle bt = getBookTitle(Integer.parseInt(bookId.substring(0, 4)));
		CopyId key = new CopyId(bt, Integer.valueOf(bookId.substring(4)));
		return bookCopyDao.getById(key).orElseThrow(() -> new InvalidBusinessCondition("Book does not exist"));
	}

	@Override
	public void addBook(String sectionId, BookTitle bookTitle, BookCopy bookCopy) throws InvalidBusinessCondition {
		if (bookTitle == null || bookCopy == null)
			throw new InvalidBusinessCondition("Invalid Input");
		LibrarySection librarySection = librarySectionService.getLibrarySection(sectionId);
		bookTitle.setSection(librarySection);
		bookTitleDao.add(Optional.of(bookTitle));
		bookCopy.setCopyId(1);
		bookCopy.setTitle(bookTitle);
		bookCopyDao.add(Optional.of(bookCopy));
	}

	@Override
	public void addBookCopy(int titleId, BookCopy bookCopy) throws InvalidBusinessCondition {
		if (bookCopy == null)
			throw new InvalidBusinessCondition("Invalid Input");
		BookTitle bookTitle = getBookTitle(titleId);
		List<BookCopy> list = bookTitle.getBookCopies();
		bookCopy.setCopyId(list.get(list.size() - 1).getCopyId() + 1);
		bookCopy.setTitle(bookTitle);
		bookCopyDao.add(Optional.of(bookCopy));
	}

	@Override
	public void deleteBook(String bookId) throws InvalidBusinessCondition {
		BookTitle bt = getBookTitle(Integer.parseInt(bookId.substring(0, 4)));
		CopyId key = new CopyId(bt, Integer.valueOf(bookId.substring(4)));
		BookCopy bc = bookCopyDao.getById(key).orElseThrow(() -> new InvalidBusinessCondition("Book does not exist"));
		bookCopyDao.delete(Optional.of(bc));
		if (bt.getBookCopies().isEmpty())
			bookTitleDao.delete(Optional.of(bt));
	}

	@Override
	public Member getMember(int memberId) throws InvalidBusinessCondition {
		return memberDao.getById(memberId).orElseThrow(() -> new InvalidBusinessCondition("Member does not exist"));
	}

	@Override
	public void addMember(Member member) throws InvalidBusinessCondition {
		if (member == null)
			throw new InvalidBusinessCondition("Invalid Input");
		member.setEnrollmentDate(new Date(System.currentTimeMillis()));
		member.setStatus(MemberActivityStatus.ACTIVE);
		memberDao.add(Optional.of(member));
	}

	@Override
	public void updateMember(Member member) throws InvalidBusinessCondition {
		Member m1 = getMember(member.getMemberId());
		m1.setName(member.getName());
		m1.setMobileNumber(member.getMobileNumber());
		m1.setEmailId(member.getEmailId());
	}

	@Override
	public void deleteMember(int memberId) throws InvalidBusinessCondition {
		memberDao.delete(Optional.of(getMember(memberId)));
	}

	@Override
	public void addSubscription(int memberId, int pkgId) throws InvalidBusinessCondition {
		Member member = getMember(memberId);
		SubscriptionPackage pkg = subpkgService.getSubscriptionPackage(pkgId);
		Subscription sub = new Subscription(member, pkg, new Date(System.currentTimeMillis()),
				TransactionStatus.ACTIVE);
		subscriptionDao.add(Optional.of(sub));
	}

	@Override
	public int issueBook(String bookid, int memberid) throws InvalidBusinessCondition {
		String freebksecid = auxiliaryDao.getFreeBookSection(bookid)
				.orElseThrow(() -> new InvalidBusinessCondition("Book is not free"));
		List<String> freememsecids = auxiliaryDao.getFreeMemberSections(memberid);
		if (!freememsecids.contains(freebksecid))
			throw new InvalidBusinessCondition("Required subscription does not exist");
		BookCopy bc = getBookCopy(bookid);
		Member m = getMember(memberid);
		Timestamp issueDate = new Timestamp(System.currentTimeMillis());
		BookTransaction bt = new BookTransaction(m, bc, issueDate, null, TransactionStatus.ACTIVE);
		bookTransactionDao.add(Optional.of(bt));
		bc.setMember(m);
		m.addBook(bc);
		BookTransaction bktrans = auxiliaryDao.getActBkTrans(bookid, memberid)
				.orElseThrow(() -> new InvalidBusinessCondition("Transaction failed"));
		return bktrans.getTransactionId();
	}

	@Override
	public int returnBook(String bookid, int memberid) throws InvalidBusinessCondition {
		BookTransaction bktrans = auxiliaryDao.getActBkTrans(bookid, memberid)
				.orElseThrow(() -> new InvalidBusinessCondition("Transaction failed"));
		BookCopy bc = getBookCopy(bookid);
		Member m = getMember(memberid);
		bktrans.setStatus(TransactionStatus.EXPIRED);
		bktrans.setReturnDate(new Timestamp(System.currentTimeMillis()));
		bc.setMember(null);
		m.removeBook(bc);
		return bktrans.getTransactionId();
	}

}
