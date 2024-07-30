package com.nagarro.bankappbackend.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nagarro.bankappbackend.dto.AccountDto;
import com.nagarro.bankappbackend.dto.UserDto;
import com.nagarro.bankappbackend.entity.Account;
import com.nagarro.bankappbackend.entity.AccountType;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.repositories.AccountRepository;
import com.nagarro.bankappbackend.repositories.UserRepository;

public class AdminServicesImplTest {

	@InjectMocks
	private AdminServicesImpl adminServices;

	@Mock
	private UserRepository userRepo;

	@Mock
	private AccountRepository accountRepo;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testDeleteUser() {
		User user = new User();
		doNothing().when(userRepo).delete(any(User.class));

		adminServices.deleteUser(user);

		verify(userRepo).delete(user);
	}

	@Test
	public void testCreateAccount() {
		// Create DTO with test data
		AccountDto accountDto = new AccountDto();
		accountDto.setUserEmail("user@example.com");
		accountDto.setAccountType(AccountType.SAVINGS);

		// Create user entity with test data
		User user = new User();
		user.setEmail("user@example.com");

		// Create account entity with test data
		Account account = new Account();
		account.setAccountNumber(123456L);
		account.setAccountType(AccountType.SAVINGS);
		account.setBalance(10000.0);
		account.setUser(user);

		// Mock userRepo to return user entity when findByEmail is called
		when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
		// Mock accountRepo to return account entity when save is called
		when(accountRepo.save(any(Account.class))).thenAnswer(invocation -> {
			Account savedAccount = invocation.getArgument(0);
			savedAccount.setAccountNumber(123456L); // Ensure the account number is set correctly
			return savedAccount;
		});

		// Call the service method
		Account createdAccount = adminServices.createAccount(accountDto);

		// Verify interactions with the mocks
		verify(userRepo).findByEmail(accountDto.getUserEmail());
		verify(accountRepo).save(any(Account.class));

		// Assert the expected values
		assertEquals(123456L, createdAccount.getAccountNumber());
		assertEquals(AccountType.SAVINGS, createdAccount.getAccountType());
		assertEquals(10000.0, createdAccount.getBalance());
	}

	@Test
	public void testGetAllUsers() {
		UserDto userDto = new UserDto();
		// Assuming UserDto has a default constructor
		when(userRepo.findInvalidUsersAsDto()).thenReturn(List.of(userDto));

		List<UserDto> userDtos = adminServices.getAllUsers();

		verify(userRepo).findInvalidUsersAsDto();
		assertEquals(1, userDtos.size());
		assertEquals(userDto, userDtos.get(0));
	}

}
