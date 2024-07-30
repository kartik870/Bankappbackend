package com.nagarro.bankappbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.nagarro.bankappbackend.response.TransactionResponse;

@ControllerAdvice
public class GlobalTransactionExceptionHandler {
	public ResponseEntity<TransactionResponse> handleException(Exception ex) {
		TransactionResponse response = new TransactionResponse(false, ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
