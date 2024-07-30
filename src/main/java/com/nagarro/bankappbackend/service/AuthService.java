package com.nagarro.bankappbackend.service;

import org.springframework.stereotype.Service;

import com.nagarro.bankappbackend.dto.LoginDto;
import com.nagarro.bankappbackend.dto.SignUpDto;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.response.AuthenticationResponse;


@Service
public interface AuthService 
{
	public User signUp(SignUpDto signupDetails);
	public AuthenticationResponse login(LoginDto logindetails);
	
}
