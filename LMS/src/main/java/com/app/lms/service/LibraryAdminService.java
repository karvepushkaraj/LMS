package com.app.lms.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.lms.dao.BasicDao;
import com.app.lms.model.LibrarySection;
import com.app.lms.model.PackageSection;
import com.app.lms.model.PackageSectionId;
import com.app.lms.model.SubscriptionPackage;

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
	public LibrarySection getLibrarySection(String id) {
		return librarySectionDao.getById(id);
	}

	@Override
	public void addLibrarySection(LibrarySection bookSection) {
		librarySectionDao.add(bookSection);
	}

	@Override
	public void updateLibrarySection(LibrarySection bookSection) {
		LibrarySection bs = getLibrarySection(bookSection.getSectionId());
		if(bs!=null)
			bs.setSectionName(bookSection.getSectionName());
	}

	@Override
	public boolean deleteLibrarySection(String id) {
		LibrarySection bs = getLibrarySection(id);
		if (bs == null)
			throw new TransactionException("Library Section not Found");
		librarySectionDao.delete(bs);
		if(getLibrarySection(id)==null)
			return true;
		return false;
	}

	@Override
	public void addSubscriptionPackage(SubscriptionPackage pkg, Map<String, Integer> map) {
		subscriptionPackageDao.add(pkg);
		for (String key : map.keySet()) {
			LibrarySection ls = getLibrarySection(key);
			if (ls == null)
				throw new TransactionException("Invalid Section ID : "+ key);
			PackageSection pkgsec = new PackageSection(ls, pkg, map.get(key));
			pkgSecDao.add(pkgsec);

		}
	}

	@Override
	public SubscriptionPackage getSubscriptionPackage(int id) {
		return subscriptionPackageDao.getById(id);
	}

	@Override
	public boolean deleteSubscriptionPackage(int id) {
		SubscriptionPackage pkg = getSubscriptionPackage(id);
		if (pkg == null)
			throw new TransactionException("Subscription Package not Found");
		subscriptionPackageDao.delete(pkg);
		if(getSubscriptionPackage(id)==null)
			return true;
		return false;
	}

}
