package com.app.lms.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.lms.dao.AuxiliaryDao;
import com.app.lms.dao.BasicDao;
import com.app.lms.model.ActivityStatus;
import com.app.lms.model.BookCopy;
import com.app.lms.model.BookTitle;
import com.app.lms.model.BookTransaction;
import com.app.lms.model.CopyId;
import com.app.lms.model.Deposite;
import com.app.lms.model.LateFee;
import com.app.lms.model.LibrarySection;
import com.app.lms.model.Member;
import com.app.lms.model.Subscription;
import com.app.lms.model.SubscriptionFee;
import com.app.lms.model.SubscriptionPackage;
import com.app.lms.util.InvalidBusinessCondition;

/**
 * Implementation of {@link BookService}, {@link MemberService},
 * {@link BookTransactionService}
 * 
 * @author karve
 *
 */

@Service("LibraryManagementService")
@Transactional(rollbackFor = InvalidBusinessCondition.class)
public class LibraryManagementService implements BookService, MemberService, BookTransactionService {

	@Autowired
	private BasicDao<BookTitle, Integer> bookTitleDao;

	@Autowired
	private BasicDao<BookCopy, CopyId> bookCopyDao;

	@Autowired
	private BasicDao<Member, Integer> memberDao;

	@Autowired
	private BasicDao<Subscription, Integer> subscriptionDao;

	@Autowired
	private BasicDao<BookTransaction, Integer> bookTransactionDao;

	@Autowired
	private AuxiliaryDao auxiliaryDao;

	@Autowired
	private LibrarySectionService librarySectionService;

	@Autowired
	private SubscriptionPackageService subpkgService;

	@Autowired
	private AccountService accountService;

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
		BookTitle bt = null;
		CopyId key;
		try {
			bt = getBookTitle(Integer.parseInt(bookId.substring(0, 4)));
			key = new CopyId(bt, Integer.valueOf(bookId.substring(4)));
		} catch (NumberFormatException e) {
			throw new InvalidBusinessCondition("Invalid Input", e);
		}
		BookCopy bc = bookCopyDao.getById(key).orElseThrow(() -> new InvalidBusinessCondition("Book does not exist"));
		if (bc.getMember() != null)
			throw new InvalidBusinessCondition("Book is issued");
		bookCopyDao.delete(Optional.of(bc));
		if (bt.getBookCopies().isEmpty())
			bookTitleDao.delete(Optional.of(bt));
	}

	@Override
	public Member getMember(int memberId) throws InvalidBusinessCondition {
		return memberDao.getById(memberId).orElseThrow(() -> new InvalidBusinessCondition("Member does not exist"));
	}

	@Override
	public void addMember(Member member, Deposite deposite) throws InvalidBusinessCondition {
		if (member == null)
			throw new InvalidBusinessCondition("Invalid Input");
		member.setEnrollmentDate(new Date(System.currentTimeMillis()));
		member.setStatus(ActivityStatus.EXPIRED);
		memberDao.add(Optional.of(member));
		accountService.addDeposite(member, deposite);
		member.setStatus(ActivityStatus.ACTIVE);
	}

	@Override
	public void updateMember(Member member) throws InvalidBusinessCondition {
		Member m1 = getMember(member.getMemberId());
		m1.setName(member.getName());
		m1.setMobileNumber(member.getMobileNumber());
		m1.setEmailId(member.getEmailId());
	}

	@Override
	public void deleteMember(int memberId, Deposite deposite) throws InvalidBusinessCondition {
		Member member = getMember(memberId);
		if (!member.getBook().isEmpty())
			throw new InvalidBusinessCondition("Book issued to this member");
		accountService.removeDeposite(member, deposite);
		member.setStatus(ActivityStatus.EXPIRED);
	}

	@Override
	public void addSubscription(int memberId, int pkgId, SubscriptionFee fee) throws InvalidBusinessCondition {
		Member member = getMember(memberId);
		SubscriptionPackage pkg = subpkgService.getSubscriptionPackage(pkgId);
		Subscription sub = new Subscription(member, pkg, new Date(System.currentTimeMillis()), ActivityStatus.ACTIVE);
		subscriptionDao.add(Optional.of(sub));
		accountService.addSubscriptionFee(sub, fee);
	}

	@Override
	public int issueBook(String bookid, int memberid) throws InvalidBusinessCondition {
		String freebksecid = auxiliaryDao.getFreeBookSection(bookid)
				.orElseThrow(() -> new InvalidBusinessCondition("Book is not free"));
		List<String> freememsecids = auxiliaryDao.getFreeMemberSections(memberid);
		if (!freememsecids.contains(freebksecid))
			throw new InvalidBusinessCondition("Member Subscription does not exist");
		BookCopy bc = getBookCopy(bookid);
		Member m = getMember(memberid);
		Timestamp issueDate = new Timestamp(System.currentTimeMillis());
		BookTransaction bt = new BookTransaction(m, bc, issueDate, null, ActivityStatus.ACTIVE);
		bookTransactionDao.add(Optional.of(bt));
		bc.setMember(m);
		m.addBook(bc);
		BookTransaction bktrans = auxiliaryDao.getActBkTrans(bookid, memberid)
				.orElseThrow(() -> new InvalidBusinessCondition("Transaction failed"));
		return bktrans.getTransactionId();
	}

	@Override
	public int returnBook(String bookid, int memberid, LateFee fee) throws InvalidBusinessCondition {
		BookTransaction bktrans = auxiliaryDao.getActBkTrans(bookid, memberid)
				.orElseThrow(() -> new InvalidBusinessCondition("Issue Transaction does not exist"));
		Date returnDate = new Timestamp(System.currentTimeMillis());
		long diffInMillis = returnDate.getTime() - bktrans.getIssueDate().getTime();
		long bookReturnDuration = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
		if (bookReturnDuration > BookTransaction.LATE_RETURN_DAYS)
			accountService.addLateFee(bktrans, fee);
		BookCopy bc = getBookCopy(bookid);
		Member m = getMember(memberid);
		bktrans.setStatus(ActivityStatus.EXPIRED);
		bktrans.setReturnDate(returnDate);
		bc.setMember(null);
		m.removeBook(bc);
		return bktrans.getTransactionId();
	}

	@Override
	public void expireSubscription() {
		List<Subscription> list = auxiliaryDao.getActSubscriptions();
		list.forEach((sub) -> sub.setStatus(ActivityStatus.EXPIRED));
	}

}
