package com.app.lms.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
 * {@link BookTransactionService} interfaces.
 * 
 * @author karve
 *
 */

@Service("LibraryManagementService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = InvalidBusinessCondition.class)
public class LibraryManagementService implements BookService, MemberService, BookTransactionService {

	private final BasicDao<BookTitle, Integer> bookTitleDao;

	private final BasicDao<BookCopy, CopyId> bookCopyDao;

	private final BasicDao<Member, Integer> memberDao;

	private final BasicDao<Subscription, Integer> subscriptionDao;

	private final BasicDao<BookTransaction, Integer> bookTransactionDao;

	private final AuxiliaryDao auxiliaryDao;

	private final LibrarySectionService librarySectionService;

	private final SubscriptionPackageService subpkgService;

	private final AccountService accountService;

	@Autowired
	public LibraryManagementService(BasicDao<BookTitle, Integer> bookTitleDao, BasicDao<BookCopy, CopyId> bookCopyDao,
			BasicDao<Member, Integer> memberDao, BasicDao<Subscription, Integer> subscriptionDao,
			BasicDao<BookTransaction, Integer> bookTransactionDao, AuxiliaryDao auxiliaryDao,
			LibrarySectionService librarySectionService, SubscriptionPackageService subpkgService,
			AccountService accountService) {
		this.bookTitleDao = bookTitleDao;
		this.bookCopyDao = bookCopyDao;
		this.memberDao = memberDao;
		this.subscriptionDao = subscriptionDao;
		this.bookTransactionDao = bookTransactionDao;
		this.auxiliaryDao = auxiliaryDao;
		this.librarySectionService = librarySectionService;
		this.subpkgService = subpkgService;
		this.accountService = accountService;
	}

	@PostConstruct
	private void setClazz() {
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
		BookTitle bookTitle = getBookTitle(Integer.parseInt(bookId.substring(0, 4)));
		CopyId key = new CopyId(bookTitle, Integer.parseInt(bookId.substring(4)));
		return bookCopyDao.getById(key).orElseThrow(() -> new InvalidBusinessCondition("Book does not exist"));
	}

	@Override
	public String addBook(String sectionId, BookTitle bookTitle, BookCopy bookCopy) throws InvalidBusinessCondition {
		if (bookTitle == null || bookCopy == null)
			throw new InvalidBusinessCondition("Invalid Input");
		if (bookTitle.getTitleId() != 0)
			return addBookCopy(bookTitle.getTitleId(), bookCopy);
		LibrarySection librarySection = librarySectionService.getLibrarySection(sectionId);
		bookTitle.setSection(librarySection);
		bookTitleDao.add(Optional.of(bookTitle));
		bookCopy.setCopyId(1);
		bookCopy.setTitle(bookTitle);
		bookCopyDao.add(Optional.of(bookCopy));
		return "" + bookTitle.getTitleId() + bookCopy.getCopyId();
	}

	@Override
	public String addBookCopy(int titleId, BookCopy bookCopy) throws InvalidBusinessCondition {
		if (bookCopy == null)
			throw new InvalidBusinessCondition("Invalid Input");
		BookTitle bookTitle = getBookTitle(titleId);
		synchronized (bookCopy) {
			List<BookCopy> list = bookTitle.getBookCopies();
			bookCopy.setCopyId(list.get(list.size() - 1).getCopyId() + 1);
			bookCopy.setTitle(bookTitle);
			bookCopyDao.add(Optional.of(bookCopy));
		}
		return "" + bookTitle.getTitleId() + bookCopy.getCopyId();
	}

	@Override
	public void deleteBook(String bookId) throws InvalidBusinessCondition {
		BookCopy bookCopy = getBookCopy(bookId);
		BookTitle bookTitle = bookCopy.getTitle();
		if (bookCopy.getMember() != null)
			throw new InvalidBusinessCondition("Book is issued");
		bookTitle.removeBookCopy(bookCopy);
		if (bookTitle.getBookCopies().isEmpty())
			bookTitleDao.delete(Optional.of(bookTitle));
	}

	@Override
	public Member getMember(int memberId) throws InvalidBusinessCondition {
		return memberDao.getById(memberId).orElseThrow(() -> new InvalidBusinessCondition("Member does not exist"));
	}

	@Override
	public int addMember(Member member, Deposite deposite) throws InvalidBusinessCondition {
		if (member == null)
			throw new InvalidBusinessCondition("Invalid Input");
		member.setEnrollmentDate(new Date(System.currentTimeMillis()));
		member.setStatus(ActivityStatus.EXPIRED);
		memberDao.add(Optional.of(member));
		accountService.addDeposite(member, deposite);
		member.setStatus(ActivityStatus.ACTIVE);
		return member.getMemberId();
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
		member.setExitDate(new Date(System.currentTimeMillis()));
		member.setStatus(ActivityStatus.EXPIRED);
	}

	@Override
	public int addSubscription(int memberId, int pkgId, SubscriptionFee fee) throws InvalidBusinessCondition {
		Member member = getMember(memberId);
		SubscriptionPackage pkg = subpkgService.getSubscriptionPackage(pkgId);
		Subscription sub = new Subscription(member, pkg, new Date(System.currentTimeMillis()), ActivityStatus.ACTIVE);
		subscriptionDao.add(Optional.of(sub));
		accountService.addSubscriptionFee(sub, fee);
		return sub.getSubscriptionId();
	}

	@Override
	public int issueBook(String bookid, int memberid) throws InvalidBusinessCondition {
		String freebksecid = auxiliaryDao.getFreeBookSection(bookid)
				.orElseThrow(() -> new InvalidBusinessCondition("Book is not free"));
		List<String> freememsecids = auxiliaryDao.getFreeMemberSections(memberid);
		if (!freememsecids.contains(freebksecid))
			throw new InvalidBusinessCondition("Member Subscription does not exist");
		BookCopy bookCopy = getBookCopy(bookid);
		Member member = getMember(memberid);
		Timestamp issueDate = new Timestamp(System.currentTimeMillis());
		BookTransaction bktrans = new BookTransaction(member, bookCopy, issueDate, null, ActivityStatus.ACTIVE);
		bookTransactionDao.add(Optional.of(bktrans));
		bookCopy.setMember(member);
		member.addBook(bookCopy);
		return bktrans.getTransactionId();
	}

	@Override
	public int returnBook(String bookid, int memberid, LateFee fee) throws InvalidBusinessCondition {
		BookTransaction bktrans = auxiliaryDao.getActBkTrans(bookid, memberid)
				.orElseThrow(() -> new InvalidBusinessCondition("Issue Transaction does not exist"));
		Date returnDate = new Timestamp(System.currentTimeMillis());
		long diffInMillis = returnDate.getTime() - bktrans.getIssueDate().getTime(); // get return duration in millisec
		// convert millisec to minutes
		long bookReturnDuration = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
		// late return period is compared to minutes i.e. 1 day = 1 min
		if (bookReturnDuration > BookTransaction.LATE_RETURN_DAYS)
			accountService.addLateFee(bktrans, fee);
		BookCopy bookCopy = getBookCopy(bookid);
		Member member = getMember(memberid);
		bktrans.setStatus(ActivityStatus.EXPIRED);
		bktrans.setReturnDate(returnDate);
		bookCopy.setMember(null);
		member.removeBook(bookCopy);
		return bktrans.getTransactionId();
	}

	@Override
	public void expireSubscription() {
		auxiliaryDao.getActSubscriptions().forEach(sub -> sub.setStatus(ActivityStatus.EXPIRED));
	}

}
