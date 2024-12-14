import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockWithdrawal {
    private List<Stock> withdrawnStocks = new ArrayList<>();
    private HashMap<Stock, Double> deductedAmounts = new HashMap<>();
    private List<String> transactionLogs = new ArrayList<>();
    private TransactionLogger transactionLogger;
    private Inventory inventory;

    // Constructor: Initializes the transaction logger and inventory
    public StockWithdrawal(TransactionLogger transactionLogger, Inventory inventory) {
        this.transactionLogger = transactionLogger;
        this.inventory = inventory;
    }

    // Withdraws an amount from the stocks and logs the transaction
    public void withdrawStock(Item item, double amount) {
        List<Stock> stocks = item.stocks;
        double totalAvailableStock = stocks.stream().mapToDouble(Stock::getAmount).sum();

        if (totalAvailableStock < amount) {
            System.out.println("Not enough stock to withdraw the requested amount.");
            return;
        }

        double remainingAmount = amount;

        for (Stock stock : stocks) {
            if (remainingAmount <= 0) break;
            double stockAmount = stock.getAmount();
            if (remainingAmount >= stockAmount) {
                remainingAmount -= stockAmount;
                stock.withdraw(stockAmount);
                addToWithdrawnStocks(stock, stockAmount);
            } else {
                stock.withdraw(remainingAmount);
                addToWithdrawnStocks(stock, remainingAmount);
                remainingAmount = 0;
            }
            logTransaction("Withdrawn", stock, stockAmount);
        }

        inventory.updateFile();
    }

    private void addToWithdrawnStocks(Stock stock, double amount) {
        boolean found = false;
        for (Stock withdrawnStock : withdrawnStocks) {
            if (withdrawnStock.getItemName().equals(stock.getItemName()) && 
                withdrawnStock.getExpiry().equals(stock.getExpiry())) {
                withdrawnStock.deposit(amount);
                found = true;
                break;
            }
        }
        if (!found) {
            Stock newWithdrawnStock = new Stock(stock.getItemName(), amount, stock.unit);
            newWithdrawnStock.setDateArrived(stock.getInvoice());
            newWithdrawnStock.setExpiryDate(stock.getExpiry());
            withdrawnStocks.add(newWithdrawnStock);
        }
    }

    // Deposits an amount back to the respective stocks and logs the transaction
    public void depositStock(Item item, double amount) {
        List<Stock> stocks = item.stocks;
        double remainingAmount = amount;

        for (Stock stock : stocks) {
            if (remainingAmount <= 0) break;
            for (Stock withdrawnStock : withdrawnStocks) {
                if (withdrawnStock.getItemName().equals(stock.getItemName()) && 
                    withdrawnStock.getExpiry().equals(stock.getExpiry())) {
                    double depositAmount = Math.min(withdrawnStock.getAmount(), remainingAmount);
                    stock.deposit(depositAmount);
                    remainingAmount -= depositAmount;
                    withdrawnStock.withdraw(depositAmount);
                    logTransaction("Deposited", stock, depositAmount);
                }
            }
        }

        if (remainingAmount > 0) {
            System.out.println("Not enough withdrawn stock to deposit the requested amount.");
        }

        inventory.updateFile();
    }

    // Removes stocks with zero amounts before the program ends
    public void cleanUpStocks() {
        for (Category category : inventory.getCategories()) {
            for (Item item : category.getItemList()) {
                List<Stock> stocks = item.stocks;
                stocks.removeIf(stock -> stock.getAmount() == 0);
            }
        }
        inventory.updateFile();
    }

    // Displays current stocks
    public void displayStocks(Item item) {
        System.out.println("Current Stocks for " + item.getItem_Name() + ":");
        for (Stock stock : item.stocks) {
            stock.printStock();
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
