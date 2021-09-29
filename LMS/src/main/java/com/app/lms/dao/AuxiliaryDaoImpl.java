package com.app.lms.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.lms.model.BookTransaction;

@Repository("AuxiliaryDao")
public class AuxiliaryDaoImpl implements AuxiliaryDao {

//	@Autowired
//	private SessionFactory sessionFactory;
	
	@Autowired
	private EntityManager em;

//	private Session getSession() {
//		return sessionFactory.getCurrentSession();
//	}
	
	private Session getSession() {
		return em.unwrap(Session.class);
	}

	@Override
	public String getFreeBookSection(String bookid) {
		String queryString = "select sectionid from freebooks where bookid=?";
		Query<?> query = getSession().createSQLQuery(queryString);
		query.setParameter(1, bookid);
		List<?> list = query.list();
		if (list.size() == 1)
			return (String) list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFreeMemberSections(int memberid) {
		String queryString = "select sectionid from freemembers where memberid=?";
		Query<?> query = getSession().createSQLQuery(queryString);
		query.setParameter(1, memberid);
		List<?> list = query.list();
		if (!list.isEmpty())
			return (List<String>) list;
		return null;
	}

	@Override
	public BookTransaction getActBkTrans(String bookid, int memberid) {
		String queryString = "from BookTransaction where memberId=?1 and titleId=?2 and copyId=?3 and status=1";
		Query<BookTransaction> query = getSession().createQuery(queryString, BookTransaction.class);
		query.setParameter(1, memberid);
		query.setParameter(2, bookid.substring(0, 4));
		query.setParameter(3, bookid.substring(4));
		List<BookTransaction> list = query.list();
		if (list.size() == 1)
			return list.get(0);
		return null;
	}

}
