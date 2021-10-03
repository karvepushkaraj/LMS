package com.app.lms.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.lms.model.BookTransaction;

/**
 * Implementation of Auxiliary Dao interface.
 * 
 * @see AuxiliaryDao
 * @author karve
 *
 */

@Repository("AuxiliaryDao")
public class AuxiliaryDaoImpl implements AuxiliaryDao {

	@Autowired
	private EntityManager em;

	private Session getSession() {
		return em.unwrap(Session.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<String> getFreeBookSection(String bookid) {
		String queryString = "select section_id from free_book where book_id=?";
		Query<String> query = getSession().createSQLQuery(queryString);
		query.setParameter(1, bookid);
		String sectionId = null;
		List<String> list = query.list();
		if (list.size()==1)
			sectionId = list.get(0);
		return Optional.ofNullable(sectionId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFreeMemberSections(int memberid) {
		String queryString = "select section_id from free_member where member_id=?";
		Query<String> query = getSession().createSQLQuery(queryString);
		query.setParameter(1, memberid);
		return query.getResultList();
	}

	@Override
	public Optional<BookTransaction> getActBkTrans(String bookid, int memberid) {
		String queryString = "from BookTransaction where member_id=?1 and title_id=?2 and copy_id=?3 and status=1";
		Query<BookTransaction> query = getSession().createQuery(queryString, BookTransaction.class);
		query.setParameter(1, memberid);
		query.setParameter(2, bookid.substring(0, 4));
		query.setParameter(3, bookid.substring(4));
		BookTransaction bktrans = null;
		List<BookTransaction> list = query.list();
		if(list.size()==1)
			bktrans = list.get(0);
		return Optional.ofNullable(bktrans);
	}

}
