package com.securebank.bankserver.repository;

import com.securebank.bankserver.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByUser_AccountNumber(String accountNumber);
}
