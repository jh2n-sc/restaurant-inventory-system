import java.util.List;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        // Initialize Inventory and load existing categories and items
        Inventory inventory = new Inventory();
        Scanner scanner = new Scanner(System.in);

        // Initialize StockWithdrawal with a single TransactionLogger
        TransactionLogger transactionLogger = new TransactionLogger();
        StockWithdrawal stockWithdrawal = new StockWithdrawal(transactionLogger, inventory);

        String anotherOperation;

        try {
            do {
                // Display available commands
                System.out.println("Available Commands: withdraw, deposit, exit");

                // Get user command
                System.out.println("Enter command: ");
                String command = scanner.next().toLowerCase();

                if (command.equals("exit")) {
                    break;
                }

                if (command.equals("withdraw") || command.equals("deposit")) {
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

                    // Display current stocks before withdrawal
                    if (command.equals("withdraw")) {
                        stockWithdrawal.displayStocks(item);
                    }

                    // Withdraw or deposit amount
                    System.out.println("Enter the amount to " + command + " from " + item.getItem_Name() + ": ");
                    double amount = scanner.nextDouble();

                    if (command.equals("withdraw")) {
                        stockWithdrawal.withdrawStock(item, amount);
                    } else {
                        stockWithdrawal.depositStock(item, amount);
                    }

                    // Display current stocks after operation
                    stockWithdrawal.displayStocks(item);
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
        } finally {
            // Clean up stocks with zero amounts before exiting
            stockWithdrawal.cleanUpStocks();
            System.out.println("Cleaned up stocks with zero amounts.");

            // Update inventory file
            inventory.updateFile();

            scanner.close();
        }
    }
}
