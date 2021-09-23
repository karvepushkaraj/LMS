package com.app.lms.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
