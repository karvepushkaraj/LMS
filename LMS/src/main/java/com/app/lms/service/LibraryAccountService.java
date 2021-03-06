package com.app.lms.service;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.lms.dao.BasicDao;
import com.app.lms.model.BookTransaction;
import com.app.lms.model.Deposite;
import com.app.lms.model.LateFee;
import com.app.lms.model.Member;
import com.app.lms.model.Subscription;
import com.app.lms.model.SubscriptionFee;
import com.app.lms.util.InvalidBusinessCondition;

/**
 * Implementation of {@link AccountService} interface.
 * 
 * @author karve
 *
 */

@Service("LibraryAccountService")
@Transactional
public class LibraryAccountService implements AccountService {

	@Autowired
	private BasicDao<Deposite, String> depositeDao;

	@Autowired
	private BasicDao<SubscriptionFee, Integer> subscriptionFeeDao;

	@Autowired
	private BasicDao<LateFee, Integer> lateFeeDao;

	@PostConstruct
	private void SetClazz() {
		depositeDao.setClazz(Deposite.class);
		subscriptionFeeDao.setClazz(SubscriptionFee.class);
		lateFeeDao.setClazz(LateFee.class);
	}

	@Override
	public void addDeposite(Member member, Deposite deposite) throws InvalidBusinessCondition {
		if (Deposite.isInvalid(deposite))
			throw new InvalidBusinessCondition("Deposite should be Rs." + Deposite.MEMBER_DEPOSITE);
		if (member.isActive())
			throw new InvalidBusinessCondition("Member is not Inactive");
		deposite.setMember(member);
		deposite.setTransactionId(member.getMemberId() + "D");
		depositeDao.add(Optional.of(deposite));
	}

	@Override
	public void removeDeposite(Member member, Deposite deposite) throws InvalidBusinessCondition {
		if (Deposite.isInvalid(deposite))
			throw new InvalidBusinessCondition("Deposite should be Rs." + Deposite.MEMBER_DEPOSITE);
		if (!member.isActive())
			throw new InvalidBusinessCondition("Member is not Active");
		deposite.setMember(member);
		deposite.setTransactionId(member.getMemberId() + "R");
		depositeDao.add(Optional.of(deposite));
	}

	@Override
	public void addSubscriptionFee(Subscription subscription, SubscriptionFee fee) throws InvalidBusinessCondition {
		if (fee == null || !fee.isCorrect(subscription))
			throw new InvalidBusinessCondition(
					"Subscription Fee should be Rs." + subscription.getSubscriptionPackage().getFees());
		fee.setSubscriptionId(subscription.getSubscriptionId());
		subscriptionFeeDao.add(Optional.of(fee));
	}

	@Override
	public void addLateFee(BookTransaction transaction, LateFee fee) throws InvalidBusinessCondition {
		if (LateFee.isInvalid(fee))
			throw new InvalidBusinessCondition("Late Fee should be Rs." + LateFee.LATE_FEE);
		fee.setTransactionId(transaction.getTransactionId());
		lateFeeDao.add(Optional.of(fee));
	}

}
