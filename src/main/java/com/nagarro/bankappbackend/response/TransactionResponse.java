package com.nagarro.bankappbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class TransactionResponse 
{
	private boolean success;
    private String message;

    public TransactionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
