package com.app.lms.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.app.lms.model.MemberActivityStatus;

/**
 * Converts {@link MemberActivityStatus} value to its status code and back.
 * @author karve
 *
 */

@Converter(autoApply = true)
public class MemActStatusConverter implements AttributeConverter<MemberActivityStatus, Integer> {

	@Override
	public Integer convertToDatabaseColumn(MemberActivityStatus status) {
		return status.getStatusCode();
	}

	@Override
	public MemberActivityStatus convertToEntityAttribute(Integer statusCode) {
		return MemberActivityStatus.statusOf(statusCode);
	}

}
