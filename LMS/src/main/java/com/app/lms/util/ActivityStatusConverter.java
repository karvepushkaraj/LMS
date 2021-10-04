package com.app.lms.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.app.lms.model.ActivityStatus;

/**
 * Converts {@link TransactionStatus} value to its status code and back.
 * 
 * @author karve
 *
 */

@Converter(autoApply = true)
public class ActivityStatusConverter implements AttributeConverter<ActivityStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ActivityStatus status) {
		return status.getStatusCode();
	}

	@Override
	public ActivityStatus convertToEntityAttribute(Integer statusCode) {
		return ActivityStatus.statusOf(statusCode);
	}

}
