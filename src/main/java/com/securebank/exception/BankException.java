package com.securebank.exception;

public class BankException extends RuntimeException {
    public BankException(String message) {
        super(message);
    }
}