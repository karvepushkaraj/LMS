package com.app.lms.util;

/**
 * Class for Invalid Business Condition Exception which is thrown by the Service
 * layer.
 * 
 * @author karve
 *
 */

public class InvalidBusinessCondition extends Exception {

	private static final long serialVersionUID = -6120877822536333613L;

	public InvalidBusinessCondition() {
		super();
	}

	public InvalidBusinessCondition(String message) {
		super(message);
	}

	public InvalidBusinessCondition(Throwable cause) {
		super(cause);
	}

	public InvalidBusinessCondition(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidBusinessCondition(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
