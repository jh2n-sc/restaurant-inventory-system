import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InventoryLogger {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logWithdraw(String itemName, double amount, String category) {
        // Create a log file name in the format "Log (date issued).txt"
        String logFileName = "./log/Log (" + LocalDate.now() + ").txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true))) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.write(String.format("%s - Withdrawn: %.2f units of %s from %s category%n", timestamp, amount, itemName, category));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to log file: " + logFileName);
        }
    }
}
