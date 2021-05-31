package com.yash.demo.bank.service;

import javax.validation.Valid;

import com.yash.demo.bank.dto.TransactionDTO;
import com.yash.demo.bank.exception.InsufficientBalanceException;
import com.yash.demo.bank.exception.TransactionFailedException;

public interface TransactionService {

	String transferFund(@Valid TransactionDTO transactionDTO)
			throws InsufficientBalanceException, TransactionFailedException;

}
