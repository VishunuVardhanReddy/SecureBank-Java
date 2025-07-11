package com.securebank.controller;

import com.securebank.dto.TransactionRequest;
import com.securebank.dto.TransactionResponseDto;
import com.securebank.exception.AccessDeniedException;
import com.securebank.exception.TransactionNotFoundException;
import com.securebank.model.Transaction;
import com.securebank.service.TransactionService;
import com.securebank.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private AuthService authService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions() {
        String accountNumber = authService.getCurrentUser().getAccountNumber();
        List<Transaction> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        return ResponseEntity.ok(mapToDtoList(transactions));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        validateTransactionOwnership(transaction);
        return ResponseEntity.ok(mapToDto(transaction));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByType(@PathVariable String type) {
        String accountNumber = authService.getCurrentUser().getAccountNumber();
        List<Transaction> transactions = transactionService.getTransactionsByAccountAndType(accountNumber, type);
        return ResponseEntity.ok(mapToDtoList(transactions));
    }

    @GetMapping("/transfers")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionResponseDto>> getTransferTransactions() {
        String accountNumber = authService.getCurrentUser().getAccountNumber();
        List<Transaction> transactions = transactionService.getTransferTransactions(accountNumber);
        return ResponseEntity.ok(mapToDtoList(transactions));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionResponseDto>> searchTransactions(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount) {
        
        String accountNumber = authService.getCurrentUser().getAccountNumber();
        List<Transaction> transactions = transactionService.searchTransactions(
                accountNumber, description, minAmount, maxAmount);
        
        return ResponseEntity.ok(mapToDtoList(transactions));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    private void validateTransactionOwnership(Transaction transaction) {
        String currentAccount = authService.getCurrentUser().getAccountNumber();
        if (!transaction.getAccountNumber().equals(currentAccount) &&
            !(transaction.getFromAccount() != null && transaction.getFromAccount().equals(currentAccount)) &&
            !(transaction.getToAccount() != null && transaction.getToAccount().equals(currentAccount))) {
            throw new AccessDeniedException("You don't have permission to access this transaction");
        }
    }

    private List<TransactionResponseDto> mapToDtoList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TransactionResponseDto mapToDto(Transaction transaction) {
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setId(transaction.getId());
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setDate(transaction.getDate());
        dto.setDescription(transaction.getDescription());
        dto.setBalance(transaction.getBalance());
        dto.setFromAccount(transaction.getFromAccount());
        dto.setToAccount(transaction.getToAccount());
        return dto;
    }
}