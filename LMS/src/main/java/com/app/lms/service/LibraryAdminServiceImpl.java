package com.app.lms.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.lms.dao.BasicDao;
import com.app.lms.model.LibrarySection;

@Service("LibraryAdminService")
@Transactional
public class LibraryAdminServiceImpl implements LibrarySectionService {
	
	@Autowired
	@Qualifier("BasicDao")
	private BasicDao<LibrarySection, String> basicDao;
	
	@PostConstruct
	public void setClazz() {
		basicDao.setClazz(LibrarySection.class);
	}

	@Override
	public LibrarySection getLibrarySection(String id) {
		return basicDao.getById(id);
	}
	
	@Override
	public void addLibrarySection(LibrarySection bookSection) {
		basicDao.add(bookSection);
	}
	
	@Override
	public void updateLibrarySection(LibrarySection bookSection) {
		LibrarySection bs = getLibrarySection(bookSection.getSectionId());
		if(bs!=null)
			bs.setSectionName(bookSection.getSectionName());
	}

	@Override
	public void deleteLibrarySection(String id) {
		LibrarySection bs = getLibrarySection(id);
		if(bs!=null)
			basicDao.delete(bs);
	}

}
