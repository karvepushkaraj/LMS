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
	 * Handles {@link HibernateException}, {@link IllegalRequestException},
	 * {@link ConstraintViolationException}
	 */
	@ExceptionHandler({ HibernateException.class, IllegalRequestException.class, ConstraintViolationException.class })
	protected ResponseEntity<ErrorMessage> handleHibernateException(Exception ex, WebRequest request) {
		StringBuilder sb = new StringBuilder(ex.getMessage());
		Throwable t = ex.getCause();
		if (t != null)
			sb.append(t.getMessage());
		ErrorMessage error = new ErrorMessage(new Date(System.currentTimeMillis()), HttpStatus.BAD_REQUEST,
				sb.toString());
		return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
	}

}
