package com.nagarro.bankappbackend.dto;

import java.util.Date;

import com.nagarro.bankappbackend.entity.TransactionType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	private double amount;
	private Date transactionDate;
}
