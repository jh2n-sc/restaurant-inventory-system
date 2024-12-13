import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionLogger {
    private String folderName = "receipts";
    private String currentFileName;

    // Constructor: Ensures the receipts folder exists and initializes the current file name
    public TransactionLogger() {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        this.currentFileName = generateFileName();
    }

    // Logs a transaction to the current receipt file
    public void log(String transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(folderName + "/" + currentFileName, true))) {
            writer.write(transaction);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generates a unique file name based on the current date and time
    private String generateFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return "Receipt_" + sdf.format(new Date()) + ".txt";
    }

    // Creates a new log file for a new session
    public void newLogFile() {
        this.currentFileName = generateFileName();
    }
}
