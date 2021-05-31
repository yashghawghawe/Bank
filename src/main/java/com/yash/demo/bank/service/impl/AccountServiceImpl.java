package com.yash.demo.bank.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.yash.demo.bank.dto.AccountDTO;
import com.yash.demo.bank.entity.Account;
import com.yash.demo.bank.repository.AccountRepository;
import com.yash.demo.bank.service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public AccountDTO findByAccountNo(String accountNumber) {
		Account account = accountRepository.findByAccountNo(accountNumber);
		if (ObjectUtils.isEmpty(account)) {
			return null;
		}
		AccountDTO accountDTO = new AccountDTO();
		BeanUtils.copyProperties(account, accountDTO);
		return accountDTO;
	}

	@Override
	public int updateAccount(double newBalance, String accountNo) {
		return accountRepository.updateAccount(newBalance,accountNo);
	}

}
