package com.app.lms.model;

import java.util.stream.Stream;

/**
 * Activity Status Enum
 * 
 * @author karve
 *
 */

public enum ActivityStatus {

	ACTIVE(1), EXPIRED(0);

	private int statusCode;

	private ActivityStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public static ActivityStatus statusOf(int statusCode) {
		return Stream.of(ActivityStatus.values()).filter(c -> c.getStatusCode() == statusCode).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
