package com.app.lms.model;

import java.util.stream.Stream;

public enum TransactionStatus {

	ACTIVE(1), EXPIRED(0);

	private int statusCode;

	private TransactionStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public static TransactionStatus statusOf(int statusCode) {
		return Stream.of(TransactionStatus.values()).filter(c -> c.getStatusCode() == statusCode).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
