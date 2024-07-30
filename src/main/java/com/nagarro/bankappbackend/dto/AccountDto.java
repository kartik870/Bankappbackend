package com.nagarro.bankappbackend.dto;

import com.nagarro.bankappbackend.entity.AccountType;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

	String userEmail;
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
}
