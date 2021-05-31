package com.yash.demo.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yash.demo.bank.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

	Account findByAccountNo(String accountNumber);

	@Modifying
	@Query("update Account a set a.balance =:newBalance where a.accountNo=:accountNo")
	int updateAccount(@Param("newBalance") double newBalance,@Param("accountNo") String accountNo);

}
