package com.nagarro.bankappbackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.bankappbackend.entity.Account;
import com.nagarro.bankappbackend.entity.User;

public interface AccountRepository extends JpaRepository<Account, Long>{

	Optional<Account> findByUser(User user);
	
	 Optional<Account> findByUser_UserId(Long userId);
}
