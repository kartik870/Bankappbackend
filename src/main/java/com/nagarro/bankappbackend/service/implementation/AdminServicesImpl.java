package com.nagarro.bankappbackend.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.bankappbackend.dto.AccountDto;
import com.nagarro.bankappbackend.dto.UserDto;
import com.nagarro.bankappbackend.entity.Account;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.repositories.AccountRepository;
import com.nagarro.bankappbackend.repositories.UserRepository;
import com.nagarro.bankappbackend.service.AdminServices;

@Service
public class AdminServicesImpl implements AdminServices{

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AccountRepository accountRepo;
	
	
	@Override
	public void deleteUser(User user) {
		userRepo.delete(user);
	}

	
	@Override
	public Account createAccount(AccountDto accountEntity) {
		
		User user = userRepo.findByEmail(accountEntity.getUserEmail()).get();
		user.setValid(true);

		userRepo.save(user);
		
		Account account = new Account();
		account.setAccountNumber(generateUniqueSixDigitId());
		account.setAccountType(accountEntity.getAccountType());
		account.setBalance(10000.0d);
		account.setUser(user);
		
		accountRepo.save(account);
		return account;
	}
	
	
	public List<UserDto> getAllUsers()
	{
		return userRepo.findInvalidUsersAsDto();
	}
	
	
	    public static long generateUniqueSixDigitId() {
	        UUID uuid = UUID.randomUUID();
	        long longValue = Math.abs(uuid.getMostSignificantBits());
	        long sixDigitId = longValue % 1000000;
	        return sixDigitId;
	    }

}
