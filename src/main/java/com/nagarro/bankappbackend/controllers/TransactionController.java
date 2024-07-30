package com.nagarro.bankappbackend.controllers;

import com.nagarro.bankappbackend.dto.MoneyTransferDto;
import com.nagarro.bankappbackend.response.TransactionResponse;
import com.nagarro.bankappbackend.service.TransactionServices;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin("*")
public class TransactionController {

    private final TransactionServices transactionServices;

    @Autowired
    public TransactionController(TransactionServices transactionServices) {
        this.transactionServices = transactionServices;
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('USER')")
    public TransactionResponse depositMoney(@RequestParam String email, @RequestParam Double amount) throws AccountNotFoundException {
        System.out.println("Depositing amount: " + amount + " for email: " + email);
        return transactionServices.depositMoney(email, amount);
    }

    @PostMapping("/withdrawal")
    @PreAuthorize("hasAuthority('USER')")
    public TransactionResponse withdrawMoney(@RequestParam String email, @RequestParam Double amount) throws AccountNotFoundException {
        System.out.println("Withdrawing amount: " + amount + " for email: " + email);
        return transactionServices.withdrawalMoney(email, amount);
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<TransactionResponse> transferMoney(@RequestBody MoneyTransferDto transfer) throws AccountNotFoundException {
        TransactionResponse response = transactionServices.transferMoney(transfer);
        return ResponseEntity.ok(response);
    }
}

