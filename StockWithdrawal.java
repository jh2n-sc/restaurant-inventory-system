import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockWithdrawal {
    private List<Stock> withdrawnStocks = new ArrayList<>();
    private HashMap<Stock, Double> deductedAmounts = new HashMap<>();
    private List<String> transactionLogs = new ArrayList<>();
    private TransactionLogger transactionLogger;

    // Constructor: Initializes the transaction logger
    public StockWithdrawal(TransactionLogger transactionLogger) {
        this.transactionLogger = transactionLogger;
    }

    // Withdraws a stock and logs the transaction
    public void withdrawStock(Stock stock) {
        if (!withdrawnStocks.contains(stock)) {
            withdrawnStocks.add(stock);
            logTransaction("Withdrawn", stock, stock.getAmount());
        } else {
            System.out.println("Stock already withdrawn.");
        }
    }

    // Deposits a stock and logs the transaction, removing it from the withdrawn list
    public void depositStock(Stock stock) {
        if (withdrawnStocks.contains(stock)) {
            withdrawnStocks.remove(stock);
            deductedAmounts.remove(stock);
            logTransaction("Deposited", stock, stock.getAmount());
        } else {
            System.out.println("Stock has not been withdrawn.");
        }
    }

    // Adds an amount to a stock and logs the transaction, ensuring it does not exceed the amount deducted
    public void addAmount(Stock stock, double amount) {
        if (withdrawnStocks.contains(stock)) {
            double deductedAmount = deductedAmounts.getOrDefault(stock, 0.0);
            if (amount <= deductedAmount) {
                stock.deposit(amount);
                deductedAmounts.put(stock, deductedAmount - amount);
                logTransaction("Withdrawn/Added", stock, -amount);
                System.out.println("Amount added.");
            } else {
                System.out.println("Cannot add more than the amount deducted.");
            }
        } else {
            System.out.println("Cannot add to a stock that has not been withdrawn.");
        }
    }

    // Deducts an amount from a stock and logs the transaction
    public void deductAmount(Stock stock, double amount) {
        if (withdrawnStocks.contains(stock)) {
            stock.withdraw(amount);
            double deductedAmount = deductedAmounts.getOrDefault(stock, 0.0);
            deductedAmounts.put(stock, deductedAmount + amount);
            logTransaction("Withdrawn/Deducted", stock, amount);
            System.out.println("Amount deducted.");
        } else {
            System.out.println("Cannot deduct from a stock that has not been withdrawn.");
        }
    }

    // Logs a transaction with its type, stock, and amount
    private void logTransaction(String type, Stock stock, double amount) {
        String log = type + ": " + amount + " " + stock.unit + " of " + stock.getItemName();
        transactionLogs.add(log);
        transactionLogger.log(log);
    }

    // Returns the list of transaction logs
    public List<String> getTransactionLogs() {
        return transactionLogs;
    }

    // Returns the list of withdrawn stocks
    public List<Stock> getWithdrawnStocks() {
        return new ArrayList<>(withdrawnStocks);
    }
}
