package com.example.trade_simulator.services;

import com.example.trade_simulator.models.TradingSessionReport;

import java.util.Map;

public interface TradingService {
    void startTradingSession(double initialBalance);
    Map<String, Object> stopTradingSession();
    Map<String, Object> getSessionReport();
}
