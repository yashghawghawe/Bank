package com.yash.demo.bank.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.yash.demo.bank.dto.AccountDTO;
import com.yash.demo.bank.dto.TransactionDTO;
import com.yash.demo.bank.entity.Transaction;
import com.yash.demo.bank.exception.InsufficientBalanceException;
import com.yash.demo.bank.exception.TransactionFailedException;
import com.yash.demo.bank.repository.TransactionRepository;
import com.yash.demo.bank.service.AccountService;

/**
 * @author yash.ghawghawe
 *
 */
@SpringBootTest
public class TransactionServiceImplTest {

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private AccountService accountService;

	@InjectMocks
	private TransactionServiceImpl transactionServiceImpl;

	private static AccountDTO accountDTO;
	private static AccountDTO accountDTOBen;
	private static TransactionDTO transactionDTO;
	private static Transaction debitTransaction;
	private static Transaction creditTransaction;

	@BeforeAll
	public static void setUp() {
		accountDTO = new AccountDTO(account -> {
			account.setAccountNo("12345678");
			account.setUserId(1);
			account.setBalance(100000);
		});

		accountDTOBen = new AccountDTO(account -> {
			account.setAccountNo("11223344");
			account.setUserId(2);
			account.setBalance(100000);
		});

		transactionDTO = new TransactionDTO("12345678", "11223344", 10000);

		debitTransaction = new Transaction(transaction -> {
			transaction.setAccountNo("12345678");
			transaction.setAccountnoben("11223344");
			transaction.setAmount(10000);
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			transaction.setDate(sqlDate);
			transaction.setDescription("fastkartTransfer");
			transaction.setType("DEBIT");
			transaction.setUserid(1);
		});

		creditTransaction = new Transaction(transaction -> {
			transaction.setAccountNo("11223344");
			transaction.setAccountnoben("12345678");
			transaction.setAmount(10000);
			Date date2 = new Date();
			java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());
			transaction.setDate(sqlDate2);
			transaction.setDescription("fastkartTransfer");
			transaction.setType("CREDIT");
			transaction.setUserid(2);
		});
	}

	/**
	 * @throws TransactionFailedException
	 * @throws InsufficientBalanceException
	 */
	@Test
	@DisplayName("Positive Scenario : Fund Transfer")
	public void fundTransferTest() throws TransactionFailedException, InsufficientBalanceException {
		// given
		when(accountService.findByAccountNo("12345678")).thenReturn(accountDTO);
		when(accountService.findByAccountNo("11223344")).thenReturn(accountDTOBen);
		when(accountService.updateAccount(10000, "12345678")).thenReturn(1);
		when(accountService.updateAccount(10000, "11223344")).thenReturn(1);
		when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(debitTransaction);
		when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(creditTransaction);

		// when
		String response = transactionServiceImpl.transferFund(transactionDTO);

		// then
		assertNotNull(response);
		assertEquals("transaction successful", response);
	}

	/**
	 * @throws TransactionFailedException
	 * @throws InsufficientBalanceException
	 */
	@Test
	@DisplayName("Negative Scenario : Fund Transfer")
	public void fundTransferFailedTest() throws TransactionFailedException, InsufficientBalanceException {
		// given
		when(accountService.findByAccountNo("12345678")).thenReturn(null);

		// when
		assertThrows(TransactionFailedException.class, () -> transactionServiceImpl.transferFund(transactionDTO));
	}

	/**
	 * @throws TransactionFailedException
	 * @throws InsufficientBalanceException
	 */
	@Test
	@DisplayName("Negative Scenario : Fund Transfer")
	public void fundTransferFailedForInsufficientBalanceExceptionTest()
			throws TransactionFailedException, InsufficientBalanceException {
		// given
		accountDTO.setBalance(8000);
		when(accountService.findByAccountNo("12345678")).thenReturn(accountDTO);

		// when
		assertThrows(InsufficientBalanceException.class, () -> transactionServiceImpl.transferFund(transactionDTO));
	}

}
