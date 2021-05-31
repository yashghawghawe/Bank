package com.yash.demo.bank.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yash.demo.bank.dto.TransactionDTO;
import com.yash.demo.bank.exception.InsufficientBalanceException;
import com.yash.demo.bank.exception.TransactionFailedException;
import com.yash.demo.bank.service.TransactionService;

/**
 * @author yash.ghawghawe
 *
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	/**
	 * @param transactionDTO
	 * @return ResponseEntity<String>
	 * @throws InsufficientBalanceException
	 * @throws TransactionFailedException
	 */
	@PostMapping
	public ResponseEntity<String> transferFund(@RequestBody @Valid TransactionDTO transactionDTO)
			throws InsufficientBalanceException, TransactionFailedException {
		String message = transactionService.transferFund(transactionDTO);
		return ResponseEntity.ok(message);

	}
}
