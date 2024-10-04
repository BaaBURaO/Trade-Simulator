package com.example.trade_simulator.models;

import java.util.List;

public class TradingSessionReport {
    private final List<Transaction> transactions;
    private final double finalCashBalance;
    private final double currentProfit;
    private final double currentStocksQty;


    public TradingSessionReport(List<Transaction> transactions, double finalCashBalance, double currentStocksQty, double currentPrice, double initialBalance) {
        this.transactions = transactions;
        this.finalCashBalance = finalCashBalance;
        this.currentStocksQty = currentStocksQty;
        this.currentProfit = calculateCurrentProfit(currentPrice, initialBalance);
    }
    public double calculateCurrentProfit(double currentPrice, double initialBalance) {
        double stocksValue = currentStocksQty * currentPrice;
        return (finalCashBalance + stocksValue) - initialBalance;
    }
}
