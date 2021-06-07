package com.yash.demo.bank.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.yash.demo.bank.dto.AccountDTO;
import com.yash.demo.bank.entity.Account;
import com.yash.demo.bank.exception.TransactionFailedException;
import com.yash.demo.bank.repository.AccountRepository;

/**
 * @author yash.ghawghawe
 *
 */
@SpringBootTest
public class AccountServiceImplTest {

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountServiceImpl accountServiceImpl;

	private static Account account;

	@BeforeAll
	public static void setUp() {
		account = new Account();
		account.setAccountNo("12345678");
		account.setBalance(100000);
		account.setUserId(1);
	}

	@Test
	@DisplayName("Positive Scenario : Find Account")
	public void findByAccountNoTest() throws TransactionFailedException {
		// given
		when(accountRepository.findByAccountNo("12345678")).thenReturn(account);

		// when
		AccountDTO account = accountServiceImpl.findByAccountNo("12345678");

		// then
		verify(accountRepository).findByAccountNo("12345678");
		assertEquals(account, account);
	}

	@Test
	@DisplayName("Negative Scenario : Find Account")
	public void findByAccountNoFailedTest() throws TransactionFailedException {
		// given
		when(accountRepository.findByAccountNo("12345678")).thenReturn(null);
	
		AccountDTO accountDTO=accountServiceImpl.findByAccountNo("12345678");
		// then
		assertNull(accountDTO);
	}

	@Test
	@DisplayName("Positive Scenario : Update Account")
	public void updateAccountTest() {
		// given
		when(accountRepository.updateAccount(90000, "12345678")).thenReturn(1);

		// when
		int account = accountServiceImpl.updateAccount(90000, "12345678");

		// then
		verify(accountRepository).updateAccount(90000, "12345678");
		assertEquals(1, account);
	}

}
