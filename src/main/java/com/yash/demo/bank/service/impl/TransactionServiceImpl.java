package com.yash.demo.bank.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.yash.demo.bank.dto.AccountDTO;
import com.yash.demo.bank.dto.TransactionDTO;
import com.yash.demo.bank.entity.Transaction;
import com.yash.demo.bank.exception.InsufficientBalanceException;
import com.yash.demo.bank.exception.TransactionFailedException;
import com.yash.demo.bank.repository.TransactionRepository;
import com.yash.demo.bank.service.AccountService;
import com.yash.demo.bank.service.TransactionService;

/**
 * @author yash.ghawghawe
 *
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountService accountService;

	/**
	 * @see com.yash.demo.bank.service.TransactionService#transferFund(com.yash.demo.bank.dto.TransactionDTO)
	 */
	@Override
	public String transferFund(@Valid TransactionDTO transactionDTO)
			throws InsufficientBalanceException, TransactionFailedException {

		if (transactionDTO.getAmount() == 0) {
			throw new TransactionFailedException();
		}
		AccountDTO account = accountService.findByAccountNo(transactionDTO.getAccountNumber());
		if (ObjectUtils.isEmpty(account)) {
			throw new TransactionFailedException();
		}
		if (account.getBalance() < transactionDTO.getAmount()) {
			throw new InsufficientBalanceException("Balance is low For the Transaction");
		}

		Transaction debitTranasaction = new Transaction();
		debitTranasaction.setAccountNo(transactionDTO.getAccountNumber());
		debitTranasaction.setAccountnoben(transactionDTO.getBeneficiaryAccountNumber());
		debitTranasaction.setAmount(transactionDTO.getAmount());
		debitTranasaction.setType("DEBIT");
		debitTranasaction.setDescription("fastkartTransfer");
		Date debitdate = new Date();
		java.sql.Date sqldebitDate = new java.sql.Date(debitdate.getTime());
		debitTranasaction.setDate(sqldebitDate);
		debitTranasaction.setUserid(account.getUserId());

		double newBalance = account.getBalance() - transactionDTO.getAmount();
		accountService.updateAccount(newBalance, account.getAccountNo());
		transactionRepository.save(debitTranasaction);

		AccountDTO accountben = accountService.findByAccountNo(transactionDTO.getBeneficiaryAccountNumber());
		if (accountben == null) {
			logger.info("the benficiary belongs to other bank : " + transactionDTO.getBeneficiaryAccountNumber());
			throw new TransactionFailedException();
		} else {
			double newBalanceBen = accountben.getBalance() + transactionDTO.getAmount();
			accountService.updateAccount(newBalanceBen, accountben.getAccountNo());
			Transaction creditTransaction = new Transaction();
			creditTransaction.setAccountNo(accountben.getAccountNo());
			creditTransaction.setAccountnoben(debitTranasaction.getAccountNo());
			creditTransaction.setAmount(debitTranasaction.getAmount());
			creditTransaction.setDescription(debitTranasaction.getDescription());
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			creditTransaction.setDate(sqlDate);
			creditTransaction.setType("CREDIT");
			creditTransaction.setDescription("fastkartTransfer");
			creditTransaction.setUserid(accountben.getUserId());
			transactionRepository.save(creditTransaction);
		}
		return "transaction successful";
	}

}
