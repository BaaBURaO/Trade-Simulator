package com.example.trade_simulator.services;

import com.example.trade_simulator.exception.PriceNotFoundException;
import com.example.trade_simulator.models.StockPrice;
import com.example.trade_simulator.models.TradingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class StockDataProviderService  {

    private static final Logger logger = LoggerFactory.getLogger(StockDataProviderService.class);
    private final List<StockPrice> priceList = new ArrayList<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final TradingProperties tradingProperties;

    public StockDataProviderService(TradingProperties tradingProperties) {
        logger.info("Initializing StockDataProviderService.");
        this.tradingProperties = tradingProperties;
        startPriceGeneration(tradingProperties.getInitialStockPrice());
    }

    public List<StockPrice> generateHistoricalData(long countOfPrices) {
        List<StockPrice> priceHistory = new ArrayList<>();
        if (countOfPrices > priceList.size()) {
            long extra = countOfPrices - priceList.size();
            logger.info("Generating extra historical data for: {} seconds", extra);
            LocalDateTime now = LocalDateTime.now();
            for (int i = 0; i < extra ; i++) {
                priceHistory.add(new StockPrice(now.minusSeconds(extra - i), generateRandomPrice(tradingProperties.getInitialStockPrice())));
                priceList.add(new StockPrice(now.minusSeconds(extra - i), generateRandomPrice(tradingProperties.getInitialStockPrice())));
            }
        }
        logger.info("Fetching historical data for: {} seconds", priceList.size());
        LocalDateTime now = LocalDateTime.now();
        for (int i = priceList.size() - 1; i > priceList.size() - countOfPrices ; i--) {
            priceHistory.add(new StockPrice(now.minusSeconds(priceList.size() - i), priceList.get(i).getPrice()));
        }
        return priceHistory;
    }

    public double getCurrentPrice() {
        if (priceList.isEmpty()) {
            return tradingProperties.getInitialStockPrice();
        }
        return priceList.get(priceList.size() - 1).getPrice();
    }

    public double getPriceAtTimestamp(LocalDateTime timestamp) throws PriceNotFoundException {
        return priceList.stream()
                .filter(stockPrice -> stockPrice.getTimestamp().equals(timestamp))
                .findFirst()
                .map(StockPrice::getPrice)
                .orElseThrow(() -> {
                    logger.error("Price not found for timestamp: {}", timestamp);
                    return new PriceNotFoundException("Price not found for timestamp: " + timestamp);
                });
    }

    private void startPriceGeneration(double initialPriceOfStock) {
        executorService.scheduleAtFixedRate(() -> {
            double lastPrice = priceList.isEmpty() ? initialPriceOfStock : getCurrentPrice();
            priceList.add(new StockPrice(LocalDateTime.now(), generateRandomPrice(lastPrice)));
            logger.info("Generated new stock price: {}", getCurrentPrice());
        }, 1, 1, TimeUnit.SECONDS);
    }

    private double generateRandomPrice(double basePrice) {
        return basePrice + (Math.random() * 10 - 5); // Random fluctuation of ±5
    }
}
