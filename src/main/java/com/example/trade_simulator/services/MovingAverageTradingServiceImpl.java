package com.example.trade_simulator.services;

import com.example.trade_simulator.models.StockPrice;
import com.example.trade_simulator.models.TradingProperties;
import com.example.trade_simulator.models.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class MovingAverageTradingServiceImpl implements TradingService {
    private static final Logger logger = LoggerFactory.getLogger(MovingAverageTradingServiceImpl.class);
    private final StockDataProviderService stockDataProvider;
    private final List<Transaction> transactions = new ArrayList<>();
    private double cashInHand;
    private double stocksInHand;
    private boolean isRunning;
    private double initialBalance;
    private ScheduledExecutorService executorService;
    private final TradingProperties tradingProperties;

    @Autowired
    public MovingAverageTradingServiceImpl(StockDataProviderService stockDataProvider,
                                           TradingProperties tradingProperties) {
        this.stockDataProvider = stockDataProvider;
        this.tradingProperties = tradingProperties;
    }

    @Override
    public void startTradingSession(double initialBalance) {
        this.cashInHand = initialBalance;
        this.isRunning = true;
        this.initialBalance = initialBalance;
        this.stocksInHand = 0.0;
        logger.info("Starting trading session with cashInHand: {}", initialBalance);

        if (executorService == null || executorService.isShutdown()) {
            this.executorService = Executors.newSingleThreadScheduledExecutor();
        }
        startPollingPrices();
    }

    private void startPollingPrices() {
        executorService.scheduleAtFixedRate(() -> {
            if (!isRunning) return;
            double currentPrice = stockDataProvider.getCurrentPrice();
            performTradeDecision(currentPrice);
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void performTradeDecision(double currentPrice) {
        List<StockPrice> priceHistory = stockDataProvider.generateHistoricalData(2L * tradingProperties.getLongTermPeriodInSec());

        double shortTermSMA = calculateSMA(priceHistory, tradingProperties.getShortTermPeriodInSec());
        double longTermSMA = calculateSMA(priceHistory, tradingProperties.getLongTermPeriodInSec());

        if (shortTermSMA > longTermSMA) {
            buyStock(currentPrice);
        }
        else if (shortTermSMA < longTermSMA && stocksInHand > 0) {
            sellStock(currentPrice);
        }
    }

    private static double calculateSMA(List<StockPrice> stockPrices, int period) {
        double sum = 0;
        for (int i = stockPrices.size() - period; i < stockPrices.size(); i++) {
            sum += stockPrices.get(i).getPrice();
        }
        return sum / period;
    }

    private void buyStock(double price) {
        double amountToBuy = cashInHand * 0.1;
        cashInHand -= amountToBuy;
        stocksInHand += amountToBuy / price;
        transactions.add(new Transaction(LocalDateTime.now(), "BUY", amountToBuy, price));
        logger.info("Bought stock: order_amount = {}, current_price = {}, updated_cash_in_hand = {}", amountToBuy, price, cashInHand);
    }

    private void sellStock(double price) {
        double amountToSell = cashInHand * 0.1;
        if (amountToSell > price * stocksInHand) {
            return;
        }
        cashInHand += amountToSell;
        stocksInHand -= amountToSell / price;
        transactions.add(new Transaction(LocalDateTime.now(), "SELL", amountToSell, price));
        logger.info("Sold stock: order_amount = {}, current_price = {}, updated_cash_in_hand = {}", amountToSell, price, cashInHand);
    }

    @Override
    public Map<String, Object> stopTradingSession() {
        this.isRunning = false;
        logger.info("Shutting down trading session...");

        // Gracefully shutdown the executor service
        executorService.shutdown(); // Disable new tasks from being submitted

        try {
            // Cancel all running and queued tasks
            executorService.shutdownNow(); // Ensures that even queued tasks are cancelled

            // Wait a while for tasks to terminate
            if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                logger.warn("Executor did not terminate in time, forcing shutdown...");
            }
        } catch (InterruptedException e) {
            logger.error("Shutdown interrupted, forcing immediate shutdown...");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();  // Restore interrupt status
        }

        logger.info("Trading session stopped.");
        return getSessionReport();
    }

    @Override
    public Map<String, Object> getSessionReport() {
        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactions);
        response.put("cash_remaining_in_hand", cashInHand);
        response.put("stocks_in_hand", stocksInHand);
        response.put("final_profit", calculateFinalProfit(stockDataProvider.getCurrentPrice(), initialBalance));
        return response;
    }

    private double calculateFinalProfit(double currentPrice, double initialBalance) {
        double stocksValue = stocksInHand * currentPrice;
        return (cashInHand + stocksValue) - initialBalance;
    }
}
