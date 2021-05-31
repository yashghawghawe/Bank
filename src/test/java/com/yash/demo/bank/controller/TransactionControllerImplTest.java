package com.yash.demo.bank.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.yash.demo.bank.dto.TransactionDTO;
import com.yash.demo.bank.exception.InsufficientBalanceException;
import com.yash.demo.bank.exception.TransactionFailedException;
import com.yash.demo.bank.service.TransactionService;

@SpringBootTest
public class TransactionControllerImplTest {

	@Mock
	private TransactionService transactionService;

	@InjectMocks
	private TransactionController transactionController;

	private static TransactionDTO transactionDTO;

	@BeforeAll
	public static void setUp() {
		transactionDTO = new TransactionDTO("12345678", "11223344", 10000);
	}

	@Test
	@DisplayName("Positive Scenario : Fund transfered")
	public void fundTransferTest() throws InsufficientBalanceException, TransactionFailedException {
		// given
		when(transactionService.transferFund(transactionDTO)).thenReturn("Transaction Successful!");

		// when
		ResponseEntity<String> message = transactionController.transferFund(transactionDTO);

		// then
		verify(transactionService).transferFund(transactionDTO);
		assertEquals("Transaction Successful!", message.getBody());
		assertEquals(HttpStatus.OK, message.getStatusCode());
	}

	@Test
	@DisplayName("Negative Scenario : Fund transfer failed")
	public void fundTransferFailedTest() throws InsufficientBalanceException, TransactionFailedException {
		// given
		when(transactionService.transferFund(transactionDTO)).thenThrow(TransactionFailedException.class);

		assertThrows(TransactionFailedException.class, () -> transactionController.transferFund(transactionDTO));
	}

	@Test
	@DisplayName("Negative Scenario : Fund transfer failed")
	public void fundTransferFailedForInsufficientBalanceExceptionTest()
			throws InsufficientBalanceException, TransactionFailedException {
		// given
		when(transactionService.transferFund(transactionDTO)).thenThrow(InsufficientBalanceException.class);

		assertThrows(InsufficientBalanceException.class, () -> transactionController.transferFund(transactionDTO));
	}

}
