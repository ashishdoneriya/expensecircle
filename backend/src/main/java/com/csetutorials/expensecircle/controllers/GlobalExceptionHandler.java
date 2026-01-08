package com.csetutorials.expensecircle.controllers;

import com.csetutorials.expensecircle.exceptions.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UnauthorizedAccessException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<String> handleResponseStatusException(ResponseStatusException e) {
		// This preserves the status code you manually set (like 404 or 400)
		return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<String> handleGenericException(Exception e) {
		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body("An unexpected error occurred on our end.");
	}

}