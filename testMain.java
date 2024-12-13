import java.util.List;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        // Initialize Inventory and load existing categories and items
        Inventory inventory = new Inventory();
        Scanner scanner = new Scanner(System.in);

        // Initialize StockWithdrawal with a single TransactionLogger
        TransactionLogger transactionLogger = new TransactionLogger();
        StockWithdrawal stockWithdrawal = new StockWithdrawal(transactionLogger);

        String anotherOperation;

        do {
            // Display available commands
            System.out.println("Available Commands: withdraw, deposit, add, deduct, exit");

            // Get user command
            System.out.println("Enter command: ");
            String command = scanner.next().toLowerCase();

            if (command.equals("exit")) {
                break;
            }

            if (command.equals("withdraw")) {
                // Display categories
                System.out.println("Available Categories:");
                List<Category> categories = inventory.getCategories();
                for (int i = 0; i < categories.size(); i++) {
                    System.out.println(i + ": " + categories.get(i).getCategoryName());
                }

                // Choose category
                System.out.println("Enter the category number: ");
                int categoryIndex = scanner.nextInt();
                Category category = categories.get(categoryIndex);

                // Display items in the category
                System.out.println("Available Items in " + category.getCategoryName() + ":");
                List<Item> items = category.getItemList();
                for (int i = 0; i < items.size(); i++) {
                    System.out.println(i + ": " + items.get(i).getItem_Name());
                }

                // Choose item
                System.out.println("Enter the item number: ");
                int itemIndex = scanner.nextInt();
                Item item = items.get(itemIndex);

                // Display stocks in the item
                System.out.println("Available Stocks for " + item.getItem_Name() + ":");
                List<Stock> stocks = item.stocks;
                for (int i = 0; i < stocks.size(); i++) {
                    System.out.println(i + ": " + stocks.get(i).getAmount() + " - Expires on: " + stocks.get(i).getExpiry());
                }

                // Choose stock
                System.out.println("Enter the stock number: ");
                int stockIndex = scanner.nextInt();
                Stock selectedStock = stocks.get(stockIndex);

                // Withdraw the stock
                stockWithdrawal.withdrawStock(selectedStock);
                System.out.println("Stock withdrawn.");
            }

            if (command.equals("deposit") || command.equals("add") || command.equals("deduct")) {
                // Display withdrawn stocks
                System.out.println("Withdrawn Stocks:");
                List<Stock> withdrawnStocks = stockWithdrawal.getWithdrawnStocks();
                for (int i = 0; i < withdrawnStocks.size(); i++) {
                    System.out.println(i + ": " + withdrawnStocks.get(i).getItemName() + " - " + withdrawnStocks.get(i).getAmount() + " - Expires on: " + withdrawnStocks.get(i).getExpiry());
                }

                System.out.println("Enter the stock number: ");
                int modifyStockIndex = scanner.nextInt();
                Stock modifyStock = withdrawnStocks.get(modifyStockIndex);

                if (command.equals("deposit")) {
                    stockWithdrawal.depositStock(modifyStock);
                    System.out.println("Stock deposited.");
                } else {
                    System.out.println("Enter the amount to " + command + " from " + modifyStock.getItemName() + ": ");
                    double modifyAmount = scanner.nextDouble();
                    if (command.equals("add")) {
                        stockWithdrawal.addAmount(modifyStock, modifyAmount);
                    } else {
                        stockWithdrawal.deductAmount(modifyStock, modifyAmount);
                    }
                }
            }

            // Prompt for another operation
            System.out.println("Do you want to perform another operation? (yes/no): ");
            anotherOperation = scanner.next();

        } while (anotherOperation.equalsIgnoreCase("yes"));

        // Print logs
        System.out.println("Transaction logs:");
        for (String log : stockWithdrawal.getTransactionLogs()) {
            System.out.println(log);
        }

        // Update inventory file
        inventory.updateFile();

        scanner.close();
    }
}
