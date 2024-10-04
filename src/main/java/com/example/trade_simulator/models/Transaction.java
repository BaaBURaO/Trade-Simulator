package com.example.trade_simulator.models;

import java.time.LocalDateTime;

public class Transaction {
    private final LocalDateTime timestamp;
    private final String type; // BUY or SELL
    private final double amount;
    private final double price;

    public Transaction(LocalDateTime timestamp, String type, double amount, double price) {
        this.timestamp = timestamp;
        this.type = type;
        this.amount = amount;
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }
}

