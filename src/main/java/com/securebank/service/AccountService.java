package com.securebank.service;

import com.securebank.dto.AuthResponse;
import com.securebank.dto.TransactionRequest;
import com.securebank.exception.*;
import com.securebank.model.Transaction;
import com.securebank.model.User;
import com.securebank.repository.TransactionRepository;
import com.securebank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User createAccount(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BankException("Email already in use");
        }
        
        // Generate account number if not provided
        if (user.getAccountNumber() == null) {
            user.setAccountNumber(generateAccountNumber());
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBalance(0.0);
        return userRepository.save(user);
    }
    
    @Transactional
    public Transaction deposit(String accountNumber, TransactionRequest request) {
        User user = userRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        
        double amount = request.getAmount();
        if (amount <= 0) {
            throw new BankException("Amount must be positive");
        }
        
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        
        Transaction transaction = createTransaction(
                accountNumber, 
                "DEPOSIT", 
                amount, 
                request.getDescription(), 
                user.getBalance());
        
        return transactionRepository.save(transaction);
    }
    
    @Transactional
    public Transaction withdraw(String accountNumber, TransactionRequest request) {
        User user = userRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        
        double amount = request.getAmount();
        if (amount <= 0) {
            throw new BankException("Amount must be positive");
        }
        
        if (user.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
        
        Transaction transaction = createTransaction(
                accountNumber, 
                "WITHDRAWAL", 
                amount, 
                request.getDescription(), 
                user.getBalance());
        
        return transactionRepository.save(transaction);
    }
    
    @Transactional
    public Transaction transfer(String fromAccountNumber, TransactionRequest request) {
        if (request.getToAccount() == null) {
            throw new BankException("Recipient account is required for transfer");
        }
        
        User fromUser = userRepository.findById(fromAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));
        
        User toUser = userRepository.findById(request.getToAccount())
                .orElseThrow(() -> new AccountNotFoundException("Recipient account not found"));
        
        double amount = request.getAmount();
        if (amount <= 0) {
            throw new BankException("Amount must be positive");
        }
        
        if (fromUser.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        
        // Update sender balance
        fromUser.setBalance(fromUser.getBalance() - amount);
        userRepository.save(fromUser);
        
        // Update recipient balance
        toUser.setBalance(toUser.getBalance() + amount);
        userRepository.save(toUser);
        
        // Create transaction for sender
        Transaction senderTransaction = createTransaction(
                fromAccountNumber, 
                "TRANSFER_OUT", 
                amount, 
                request.getDescription(), 
                fromUser.getBalance());
        senderTransaction.setToAccount(request.getToAccount());
        transactionRepository.save(senderTransaction);
        
        // Create transaction for recipient
        Transaction recipientTransaction = createTransaction(
                request.getToAccount(), 
                "TRANSFER_IN", 
                amount, 
                request.getDescription(), 
                toUser.getBalance());
        recipientTransaction.setFromAccount(fromAccountNumber);
        transactionRepository.save(recipientTransaction);
        
        return senderTransaction;
    }
    
    public List<Transaction> getTransactions(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }
    
    public User getAccountDetails(String accountNumber) {
        return userRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }
    
    private Transaction createTransaction(String accountNumber, String type, 
                                       double amount, String description, double balance) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(description);
        transaction.setBalance(balance);
        return transaction;
    }
    
    private String generateAccountNumber() {
        return "AC" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}