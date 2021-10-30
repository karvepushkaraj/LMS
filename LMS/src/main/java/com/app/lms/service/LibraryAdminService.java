package com.app.lms.service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.app.lms.dao.BasicDao;
import com.app.lms.model.LibrarySection;
import com.app.lms.model.PackageSection;
import com.app.lms.model.PackageSectionId;
import com.app.lms.model.SubscriptionPackage;
import com.app.lms.util.InvalidBusinessCondition;

/**
 * Implementation of {@link LibrarySectionService},
 * {@link SubscriptionPackageService} interfaces.
 * 
 * @author karve
 *
 */

@Service("LibraryAdminService")
@Transactional
public class LibraryAdminService implements LibrarySectionService, SubscriptionPackageService {

	@Autowired
	private BasicDao<LibrarySection, String> librarySectionDao;

	@Autowired
	private BasicDao<SubscriptionPackage, Integer> subscriptionPackageDao;

	@Autowired
	private BasicDao<PackageSection, PackageSectionId> pkgSecDao;

//	@Autowired
//	public LibraryAdminService(BasicDao<LibrarySection, String> librarySectionDao,
//			BasicDao<SubscriptionPackage, Integer> subscriptionPackageDao,
//			BasicDao<PackageSection, PackageSectionId> pkgSecDao) {
//		this.librarySectionDao = librarySectionDao;
//		this.subscriptionPackageDao = subscriptionPackageDao;
//		this.pkgSecDao = pkgSecDao;
//	}

	@PostConstruct
	private void setClazz() {
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
		LibrarySection ls = getLibrarySection(bookSection.getSectionId());
		ls.setSectionName(bookSection.getSectionName());
	}

	@Override
	public SubscriptionPackage getSubscriptionPackage(int id) throws InvalidBusinessCondition {
		return subscriptionPackageDao.getById(id)
				.orElseThrow(() -> new InvalidBusinessCondition("Subscription Package does not exist"));
	}

	@Override
	public int addSubscriptionPackage(SubscriptionPackage pkg, Map<String, Integer> map)
			throws InvalidBusinessCondition {
		if (CollectionUtils.isEmpty(map))
			throw new InvalidBusinessCondition("Library Sections cannot be empty");
		try {
			subscriptionPackageDao.add(Optional.ofNullable(pkg));
			for (String key : map.keySet()) {
				@Valid
				PackageSection pkgsec = new PackageSection(getLibrarySection(key), pkg, map.get(key));
				pkgSecDao.add(Optional.of(pkgsec));
			}
			return pkg.getPackageId();
		} catch (NoSuchElementException | ConstraintViolationException e) {
			throw new InvalidBusinessCondition("Invalid Input", e);
		}
	}

	@Override
	public void deleteSubscriptionPackage(int id) throws InvalidBusinessCondition {
		subscriptionPackageDao.delete(Optional.of(getSubscriptionPackage(id)));
	}

}
