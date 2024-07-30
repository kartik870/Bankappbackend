package com.nagarro.bankappbackend.service.implementation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nagarro.bankappbackend.dto.TransactionDto;
import com.nagarro.bankappbackend.dto.UserDto;
import com.nagarro.bankappbackend.dto.UserEditDto;
import com.nagarro.bankappbackend.entity.Transaction;
import com.nagarro.bankappbackend.entity.TransactionType;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.repositories.TransactionRepository;
import com.nagarro.bankappbackend.repositories.UserRepository;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private TransactionRepository transactionRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateUser() {
        String email = "user@example.com";
        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setAge(30);
        userEditDto.setDateOfBirth(LocalDate.of(2000, 12, 1));
        userEditDto.setProfilePhoto("photoUrl");
        userEditDto.setEmail("newEmail@example.com");

        User user = new User();
        user.setEmail(email);

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserDto updatedUser = userService.updateUser(userEditDto, email);

        verify(userRepo).findByEmail(email);
        verify(userRepo).save(user);

        assertEquals(userEditDto.getEmail(), updatedUser.getEmail());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        String email = "user@example.com";
        UserEditDto userEditDto = new UserEditDto();

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(userEditDto, email));
    }

    @Test
    public void testGetUserByEmail() {
        String email = "user@example.com";

        User user = new User();
        user.setEmail(email);

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserEditDto userDto = userService.getUserByEmail(email);

        verify(userRepo).findByEmail(email);

        assertEquals(email, userDto.getEmail());
    }

    @Test
    public void testGetUserStatement() {
        String email = "user@example.com";

        User user = new User();
        user.setEmail(email);

        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setType(TransactionType.DEBIT);
        transaction1.setAmount(100.0);
        transaction1.setTransactionDate(new Date());

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setType(TransactionType.CREDIT);
        transaction2.setAmount(50.0);
        transaction2.setTransactionDate(new Date());

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(transactionRepo.findByUser(any(User.class))).thenReturn(transactions);

        List<TransactionDto> transactionDtos = userService.getUserStatement(email);

        verify(userRepo).findByEmail(email);
        verify(transactionRepo).findByUser(user);

        assertEquals(2, transactionDtos.size());
        assertEquals(transaction1.getId(), transactionDtos.get(0).getId());
        assertEquals(transaction2.getId(), transactionDtos.get(1).getId());
    }
}
