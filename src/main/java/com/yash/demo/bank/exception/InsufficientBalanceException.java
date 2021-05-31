package com.yash.demo.bank.exception;

public class InsufficientBalanceException extends Exception {

	private static final long serialVersionUID = 1L;
	String str;

	public InsufficientBalanceException() {

	}

	public InsufficientBalanceException(String str) {
		this.str = str;
	}

	public String toString() {
		return str;
	}

}
