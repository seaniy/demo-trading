package com.example.demo.exception;

public class CryptoPairNotFoundException extends RuntimeException {
    public CryptoPairNotFoundException(String crypto) {
        super(crypto + " not found");
    }
}
