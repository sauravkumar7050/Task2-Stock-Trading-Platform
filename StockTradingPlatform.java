import java.util.*;

// Class to represent a Stock
class Stock {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

// Class to represent a user's Portfolio
class Portfolio {
    private double cashBalance;
    private Map<String, Integer> holdings;

    public Portfolio(double initialBalance) {
        this.cashBalance = initialBalance;
        this.holdings = new HashMap<>();
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public Map<String, Integer> getHoldings() {
        return holdings;
    }

    public void buyStock(String symbol, int quantity, double price) {
        double totalCost = quantity * price;
        if (cashBalance >= totalCost) {
            holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
            cashBalance -= totalCost;
            System.out.println("Bought " + quantity + " shares of " + symbol);
        } else {
            System.out.println("Insufficient balance to buy " + quantity + " shares of " + symbol);
        }
    }

    public void sellStock(String symbol, int quantity, double price) {
        if (holdings.containsKey(symbol) && holdings.get(symbol) >= quantity) {
            holdings.put(symbol, holdings.get(symbol) - quantity);
            if (holdings.get(symbol) == 0) {
                holdings.remove(symbol);
            }
            cashBalance += quantity * price;
            System.out.println("Sold " + quantity + " shares of " + symbol);
        } else {
            System.out.println("Insufficient shares to sell " + quantity + " shares of " + symbol);
        }
    }

    public double getPortfolioValue(Map<String, Stock> market) {
        double totalValue = cashBalance;
        for (String symbol : holdings.keySet()) {
            int quantity = holdings.get(symbol);
            double stockPrice = market.get(symbol).getPrice();
            totalValue += quantity * stockPrice;
        }
        return totalValue;
    }
}

// Class to simulate the Stock Market
class StockMarket {
    private Map<String, Stock> market;

    public StockMarket() {
        market = new HashMap<>();
        // Sample stocks
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 150.00));
        market.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 2800.00));
        market.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 3400.00));
        market.put("MSFT", new Stock("MSFT", "Microsoft Corp.", 300.00));
    }

    public Map<String, Stock> getMarket() {
        return market;
    }

    public void displayMarketData() {
        System.out.println("Stock Market Data:");
        for (Stock stock : market.values()) {
            System.out.printf("%s (%s): $%.2f%n", stock.getName(), stock.getSymbol(), stock.getPrice());
        }
    }

    public Stock getStock(String symbol) {
        return market.get(symbol);
    }

    // Method to simulate market changes (optional)
    public void updateMarketPrices() {
        Random rand = new Random();
        for (Stock stock : market.values()) {
            double changePercent = (rand.nextDouble() - 0.5) * 2; // -1% to +1% change
            double newPrice = stock.getPrice() * (1 + changePercent / 100);
            stock.setPrice(newPrice);
        }
    }
}

// Main Class to simulate the Stock Trading Platform
public class StockTradingPlatform {
    private static Scanner scanner = new Scanner(System.in);
    private static StockMarket stockMarket = new StockMarket();
    private static Portfolio portfolio = new Portfolio(10000.00); // Initial cash balance

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Stock Trading Platform ---");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Update Market Prices");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    stockMarket.displayMarketData();
                    break;
                case 2:
                    buyStock();
                    break;
                case 3:
                    sellStock();
                    break;
                case 4:
                    viewPortfolio();
                    break;
                case 5:
                    stockMarket.updateMarketPrices();
                    System.out.println("Market prices updated.");
                    break;
                case 6:
                    exit = true;
                    System.out.println("Exiting Stock Trading Platform.");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
            }
        }
        scanner.close();
    }

    private static void buyStock() {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.next().toUpperCase();
        Stock stock = stockMarket.getStock(symbol);
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }
        System.out.print("Enter quantity to buy: ");
        int quantity = scanner.nextInt();
        portfolio.buyStock(symbol, quantity, stock.getPrice());
    }

    private static void sellStock() {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.next().toUpperCase();
        Stock stock = stockMarket.getStock(symbol);
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }
        System.out.print("Enter quantity to sell: ");
        int quantity = scanner.nextInt();
        portfolio.sellStock(symbol, quantity, stock.getPrice());
    }

    private static void viewPortfolio() {
        System.out.println("Portfolio:");
        System.out.printf("Cash Balance: $%.2f%n", portfolio.getCashBalance());
        System.out.println("Stock Holdings:");
        for (Map.Entry<String, Integer> entry : portfolio.getHoldings().entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            Stock stock = stockMarket.getStock(symbol);
            System.out.printf("%s (%s): %d shares @ $%.2f each%n", stock.getName(), symbol, quantity, stock.getPrice());
        }
        System.out.printf("Total Portfolio Value: $%.2f%n", portfolio.getPortfolioValue(stockMarket.getMarket()));
    }
}
