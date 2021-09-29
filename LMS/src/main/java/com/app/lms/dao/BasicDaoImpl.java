package com.app.lms.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("BasicDao")
@Scope("prototype")
public class BasicDaoImpl<T, K extends Serializable> implements BasicDao<T, K> {

//	@Autowired
//	private SessionFactory sessionFactory;
	
	@Autowired
	private EntityManager em;
	
	private Class<T> clazz;

	@Override
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

//	private Session getSession() {
//		return sessionFactory.getCurrentSession();
//	}
	
	private Session getSession() {
		return em.unwrap(Session.class);
	}

	@Override
	public T getById(K key) {
		return getSession().get(clazz, key);
	}

	@Override
	public void add(T record) {
		getSession().save(record);
	}

	@Override
	public void delete(T record) {
		getSession().delete(record);
	}

}
