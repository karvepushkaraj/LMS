package com.app.lms.util;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.hibernate.HibernateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Custom Exception handler class.
 * 
 * @author karve
 *
 */

@ControllerAdvice
public class LMSExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handles {@link MethodArgumentNotValidException}
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorMessage error = new ErrorMessage(new Date(System.currentTimeMillis()), status,
				ex.getBindingResult().getFieldError().getDefaultMessage());
		return handleExceptionInternal(ex, error, headers, status, request);
	}

	/**
	 * Handles {@link HttpMessageNotReadableException}
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorMessage error = new ErrorMessage(new Date(System.currentTimeMillis()), status, ex.getMessage());
		return handleExceptionInternal(ex, error, headers, status, request);
	}

	/**
	 * Handles {@link IllegalRequestException}
	 */
	@ExceptionHandler(IllegalRequestException.class)
	protected ResponseEntity<Object> handleIllegalRequest(IllegalRequestException ex, WebRequest request) {
		ErrorMessage error = new ErrorMessage(new Date(System.currentTimeMillis()), HttpStatus.BAD_REQUEST,
				ex.getMessage());
		return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
	}

	/**
	 * Handles {@link HibernateException}
	 */
	@ExceptionHandler(HibernateException.class)
	protected ResponseEntity<Object> handleHibernateException(HibernateException ex, WebRequest request) {
		StringBuilder sb = new StringBuilder(ex.getMessage());
		Throwable t = ex.getCause();
		if (t != null)
			sb.append(t.getMessage());
		ErrorMessage error = new ErrorMessage(new Date(System.currentTimeMillis()), HttpStatus.BAD_REQUEST,
				sb.toString());
		return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
	}

	/**
	 * Handles {@link ConstraintViolationException}
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		ErrorMessage error = new ErrorMessage(new Date(System.currentTimeMillis()), HttpStatus.BAD_REQUEST,
				ex.getMessage());
		return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
	}

}
