package com.app.lms.service;

import java.util.Map;

import com.app.lms.model.SubscriptionPackage;

/**
 * Service interface for all operations related to Subscription Packages.
 * 
 * @author karve
 *
 */

public interface SubscriptionPackageService {

	SubscriptionPackage getSubscriptionPackage(int id);

	void addSubscriptionPackage(SubscriptionPackage pkg, Map<String, Integer> map);

	boolean deleteSubscriptionPackage(int id);

}
