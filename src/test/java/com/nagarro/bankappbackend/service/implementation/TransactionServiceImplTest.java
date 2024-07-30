package com.nagarro.bankappbackend.service.implementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nagarro.bankappbackend.dto.MoneyTransferDto;
import com.nagarro.bankappbackend.entity.Account;
import com.nagarro.bankappbackend.entity.Transaction;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.exception.InsufficientBalanceException;
import com.nagarro.bankappbackend.exception.UserNotFoundException;
import com.nagarro.bankappbackend.repositories.AccountRepository;
import com.nagarro.bankappbackend.repositories.TransactionRepository;
import com.nagarro.bankappbackend.repositories.UserRepository;
import com.nagarro.bankappbackend.response.TransactionResponse;

public class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private TransactionRepository transactionRepo;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testWithdrawalMoney_UserNotFound() {
        String userEmail = "user@example.com";
        double amount = 100.0;

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            transactionService.withdrawalMoney(userEmail, amount);
        });

        verify(userRepo).findByEmail(userEmail);
        verify(accountRepo, never()).findByUser(any(User.class));
    }

    @Test
    public void testWithdrawalMoney_Success() {
        String userEmail = "user@example.com";
        double amount = 100.0;

        User user = new User();
        user.setEmail(userEmail);

        Account account = new Account();
        account.setBalance(200.0);
        account.setUser(user);

        Transaction transaction = new Transaction();
        transaction.setId(1L); // Set a valid transaction ID

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(accountRepo.findByUser(any(User.class))).thenReturn(Optional.of(account));
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L); // Ensure the transaction ID is set correctly
            return savedTransaction;
        });

        TransactionResponse response = transactionService.withdrawalMoney(userEmail, amount);

        verify(userRepo).findByEmail(userEmail);
        verify(accountRepo).findByUser(user);
        verify(accountRepo).save(account);
        verify(transactionRepo).save(any(Transaction.class));

        assertTrue(response.isSuccess());
        assertEquals("Money deducted successfully. Your transaction id is 1", response.getMessage());
        assertEquals(100.0, account.getBalance());
    }

    @Test
    public void testDepositMoney_Success() throws AccountNotFoundException {
        String userEmail = "user@example.com";
        double amount = 100.0;

        User user = new User();
        user.setEmail(userEmail);

        Account account = new Account();
        account.setBalance(200.0);
        account.setUser(user);

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(accountRepo.findByUser(any(User.class))).thenReturn(Optional.of(account));
        when(accountRepo.save(any(Account.class))).thenReturn(account);
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            transaction.setId(1L);  // Setting transaction id to 1
            return transaction;
        });

        TransactionResponse response = transactionService.depositMoney(userEmail, amount);

        verify(userRepo).findByEmail(userEmail);
        verify(accountRepo).findByUser(user);
        verify(accountRepo).save(account);
        verify(transactionRepo).save(any(Transaction.class));

        assertTrue(response.isSuccess());
        assertEquals("Money credited to account successfully. Your transaction id is 1", response.getMessage());
        assertEquals(300.0, account.getBalance());
    }

    @Test
    public void testDepositMoney_UserNotFound() {
        String userEmail = "user@example.com";
        double amount = 100.0;

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            transactionService.depositMoney(userEmail, amount);
        });

        verify(userRepo).findByEmail(userEmail);
        verify(accountRepo, never()).findByUser(any(User.class));
    }

    @Test
    public void testDepositMoney_AccountNotFound() {
        String userEmail = "user@example.com";
        double amount = 100.0;

        User user = new User();
        user.setEmail(userEmail);

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(accountRepo.findByUser(any(User.class))).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.depositMoney(userEmail, amount);
        });

        verify(userRepo).findByEmail(userEmail);
        verify(accountRepo).findByUser(user);
    }
    @Test
    public void testTransferMoney_Success() throws AccountNotFoundException {
        MoneyTransferDto transferDto = new MoneyTransferDto();
        transferDto.setUserEmail("userFrom@example.com");
        transferDto.setRecipientId(2L);
        transferDto.setRecipientAccountId(3L);
        transferDto.setAmount(100.0);

        User userFrom = new User();
        userFrom.setEmail(transferDto.getUserEmail());
        userFrom.setUserId(1L);

        Account accountFrom = new Account();
        accountFrom.setUser(userFrom);
        accountFrom.setBalance(200.0);

        User userTo = new User();
        userTo.setUserId(transferDto.getRecipientId());

        Account accountTo = new Account();
        accountTo.setUser(userTo);
        accountTo.setBalance(300.0);
        accountTo.setAccountNumber(transferDto.getRecipientAccountId());

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(userFrom));
        when(accountRepo.findByUser_UserId(anyLong())).thenReturn(Optional.of(accountFrom));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(userTo));
        when(accountRepo.findById(anyLong())).thenReturn(Optional.of(accountTo));
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L); // Ensure the transaction ID is set correctly
            return savedTransaction;
        });

        TransactionResponse response = transactionService.transferMoney(transferDto);

        verify(userRepo).findByEmail(transferDto.getUserEmail());
        verify(accountRepo).findByUser_UserId(userFrom.getUserId());
        verify(userRepo).findById(transferDto.getRecipientId());
        verify(accountRepo).findById(transferDto.getRecipientAccountId());
        verify(accountRepo).save(accountFrom);
        verify(accountRepo).save(accountTo);
        verify(transactionRepo, times(2)).save(any(Transaction.class));

        assertTrue(response.isSuccess());
        assertEquals("Money transferred successfully from account " + userFrom.getUserId()
                + " to account " + transferDto.getRecipientAccountId() + ". Your transaction ID is 1", response.getMessage());
        assertEquals(100.0, accountFrom.getBalance());
        assertEquals(400.0, accountTo.getBalance());
    }

    @Test
    public void testTransferMoney_InsufficientBalance() throws AccountNotFoundException {
        MoneyTransferDto transferDto = new MoneyTransferDto();
        transferDto.setUserEmail("userFrom@example.com");
        transferDto.setRecipientId(2L);
        transferDto.setRecipientAccountId(3L);
        transferDto.setAmount(300.0);

        User userFrom = new User();
        userFrom.setEmail(transferDto.getUserEmail());
        userFrom.setUserId(1L);

        Account accountFrom = new Account();
        accountFrom.setUser(userFrom);
        accountFrom.setBalance(200.0);

        User userTo = new User();
        userTo.setUserId(transferDto.getRecipientId());

        Account accountTo = new Account();
        accountTo.setUser(userTo);
        accountTo.setBalance(300.0);
        accountTo.setAccountNumber(transferDto.getRecipientAccountId());

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(userFrom));
        when(accountRepo.findByUser_UserId(anyLong())).thenReturn(Optional.of(accountFrom));
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(userTo));
        when(accountRepo.findById(anyLong())).thenReturn(Optional.of(accountTo));

        assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.transferMoney(transferDto);
        });

        verify(userRepo).findByEmail(transferDto.getUserEmail());
        verify(accountRepo).findByUser_UserId(userFrom.getUserId());
        verify(userRepo).findById(transferDto.getRecipientId());
        verify(accountRepo).findById(transferDto.getRecipientAccountId());
        verify(accountRepo, never()).save(accountFrom);
        verify(accountRepo, never()).save(accountTo);
        verify(transactionRepo, never()).save(any(Transaction.class));
    }
}
