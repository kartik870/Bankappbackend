package com.nagarro.bankappbackend.service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.stereotype.Service;

import com.nagarro.bankappbackend.dto.MoneyTransferDto;
import com.nagarro.bankappbackend.response.TransactionResponse;

@Service
public interface TransactionServices {
	TransactionResponse withdrawalMoney(String userEmail, double amount);

	TransactionResponse depositMoney(String userEmail, double amount) throws AccountNotFoundException;

	TransactionResponse  transferMoney(MoneyTransferDto transfer) throws AccountNotFoundException;

}
