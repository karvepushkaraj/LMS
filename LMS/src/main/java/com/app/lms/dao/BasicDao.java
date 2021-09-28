package com.app.lms.dao;

import java.io.Serializable;

public interface BasicDao<T, K extends Serializable> {

	void setClazz(Class<T> clazz);

	T getById(K key);

	void add(T record);

	void delete(T record);

}
