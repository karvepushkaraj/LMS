package com.app.lms.service;

import com.app.lms.model.BookTransaction;
import com.app.lms.model.Deposite;
import com.app.lms.model.LateFee;
import com.app.lms.model.Member;
import com.app.lms.model.Subscription;
import com.app.lms.model.SubscriptionFee;
import com.app.lms.util.InvalidBusinessCondition;

public interface AccountService {
		
	void addDeposite(Member member, Deposite deposite) throws InvalidBusinessCondition;
	
	void removeDeposite(Member member, Deposite deposite) throws InvalidBusinessCondition;
	
	void addSubscriptionFee(Subscription subscription, SubscriptionFee fee) throws InvalidBusinessCondition;
	
	void addLateFee(BookTransaction transaction, LateFee fee) throws InvalidBusinessCondition;

}
