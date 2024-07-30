package com.nagarro.bankappbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.bankappbackend.entity.Transaction;
import com.nagarro.bankappbackend.entity.User;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	List<Transaction> findByUser(User user);
}
