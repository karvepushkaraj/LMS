package com.app.lms.service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.lms.dao.BasicDao;
import com.app.lms.model.LibrarySection;
import com.app.lms.model.PackageSection;
import com.app.lms.model.PackageSectionId;
import com.app.lms.model.SubscriptionPackage;
import com.app.lms.util.InvalidBusinessCondition;

/**
 * Implementation of {@link LibrarySectionService},
 * {@link SubscriptionPackageService}
 * 
 * @author karve
 *
 */

@Service("LibraryAdminService")
@Transactional
public class LibraryAdminService implements LibrarySectionService, SubscriptionPackageService {

	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<LibrarySection, String> librarySectionDao;

	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<SubscriptionPackage, Integer> subscriptionPackageDao;

	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<PackageSection, PackageSectionId> pkgSecDao;

	@PostConstruct
	public void setClazz() {
		librarySectionDao.setClazz(LibrarySection.class);
		subscriptionPackageDao.setClazz(SubscriptionPackage.class);
		pkgSecDao.setClazz(PackageSection.class);
	}

	@Override
	public LibrarySection getLibrarySection(String id) throws InvalidBusinessCondition {
		return librarySectionDao.getById(id)
				.orElseThrow(() -> new InvalidBusinessCondition("Library Section does not exist"));
	}

	@Override
	public void addLibrarySection(LibrarySection bookSection) throws InvalidBusinessCondition {
		try {
			librarySectionDao.add(Optional.ofNullable(bookSection));
		} catch (NoSuchElementException e) {
			throw new InvalidBusinessCondition("Invalid Input");
		}
	}

	@Override
	public void updateLibrarySection(LibrarySection bookSection) throws InvalidBusinessCondition {
		if (bookSection == null)
			throw new InvalidBusinessCondition("Invalid Input");
		LibrarySection bs = getLibrarySection(bookSection.getSectionId());
		bs.setSectionName(bookSection.getSectionName());
	}

	@Override
	public void deleteLibrarySection(String id) throws InvalidBusinessCondition {
		librarySectionDao.delete(Optional.of(getLibrarySection(id)));
	}

	@Override
	public SubscriptionPackage getSubscriptionPackage(int id) throws InvalidBusinessCondition {
		return subscriptionPackageDao.getById(id)
				.orElseThrow(() -> new InvalidBusinessCondition("Subscription Package does not exist"));
	}

	@Override
	public void addSubscriptionPackage(SubscriptionPackage pkg, Map<String, Integer> map)
			throws InvalidBusinessCondition {
		if (map == null || map.isEmpty())
			throw new InvalidBusinessCondition("Library Section cannot be empty");
		try {
			subscriptionPackageDao.add(Optional.ofNullable(pkg));
		} catch (NoSuchElementException e) {
			throw new InvalidBusinessCondition("Invalid Input");
		}
		for (String key : map.keySet()) {
			LibrarySection ls = getLibrarySection(key);
			PackageSection pkgsec = new PackageSection(ls, pkg, map.get(key));
			pkgSecDao.add(Optional.of(pkgsec));
		}
	}

	@Override
	public void deleteSubscriptionPackage(int id) throws InvalidBusinessCondition {
		subscriptionPackageDao.delete(Optional.of(getSubscriptionPackage(id)));
	}

}
