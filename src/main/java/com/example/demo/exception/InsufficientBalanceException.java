package com.example.demo.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String crypto) {
        super("Insufficient " + crypto + " balance");
    }
}
