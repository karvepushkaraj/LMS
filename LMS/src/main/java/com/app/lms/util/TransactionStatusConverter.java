package com.app.lms.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.app.lms.model.TransactionStatus;

/**
 * Converts {@link TransactionStatus} value to its status code and back.
 * @author karve
 *
 */

@Converter(autoApply = true)
public class TransactionStatusConverter implements AttributeConverter<TransactionStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(TransactionStatus status) {
		return status.getStatusCode();
	}

	@Override
	public TransactionStatus convertToEntityAttribute(Integer statusCode) {
		return TransactionStatus.statusOf(statusCode);
	}

}
