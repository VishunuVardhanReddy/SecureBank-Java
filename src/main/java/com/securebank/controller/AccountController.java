package com.securebank.controller;

import com.securebank.dto.TransactionRequest;
import com.securebank.model.Transaction;
import com.securebank.model.User;
import com.securebank.service.AccountService;
import com.securebank.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private AuthService authService;
    
    @PostMapping
    public ResponseEntity<User> createAccount(@RequestBody User user) {
        return ResponseEntity.ok(accountService.createAccount(user));
    }
    
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getMyAccount() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(accountService.getAccountDetails(user.getAccountNumber()));
    }
    
    @PostMapping("/deposit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Transaction> deposit(@RequestBody TransactionRequest request) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(accountService.deposit(user.getAccountNumber(), request));
    }
    
    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Transaction> withdraw(@RequestBody TransactionRequest request) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(accountService.withdraw(user.getAccountNumber(), request));
    }
    
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Transaction> transfer(@RequestBody TransactionRequest request) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(accountService.transfer(user.getAccountNumber(), request));
    }
    
    @GetMapping("/transactions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Transaction>> getMyTransactions() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(accountService.getTransactions(user.getAccountNumber()));
    }
}