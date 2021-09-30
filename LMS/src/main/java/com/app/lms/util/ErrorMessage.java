package com.app.lms.util;

import java.util.Date;

import org.springframework.http.HttpStatus;

/**
 * Class defining custom Error message.
 * @author karve
 *
 */
public class ErrorMessage {

	private Date timestamp;
	private HttpStatus status;
	private String message;

	public ErrorMessage() {
		super();
	}

	public ErrorMessage(Date timestamp, HttpStatus status, String message) {
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ErrorMessage [timestamp=" + timestamp + ", status=" + status + ", message=" + message + "]";
	}

}
