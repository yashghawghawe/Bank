package com.yash.demo.bank.service.impl;

import java.util.Date;

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
	private static final String TRANSACTION_FAILED = "Transaction Failed";

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
		if (Double.toString(transactionDTO.getAmount()).equals("0.0")) {
			logger.info(TRANSACTION_FAILED);
			throw new TransactionFailedException(TRANSACTION_FAILED);
		}
		AccountDTO account = accountService.findByAccountNo(transactionDTO.getAccountNumber());
		if (ObjectUtils.isEmpty(account)) {
			logger.info(TRANSACTION_FAILED);
			throw new TransactionFailedException(TRANSACTION_FAILED);
		}
		if (account.getBalance() < transactionDTO.getAmount()) {
			throw new InsufficientBalanceException("Balance is low For the Transaction");
		}

		Transaction debitTranasaction = new Transaction(transaction -> {
			transaction.setAccountNo(transactionDTO.getAccountNumber());
			transaction.setAccountnoben(transactionDTO.getBeneficiaryAccountNumber());
			transaction.setAmount(transactionDTO.getAmount());
			transaction.setType("DEBIT");
			transaction.setDescription("fastkartTransfer");
			Date debitdate = new Date();
			java.sql.Date sqldebitDate = new java.sql.Date(debitdate.getTime());
			transaction.setDate(sqldebitDate);
			transaction.setUserid(account.getUserId());
		});

		double newBalance = account.getBalance() - transactionDTO.getAmount();
		accountService.updateAccount(newBalance, account.getAccountNo());
		transactionRepository.save(debitTranasaction);

		AccountDTO accountben = accountService.findByAccountNo(transactionDTO.getBeneficiaryAccountNumber());
		if (accountben == null) {
			logger.info("the benficiary belongs to other bank : " + transactionDTO.getBeneficiaryAccountNumber());
			throw new TransactionFailedException(TRANSACTION_FAILED);
		} else {
			double newBalanceBen = accountben.getBalance() + transactionDTO.getAmount();
			accountService.updateAccount(newBalanceBen, accountben.getAccountNo());
			Transaction creditTransaction = new Transaction(transaction -> {
				transaction.setAccountNo(accountben.getAccountNo());
				transaction.setAccountnoben(debitTranasaction.getAccountNo());
				transaction.setAmount(debitTranasaction.getAmount());
				transaction.setDescription(debitTranasaction.getDescription());
				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				transaction.setDate(sqlDate);
				transaction.setType("CREDIT");
				transaction.setDescription("fastkartTransfer");
				transaction.setUserid(accountben.getUserId());
			});
			transactionRepository.save(creditTransaction);
		}
		return "transaction successful";
	}

}
