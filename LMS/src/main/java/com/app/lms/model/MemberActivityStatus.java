package com.app.lms.model;

import java.util.stream.Stream;

public enum MemberActivityStatus {

	ACTIVE(1), DORMANT(2), BLOCKED(3), INACTIVE(-1);

	private int statusCode;

	private MemberActivityStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public static MemberActivityStatus statusOf(int statusCode) {
		return Stream.of(MemberActivityStatus.values()).filter(c -> c.getStatusCode() == statusCode).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
