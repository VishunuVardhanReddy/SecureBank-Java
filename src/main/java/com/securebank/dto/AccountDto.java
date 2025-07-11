package com.securebank.dto;

public class AccountDto {
    private String accountNumber;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private double balance;

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}