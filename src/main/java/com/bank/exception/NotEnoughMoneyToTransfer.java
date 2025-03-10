package com.bank.exception;

public class NotEnoughMoneyToTransfer extends RuntimeException {

	private static final long serialVersionUID = -7419592720454654335L;

	public NotEnoughMoneyToTransfer(String message) {
		super(message);
	}

}
