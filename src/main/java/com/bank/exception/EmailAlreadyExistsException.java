package com.bank.exception;

public class EmailAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -7207266004142298628L;

	public EmailAlreadyExistsException(String message) {
		super(message);

	}

}
