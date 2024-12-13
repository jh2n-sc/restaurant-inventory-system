import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockWithdrawal {
    private List<Stock> withdrawnStocks = new ArrayList<>();
    private HashMap<Stock, Double> deductedAmounts = new HashMap<>();
    private List<String> transactionLogs = new ArrayList<>();
    private TransactionLogger transactionLogger;

    public StockWithdrawal(TransactionLogger transactionLogger) {
        this.transactionLogger = transactionLogger;
    }

    public void withdrawStock(Stock stock) {
        if (!withdrawnStocks.contains(stock)) {
            withdrawnStocks.add(stock);
            logTransaction("Withdrawn", stock, stock.getAmount());
        } else {
            System.out.println("Stock already withdrawn.");
        }
    }

    public void depositStock(Stock stock) {
        if (withdrawnStocks.contains(stock)) {
            withdrawnStocks.remove(stock);
            deductedAmounts.remove(stock);
            logTransaction("Deposited", stock, stock.getAmount());
        } else {
            System.out.println("Stock has not been withdrawn.");
        }
    }

    public void addAmount(Stock stock, double amount) {
        if (withdrawnStocks.contains(stock)) {
            double deductedAmount = deductedAmounts.getOrDefault(stock, 0.0);
            if (amount <= deductedAmount) {
                stock.deposit(amount);
                deductedAmounts.put(stock, deductedAmount - amount);
                logTransaction("Added", stock, amount);
                System.out.println("Amount added.");
            } else {
                System.out.println("Cannot add more than the amount deducted.");
            }
        } else {
            System.out.println("Cannot add to a stock that has not been withdrawn.");
        }
    }

    public void deductAmount(Stock stock, double amount) {
        if (withdrawnStocks.contains(stock)) {
            stock.withdraw(amount);
            double deductedAmount = deductedAmounts.getOrDefault(stock, 0.0);
            deductedAmounts.put(stock, deductedAmount + amount);
            logTransaction("Deducted", stock, amount);
            System.out.println("Amount deducted.");
        } else {
            System.out.println("Cannot deduct from a stock that has not been withdrawn.");
        }
    }

    private void logTransaction(String type, Stock stock, double amount) {
        String log = type + ": " + amount + " " + stock.unit + " of " + stock.getItemName();
        transactionLogs.add(log);
        transactionLogger.log(log);
    }

    public List<String> getTransactionLogs() {
        return transactionLogs;
    }

    public List<Stock> getWithdrawnStocks() {
        return new ArrayList<>(withdrawnStocks);
    }
}
