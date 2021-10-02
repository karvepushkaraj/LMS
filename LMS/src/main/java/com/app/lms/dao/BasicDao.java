package com.app.lms.dao;

import java.io.Serializable;
import java.util.Optional;

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
	Optional<T> getById(K key);

	/**
	 * Add new record of model.
	 * @param record
	 */
	void add(Optional<T> record);

	/**
	 * Delete existing record of model.
	 * @param record
	 */
	void delete(Optional<T> record);

}
