package com.yash.demo.bank.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yash.ghawghawe
 *
 */
@Getter
@Setter
@ToString
public class AccountDTO {

	private String accountNo;
	private int userId;
	private double balance;

}
