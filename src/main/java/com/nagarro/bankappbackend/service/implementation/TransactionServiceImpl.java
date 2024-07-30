package com.nagarro.bankappbackend.service.implementation;

import java.util.Date;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.nagarro.bankappbackend.dto.MoneyTransferDto;
import com.nagarro.bankappbackend.entity.Account;
import com.nagarro.bankappbackend.entity.Transaction;
import com.nagarro.bankappbackend.entity.TransactionType;
import com.nagarro.bankappbackend.entity.User;
import com.nagarro.bankappbackend.exception.InsufficientBalanceException;
import com.nagarro.bankappbackend.exception.UserNotFoundException;
import com.nagarro.bankappbackend.repositories.AccountRepository;
import com.nagarro.bankappbackend.repositories.TransactionRepository;
import com.nagarro.bankappbackend.repositories.UserRepository;
import com.nagarro.bankappbackend.response.TransactionResponse;
import com.nagarro.bankappbackend.service.TransactionServices;

@Service
public class TransactionServiceImpl implements TransactionServices {

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TransactionRepository transactionRepo;

	@Override
	public TransactionResponse withdrawalMoney(String userEmail, double amount) {
		System.out.println(userEmail);
		Optional<User> optionalUser = userRepo.findByEmail(userEmail);
		if (optionalUser.isEmpty()) {
			throw new UserNotFoundException("User not found with email: " + userEmail);
		}

		User user = optionalUser.get(); // Safe to call get() after checking isPresent()

		Optional<Account> optionalAccount = accountRepo.findByUser(user);

		if (optionalAccount.isEmpty()) {
			throw new UserNotFoundException("Account not found for user with email: " + userEmail);
		}

		Account account = optionalAccount.get(); // Safe to call get() after checking isPresent()

		// Update user's balance
		double newBalance = account.getBalance() - amount;
		account.setBalance(newBalance);

		// Save transaction
		Transaction transaction = new Transaction();
		transaction.setUser(user);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.DEBIT);
		transaction.setTransactionDate(new Date());

		accountRepo.save(account);
		transactionRepo.save(transaction);

		return new TransactionResponse(true,
				"Money deducted successfully. Your transaction id is " + transaction.getId());
	}



    @Override
    public TransactionResponse transferMoney(MoneyTransferDto transfer) throws AccountNotFoundException {

    	  // Find the sender
        User userFrom = userRepo.findByEmail(transfer.getUserEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + transfer.getUserEmail()));

        // Find the sender's account
        Account accountFrom = accountRepo.findByUser_UserId(userFrom.getUserId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found for user ID: " + userFrom.getUserId()));

    	
        // Find the recipient
        User userTo = userRepo.findById(transfer.getRecipientId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + transfer.getRecipientId()));

        // Find the recipient's account
        Account accountTo = accountRepo.findById(transfer.getRecipientAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with Account number: " + transfer.getRecipientAccountId()));

        // Check if the sender has sufficient balance
        if (accountFrom.getBalance() < transfer.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        // Update sender's balance
        double newBalancePayer = accountFrom.getBalance() - transfer.getAmount();
        accountFrom.setBalance(newBalancePayer);

        // Update receiver's balance
        double newBalanceReceiver = accountTo.getBalance() + transfer.getAmount();
        accountTo.setBalance(newBalanceReceiver);

        // Save sender's transaction
        Transaction senderTransaction = new Transaction();
        senderTransaction.setUser(userFrom);
        senderTransaction.setAmount(transfer.getAmount());
        senderTransaction.setType(TransactionType.DEBIT);
        senderTransaction.setTransactionDate(new Date());

        // Save receiver's transaction
        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setUser(userTo);
        receiverTransaction.setAmount(transfer.getAmount());
        receiverTransaction.setType(TransactionType.CREDIT);
        receiverTransaction.setTransactionDate(new Date());

        // Save all changes
        accountRepo.save(accountFrom);
        accountRepo.save(accountTo);
        transactionRepo.save(senderTransaction);
        transactionRepo.save(receiverTransaction);

        return new TransactionResponse(true, "Money transferred successfully from account " + userFrom.getUserId()
                + " to account " + transfer.getRecipientAccountId() + ". Your transaction ID is " + senderTransaction.getId());
    }

	@Override
	public TransactionResponse depositMoney(String userEmail, double amount) throws AccountNotFoundException {
		// Print statements for debugging
		System.out.println("Attempting to deposit money for user with email: " + userEmail);

		// Find the user by email
		User user = userRepo.findByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));

		// Find the account associated with the user
		Account account = accountRepo.findByUser(user)
				.orElseThrow(() -> new AccountNotFoundException("Account not found for user with email: " + userEmail));

		// Update user's balance
		double newBalance = account.getBalance() + amount;
		account.setBalance(newBalance);

		try {
			// Save transaction
			Transaction transaction = new Transaction();
			transaction.setUser(user);
			transaction.setAmount(amount);
			transaction.setType(TransactionType.CREDIT);
			transaction.setTransactionDate(new Date());

			accountRepo.save(account);
			transactionRepo.save(transaction);

			return new TransactionResponse(true,
					"Money credited to account successfully. Your transaction id is " + transaction.getId());
		} catch (DataAccessException e) {
			// Handle database access or persistence related errors
			throw new TransactionException("Failed to deposit money. Please try again later.", e);
		}
	}

}
