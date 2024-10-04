package com.example.trade_simulator.models;

import java.time.LocalDateTime;

public class StockPrice {
    private final LocalDateTime timestamp;
    private final double price;

    public StockPrice(LocalDateTime timestamp, double price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getPrice() {
        return price;
    }
}
