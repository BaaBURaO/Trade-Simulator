package com.example.trade_simulator;

import com.example.trade_simulator.models.TradingSessionReport;
import com.example.trade_simulator.services.MovingAverageTradingServiceImpl;
import com.example.trade_simulator.services.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/trading")
public class TradingController {

    private final TradingService tradingService;

    @Autowired
    public TradingController(MovingAverageTradingServiceImpl tradingService) {
        this.tradingService = tradingService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startTrading(@RequestParam double initialBalance) {
        tradingService.startTradingSession(initialBalance);
        return ResponseEntity.ok("Trading session started with balance: " + initialBalance);
    }

    @PostMapping("/stop")
    public ResponseEntity<Map<String,Object>> stopTrading() {
        return  ResponseEntity.ok(tradingService.stopTradingSession());
    }

    @GetMapping("/report")
    public ResponseEntity<Map<String,Object>> getSessionReport() {
        return ResponseEntity.ok(tradingService.getSessionReport());
    }
}
