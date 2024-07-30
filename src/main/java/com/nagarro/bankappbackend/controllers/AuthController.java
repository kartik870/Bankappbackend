package com.nagarro.bankappbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.bankappbackend.dto.LoginDto;
import com.nagarro.bankappbackend.dto.SignUpDto;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.response.AuthenticationResponse;
import com.nagarro.bankappbackend.service.implementation.AuthServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController 
{
	
	@Autowired
	private final AuthServiceImpl service;
	
	@PostMapping("/registeration")
	public ResponseEntity<User> register(@RequestBody SignUpDto request)
	{
		return ResponseEntity.ok(service.signUp(request));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginDto request)
	{
		return ResponseEntity.ok(service.login(request));
	}
}
