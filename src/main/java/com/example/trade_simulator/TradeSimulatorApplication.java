package com.example.trade_simulator;

import com.example.trade_simulator.models.TradingProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TradingProperties.class)
public class TradeSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeSimulatorApplication.class, args);
	}

}
