package com.securebank.repository;

import com.securebank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountNumber(String accountNumber);
    List<Transaction> findByType(String type);
    List<Transaction> findByAccountNumberAndType(String accountNumber, String type);
    List<Transaction> findByFromAccountOrToAccount(String fromAccount, String toAccount);
    List<Transaction> findByAccountNumberAndDescriptionContainingIgnoreCase(String accountNumber, String description);
    List<Transaction> findByAccountNumberAndAmountBetween(String accountNumber, double minAmount, double maxAmount);
    List<Transaction> findByAccountNumberAndAmountGreaterThanEqual(String accountNumber, double amount);
    List<Transaction> findByAccountNumberAndAmountLessThanEqual(String accountNumber, double amount);
    Optional<Transaction> findById(Long id);
}