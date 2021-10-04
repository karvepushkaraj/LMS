package com.app.lms.service;

import java.util.Map;

import com.app.lms.model.SubscriptionPackage;
import com.app.lms.util.InvalidBusinessCondition;

/**
 * Service interface for all operations related to Subscription Packages.
 * 
 * @author karve
 *
 */

public interface SubscriptionPackageService {

	SubscriptionPackage getSubscriptionPackage(int id) throws InvalidBusinessCondition;

	int addSubscriptionPackage(SubscriptionPackage pkg, Map<String, Integer> map) throws InvalidBusinessCondition;

	void deleteSubscriptionPackage(int id) throws InvalidBusinessCondition;

}
