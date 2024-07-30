package com.nagarro.bankappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferDto 
{
	private String userEmail;
	private Long recipientId;
	private Long recipientAccountId;
	private double amount;
}
