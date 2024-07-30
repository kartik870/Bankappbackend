package com.nagarro.bankappbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {

	@Id
	private Long accountNumber;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "userId")
	private User user;
	private double balance;

	@Enumerated(EnumType.STRING)
	private AccountType accountType;
}
