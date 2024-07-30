package com.nagarro.bankappbackend.service;

import org.springframework.stereotype.Service;

import com.nagarro.bankappbackend.dto.AccountDto;
import com.nagarro.bankappbackend.entity.Account;
import com.nagarro.bankappbackend.entity.User;

@Service
public interface AdminServices {
	public void deleteUser(User user);
	Account createAccount(AccountDto accountEntity);
}
