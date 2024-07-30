package com.nagarro.bankappbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nagarro.bankappbackend.dto.TransactionDto;
import com.nagarro.bankappbackend.dto.UserDto;
import com.nagarro.bankappbackend.dto.UserEditDto;
import com.nagarro.bankappbackend.entity.Transaction;
import com.nagarro.bankappbackend.entity.User;

@Service
public interface UserServices {
	UserDto updateUser(UserEditDto entity, String email);
	UserEditDto getUserByEmail(String email);
	public List<TransactionDto> getUserStatement(String email) ;
}
