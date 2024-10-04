package com.example.trade_simulator.models;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="trading")
public class TradingProperties {

    private double initialStockPrice;
    private long pollInterval;
    private double defaultBalance;
    private int shortTermPeriodInSec;
    private int longTermPeriodInSec;

    // Getters and Setters
    public double getInitialStockPrice() {
        return initialStockPrice;
    }

    public void setInitialStockPrice(double initialStockPrice) {
        this.initialStockPrice = initialStockPrice;
    }

    public long getPollInterval() {
        return pollInterval;
    }

    public void setPollInterval(long pollInterval) {
        this.pollInterval = pollInterval;
    }

    public double getDefaultBalance() {
        return defaultBalance;
    }

    public void setDefaultBalance(double defaultBalance) {
        this.defaultBalance = defaultBalance;
    }

    public int getLongTermPeriodInSec() {
        return longTermPeriodInSec;
    }

    public int getShortTermPeriodInSec() {
        return shortTermPeriodInSec;
    }
    public void setShortTermPeriodInSec(int shortTermPeriodInSec) {
        this.shortTermPeriodInSec = shortTermPeriodInSec;
    }

    public void setLongTermPeriodInSec(int longTermPeriodInSec) {
        this.longTermPeriodInSec = longTermPeriodInSec;
    }
}