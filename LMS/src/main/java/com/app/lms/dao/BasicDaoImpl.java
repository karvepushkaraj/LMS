package com.app.lms.dao;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * Generic implementation of Basic Dao interface.
 * 
 * @see BasicDao
 * @author karve
 *
 * @param <T> model
 * @param <K> key
 */

@Repository("BasicDao")
@Scope("prototype")
public class BasicDaoImpl<T, K extends Serializable> implements BasicDao<T, K> {

	@Autowired
	private EntityManager em;

	private Class<T> clazz;

	@Override
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	private Session getSession() {
		return em.unwrap(Session.class);
	}

	@Override
	public Optional<T> getById(K key) {
		return Optional.ofNullable(getSession().get(clazz, key));
	}

	@Override
	public void add(Optional<T> record) {
		getSession().save(record.orElseThrow());
	}

	@Override
	public void delete(Optional<T> record) {
		getSession().delete(record.orElseThrow());
	}

}
