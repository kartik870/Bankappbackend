package com.nagarro.bankappbackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.bankappbackend.dto.TransactionDto;
import com.nagarro.bankappbackend.dto.UserDto;
import com.nagarro.bankappbackend.dto.UserEditDto;
import com.nagarro.bankappbackend.service.implementation.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
	@Autowired
	private final UserServiceImpl service;

	@PutMapping("/update/{email}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<UserDto> update(@RequestBody UserEditDto request, @PathVariable String email) {
		return ResponseEntity.ok(service.updateUser(request, email));
	}
	
	@GetMapping("/user-profile/{email}")
	@PreAuthorize("hasAuthority('USER')")
	public UserEditDto getUserProfileByEmail(@PathVariable String email) {
		return service.getUserByEmail(email);
	}
	
	@GetMapping("/get-statement/{email}")
	@PreAuthorize("hasAuthority('USER')")
	public List<TransactionDto>  getUserStatement(@PathVariable String email) {
		return service.getUserStatement(email);
		
	}

}
