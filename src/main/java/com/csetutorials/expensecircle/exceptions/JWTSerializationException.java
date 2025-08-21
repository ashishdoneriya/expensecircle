package com.csetutorials.expensecircle.exceptions;

public class JWTSerializationException extends RuntimeException {

	public JWTSerializationException(Exception e) {
		super(e);
	}

}
