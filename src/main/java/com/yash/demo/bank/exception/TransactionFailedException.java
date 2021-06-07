package com.yash.demo.bank.exception;

/**
 * @author yash.ghawghawe
 *
 */
public class TransactionFailedException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String str;

	public TransactionFailedException(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return str;
	}

}
