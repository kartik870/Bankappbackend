package com.nagarro.bankappbackend.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.bankappbackend.dto.TransactionDto;
import com.nagarro.bankappbackend.dto.UserDto;
import com.nagarro.bankappbackend.dto.UserEditDto;
import com.nagarro.bankappbackend.entity.Transaction;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.repositories.TransactionRepository;
import com.nagarro.bankappbackend.repositories.UserRepository;
import com.nagarro.bankappbackend.service.UserServices;


@Service
public class UserServiceImpl implements UserServices{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	@Override
	public UserDto updateUser(UserEditDto request, String email) {
	    User user = userRepo.findByEmail(email)
	        .orElseThrow(() -> new RuntimeException("User not found"));
	    user.setAge(request.getAge());
	    user.setDateOfBirth(request.getDateOfBirth());
	    user.setProfilePhoto(request.getProfilePhoto());
	    user.setEmail(request.getEmail());
	    
	    userRepo.save(user);
	    return convertUserToDto(user);
	    
	}
	
	@Override
	public UserEditDto getUserByEmail(String email)
	{
		User user= userRepo.findByEmail(email).get();
		return convertToDto(user);
	}

	
	@Override 
	public List<TransactionDto> getUserStatement(String email) {
		System.out.println(email);
		Optional<User> user = userRepo.findByEmail(email);
		List<Transaction> list =transactionRepo.findByUser(user.get());
		return transactionToDto(list);
		
	}
	private List<TransactionDto> transactionToDto(List<Transaction> list) {
	    return list.stream()
	               .map(transaction -> new TransactionDto(transaction.getId(), transaction.getType(),
	                                                       transaction.getAmount(), transaction.getTransactionDate()))
	               .collect(Collectors.toList());
	}
	

	private UserDto convertUserToDto(User user)
	{
		UserDto dto = new UserDto();
		dto.setEmail(user.getEmail());
		dto.setUserId(user.getUserId());
		dto.setFullName(user.getFirstName()+" "+user.getLastName());
		return dto;
	}
	
	private UserEditDto convertToDto(User user)
	{
		UserEditDto dto = new UserEditDto();
		dto.setEmail(user.getEmail());
		dto.setAge(user.getAge());
		dto.setDateOfBirth(user.getDateOfBirth());
		dto.setProfilePhoto(user.getProfilePhoto());
		dto.setValid(user.isValid());
		return dto;
	}

}
