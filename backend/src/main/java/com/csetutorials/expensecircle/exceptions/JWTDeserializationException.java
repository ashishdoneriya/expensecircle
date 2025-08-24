package com.csetutorials.expensecircle.exceptions;

public class JWTDeserializationException extends Exception {
	public JWTDeserializationException(Exception e) {
		super(e);
	}
}
