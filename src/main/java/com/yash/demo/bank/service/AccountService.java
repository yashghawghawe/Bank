package com.yash.demo.bank.service;

import com.yash.demo.bank.dto.AccountDTO;

public interface AccountService {

	AccountDTO findByAccountNo(String accountNumber);

	int updateAccount(double newBalance, String accountNo);

}
