package com.yash.demo.bank.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author yash.ghawghawe
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

	@NotEmpty(message = "accountNumber cannot be empty")
	private String accountNumber;

	@NotEmpty(message = "benficiaryAccountNumber cannot be empty")
	private String beneficiaryAccountNumber;
	
	private double amount;
}
