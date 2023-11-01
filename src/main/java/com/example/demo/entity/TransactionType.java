package com.example.demo.entity;

public enum TransactionType {
    BUY,
    SELL;

    public boolean isBuy() {
        return BUY.equals(this);
    }
}

