package com.app.lms.service;

import java.util.Map;

import com.app.lms.model.SubscriptionPackage;

public interface SubscriptionPackageService {

	void addSubscriptionPackage(SubscriptionPackage pkg, Map<String, Integer> map);

	SubscriptionPackage getSubscriptionPackage(int id);

	boolean deleteSubscriptionPackage(int id);

}
