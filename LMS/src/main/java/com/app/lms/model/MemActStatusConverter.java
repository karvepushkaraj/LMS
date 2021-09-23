package com.app.lms.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
