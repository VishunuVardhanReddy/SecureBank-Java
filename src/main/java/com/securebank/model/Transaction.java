package com.securebank.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "accountNumber", nullable = false)
    private String accountNumber;
    
    @Column(name = "type", nullable = false)
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER
    
    @Column(name = "amount", nullable = false)
    private double amount;
    
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "balance", nullable = false)
    private double balance;
    
    @Column(name = "fromAccount")
    private String fromAccount;
    
    @Column(name = "toAccount")
    private String toAccount;
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }
    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
}