package com.yash.demo.bank.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yash.demo.bank.exception.InsufficientBalanceException;
import com.yash.demo.bank.exception.TransactionFailedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * @param e
	 * @return
	 */
	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<?> handleInsufficientBalanceException(InsufficientBalanceException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Balance is low for tranasction " + InsufficientBalanceException.class);
	}

	/**
	 * @param e
	 * @return
	 */
	@ExceptionHandler(TransactionFailedException.class)
	public ResponseEntity<?> handleTransactionFailedException(TransactionFailedException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("Transaction Failed " + TransactionFailedException.class);
	}

	/**
	 * @param MethodArgumentNotValidException
	 *            ex
	 * @return ResponseEntity
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

}
