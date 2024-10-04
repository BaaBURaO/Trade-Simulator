#### Project Overview
The **Trade Simulator** is a Java Spring Boot-based application designed to simulate a stock trading session using the Moving Average Crossover strategy. The simulator fetches stock prices at regular intervals and decides whether to buy or sell based on the short-term and long-term Simple Moving Averages (SMA). Users can start and stop trading sessions, and view session reports containing transaction details, profit calculations, and remaining balance.

#### Technologies Used:
- **Java 11+**
- **Spring Boot**
- **Maven**
- **REST APIs**
- **SLF4J for Logging**

---

#### How to Run the Application:

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd trade_simulator
   ```

2. **Update application properties:**
   Modify the `application.properties` file in the `src/main/resources` folder to adjust the trading parameters.

   Example configuration:
   ```properties
   trading.initialStockPrice=100.0
   trading.pollInterval=1
   trading.defaultBalance=1000.0
   trading.shortTermPeriodInSec=5
   trading.longTermPeriodInSec=20
   ```

3. **Run the Application:**
   Run the Spring Boot application via Maven:
   ```bash
   mvn spring-boot:run
   ```

4. **Available REST API Endpoints:**
   - **Start Trading**: 
     ```http
     POST /trading/start?initialBalance=<balance>
     ```
     Starts a trading session with the specified initial balance.

   - **Stop Trading**:
     ```http
     POST /trading/stop
     ```
     Stops the trading session and returns the session report.

   - **Get Trading Session Report**:
     ```http
     GET /trading/report
     ```
     Returns the summary of the current trading session including transactions, final profit, remaining balance, and stocks held.

---

#### How to Modify Trading Parameters:
The trading behavior can be adjusted via the `application.properties` file:
- `trading.initialStockPrice`: The starting price of the stock.
- `trading.pollInterval`: The interval (in seconds) between price fetches.
- `trading.defaultBalance`: The default initial balance to start a trading session.
- `trading.shortTermPeriodInSec`: The period (in seconds) for calculating the short-term SMA.
- `trading.longTermPeriodInSec`: The period (in seconds) for calculating the long-term SMA.

---

#### Example Usage:

1. Start a trading session with an initial balance of 1000.0:
   ```bash
   curl -X POST "http://localhost:8080/trading/start?initialBalance=1000"
   ```

2. Stop the trading session:
   ```bash
   curl -X POST "http://localhost:8080/trading/stop"
   ```

3. Fetch the session report:
   ```bash
   curl -X GET "http://localhost:8080/trading/report"
   ```

---

#### Logs:
Application logs can be viewed in the console. Transactions (buy and sell orders) are logged with details about the amount, price, and balance updates.

---

#### Future Enhancements:
- Implementing multiple trading strategies.
- Adding support for real-time stock data.
- Expanding to handle multiple stocks in the same session.

---

### Solution Document

#### Problem Description:
The task was to develop a simple stock trading bot using the Moving Average Crossover Strategy. The strategy relies on comparing short-term and long-term Simple Moving Averages (SMA) of stock prices. When the short-term SMA crosses above the long-term SMA, the bot buys the stock, and when it crosses below, it sells the stock.

---

#### Architecture:

- **MovingAverageTradingServiceImpl**: Core service implementing the trading logic. It fetches stock prices at regular intervals, computes the SMAs, and makes trading decisions.
- **StockDataProviderService**: Simulates stock price changes and provides historical data for SMA calculation.
- **TradingController**: REST controller that exposes endpoints for starting, stopping, and reporting on trading sessions.
- **TradingProperties**: Configuration class that holds key parameters for the trading bot like stock price, trading intervals, etc.

---

#### Key Features:

1. **Trading Session Management**: 
   The system allows starting a trading session with an initial balance and manages buying and selling based on the Moving Average Crossover strategy.
   
2. **Polling Stock Prices**: 
   Stock prices are fetched at intervals defined by the `pollInterval` property, and decisions are made based on real-time and historical price data.
   
3. **Transaction Logging**: 
   All buy and sell orders are logged and stored as transactions for reporting.

4. **Graceful Shutdown**: 
   When the trading session is stopped, the scheduled polling tasks are terminated, ensuring no additional trades occur after the session ends.

5. **Trading Report**: 
   Users can view a summary of the trading session, including all transactions, the remaining balance, stocks held, and the final profit/loss.

---

#### Core Logic:
The trading decisions are made based on the following rules:
- **Buy Signal**: When the short-term SMA is higher than the long-term SMA, the bot buys stocks using 10% of the cash balance.
- **Sell Signal**: When the short-term SMA is lower than the long-term SMA, the bot sells 10% of the stocks in hand.
  
The SMAs are calculated using the `calculateSMA` method, which averages the prices over the short-term and long-term windows.

---

#### Example Flow:

1. **Initialization**: 
   When the session starts, the bot sets up the initial cash balance and begins fetching stock prices.

2. **Price Fetching and SMA Calculation**: 
   Prices are fetched at regular intervals, and SMAs are calculated based on historical price data. 

3. **Trade Execution**: 
   When a buy or sell signal is detected based on the SMAs, the bot executes the appropriate trade, updating the balance and the stocks held.

4. **Session Termination**: 
   When the session is stopped, a final report is generated, summarizing all trades and the profit/loss.

---

#### Future Improvements:

- **Real-Time Data**: Integrating with a real stock market API to use live stock data.
- **Multiple Stocks**: Extending the system to handle multiple stock symbols in one session.
- **More Trading Strategies**: Implementing additional strategies like Bollinger Bands or RSI.

#### Conclusion:
This trading simulator provides a robust foundation for testing the Moving Average Crossover Strategy in a simplified, controlled environment.