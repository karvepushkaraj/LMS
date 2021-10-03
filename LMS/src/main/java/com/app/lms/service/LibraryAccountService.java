package com.app.lms.service;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.lms.dao.BasicDao;
import com.app.lms.model.BookTransaction;
import com.app.lms.model.Deposite;
import com.app.lms.model.LateFee;
import com.app.lms.model.Member;
import com.app.lms.model.MemberActivityStatus;
import com.app.lms.model.Subscription;
import com.app.lms.model.SubscriptionFee;
import com.app.lms.util.InvalidBusinessCondition;

@Service("LibraryAccountService")
@Transactional
public class LibraryAccountService implements AccountService {

	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<Deposite, String> depositeDao;

	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<SubscriptionFee, Integer> subscriptionFeeDao;

	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<LateFee, Integer> lateFeeDao;

	@PostConstruct
	public void SetClazz() {
		depositeDao.setClazz(Deposite.class);
		subscriptionFeeDao.setClazz(SubscriptionFee.class);
		lateFeeDao.setClazz(LateFee.class);
	}

	@Override
	public void addDeposite(Member member, Deposite deposite) throws InvalidBusinessCondition {
		if (deposite == null || deposite.getDeposite() != Deposite.getMemberDespoite())
			throw new InvalidBusinessCondition("Deposite should be Rs." + Deposite.getMemberDespoite());
		if (!member.getStatus().equals(MemberActivityStatus.INACTIVE))
			throw new InvalidBusinessCondition("Member is not Inactive");
		deposite.setMember(member);
		deposite.setTransactionId(member.getMemberId() + "D");
		depositeDao.add(Optional.of(deposite));
	}

	@Override
	public void removeDeposite(Member member, Deposite deposite) throws InvalidBusinessCondition {
		if (deposite == null || deposite.getDeposite() != Deposite.getMemberDespoite())
			throw new InvalidBusinessCondition("Deposite should be Rs." + Deposite.getMemberDespoite());
		if (member.getStatus().equals(MemberActivityStatus.INACTIVE))
			throw new InvalidBusinessCondition("Member is Inactive");
		deposite.setMember(member);
		deposite.setTransactionId(member.getMemberId() + "R");
		depositeDao.add(Optional.of(deposite));
	}

	@Override
	public void addSubscriptionFee(Subscription subscription, SubscriptionFee fee) throws InvalidBusinessCondition {
		if (fee == null || fee.getFees() != subscription.getSubscriptionPackage().getFees())
			throw new InvalidBusinessCondition(
					"Subscription Fee should be Rs." + subscription.getSubscriptionPackage().getFees());
		fee.setSubscription(subscription);
		subscriptionFeeDao.add(Optional.of(fee));
	}

	@Override
	public void addLateFee(BookTransaction transaction, LateFee fee) throws InvalidBusinessCondition {
		if (fee == null || fee.getLateFees() != LateFee.getLateFeeAmount())
			throw new InvalidBusinessCondition("Late Fee should be Rs." + LateFee.getLateFeeAmount());
		fee.setTransaction(transaction);
		lateFeeDao.add(Optional.of(fee));
	}

}
