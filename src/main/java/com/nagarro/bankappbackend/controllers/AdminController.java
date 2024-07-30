package com.nagarro.bankappbackend.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.bankappbackend.dto.AccountDto;
import com.nagarro.bankappbackend.dto.UserDto;
import com.nagarro.bankappbackend.entity.Account;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.service.implementation.AdminServicesImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {
    
    private final AdminServicesImpl service;

    @PostMapping("/kyc")
    @PreAuthorize("hasAuthority('SUPERVISOR')")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto request){
        return ResponseEntity.ok(service.createAccount(request));
    }
    
    @DeleteMapping("/kyc")
    @PreAuthorize("hasAuthority('SUPERVISOR')")
    public ResponseEntity<Void> authenticate(@RequestBody User user) {
        service.deleteUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/get/allUsers")
    @PreAuthorize("hasAuthority('SUPERVISOR')")
    public List<UserDto> getUsers() {
		List<UserDto> users = service.getAllUsers();
		return users;
	}
}
