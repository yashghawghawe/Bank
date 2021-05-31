package com.yash.demo.bank.exception;

public class TransactionFailedException extends Exception{
	
	private static final long serialVersionUID = 1L;
	String str;

	public TransactionFailedException() {

	}

	public TransactionFailedException(String str) {
		this.str = str;
	}

	public String toString() {
		return str;
	}

}
