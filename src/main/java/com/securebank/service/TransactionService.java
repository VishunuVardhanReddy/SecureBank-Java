package com.securebank.service;

import com.securebank.model.Transaction;
import com.securebank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    public List<Transaction> getTransactionsByType(String type) {
        return transactionRepository.findByType(type);
    }

    public List<Transaction> getTransactionsByAccountAndType(String accountNumber, String type) {
        return transactionRepository.findByAccountNumberAndType(accountNumber, type);
    }

    public List<Transaction> getTransferTransactions(String accountNumber) {
        return transactionRepository.findByFromAccountOrToAccount(accountNumber, accountNumber);
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> searchTransactions(String accountNumber, String description, 
                                             Double minAmount, Double maxAmount) {
        if (description != null) {
            return transactionRepository.findByAccountNumberAndDescriptionContainingIgnoreCase(accountNumber, description);
        } else if (minAmount != null && maxAmount != null) {
            return transactionRepository.findByAccountNumberAndAmountBetween(accountNumber, minAmount, maxAmount);
        } else if (minAmount != null) {
            return transactionRepository.findByAccountNumberAndAmountGreaterThanEqual(accountNumber, minAmount);
        } else if (maxAmount != null) {
            return transactionRepository.findByAccountNumberAndAmountLessThanEqual(accountNumber, maxAmount);
        }
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}