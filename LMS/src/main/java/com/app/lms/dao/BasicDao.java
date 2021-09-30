package com.app.lms.dao;

import java.io.Serializable;

/**
 * Generic Dao interface for basic CRUD operations.
 * @author karve
 *
 * @param <T> model
 * @param <K> key
 */
public interface BasicDao<T, K extends Serializable> {

	/**
	 * Set class member of the generic implementation class.
	 * @param clazz
	 */
	void setClazz(Class<T> clazz);

	/**
	 * Get model by its id.
	 * @param key
	 * @return
	 */
	T getById(K key);

	/**
	 * Add new record of model.
	 * @param record
	 */
	void add(T record);

	/**
	 * Delete existing record of model.
	 * @param record
	 */
	void delete(T record);

}
