import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionLogger {
    private String folderName = "receipts";
    private String currentFileName;

    public TransactionLogger() {
        // Ensure the receipts folder exists
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        // Initialize the file name
        this.currentFileName = generateFileName();
    }

    public void log(String transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(folderName + "/" + currentFileName, true))) {
            writer.write(transaction);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return "Receipt_" + sdf.format(new Date()) + ".txt";
    }

    public void newLogFile() {
        this.currentFileName = generateFileName();
    }
}
