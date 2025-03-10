package com.bank.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8032888743313142174L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
