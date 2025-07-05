package com.securebank.bankserver.controller;

import com.securebank.bankserver.model.Transaction;
import com.securebank.bankserver.model.User;
import com.securebank.bankserver.repository.TransactionRepository;
import com.securebank.bankserver.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public UserController(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> data) {
        String accountNumber = String.valueOf(new Random().nextInt(900000000) + 100000000);
        double initialDeposit = Double.parseDouble(data.get("initialDeposit"));

        User user = new User();
        user.setAccountNumber(accountNumber);
        user.setFullName(data.get("fullName"));
        user.setEmail(data.get("email"));
        user.setPhone(data.get("phone"));
        user.setAddress(data.get("address"));
        user.setPassword(data.get("password"));
        user.setBalance(initialDeposit);

        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID().toString());
        tx.setType("deposit");
        tx.setAmount(initialDeposit);
        tx.setDate(LocalDateTime.now().toString());
        tx.setDescription("Initial Deposit");
        tx.setBalance(initialDeposit);
        tx.setToAccount(accountNumber);
        tx.setUser(user);

        user.setTransactions(List.of(tx));
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", accountNumber);
        return response;
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> data) {
        String account = data.get("accountNumber");
        String password = data.get("password");

        User user = userRepository.findByAccountNumberAndPassword(account, password);
        if (user == null) throw new RuntimeException("Invalid credentials");

        List<Transaction> transactions = transactionRepository.findByUser_AccountNumber(account);
        user.setTransactions(transactions);
        return user;
    }
}
