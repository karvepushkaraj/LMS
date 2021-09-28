package com.app.lms.dao;

import java.util.List;

import com.app.lms.model.BookTransaction;

public interface AuxiliaryDao {
	
	String getFreeBookSection(String bookid);
	
	List<String> getFreeMemberSections(int memberid);
	
	BookTransaction getActBkTrans(String bookid, int memberid);

}
