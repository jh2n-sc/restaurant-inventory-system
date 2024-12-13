import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Stock {

    public String unit;
    private Date invoice;
    private Date expiry;
    private double amount;
    private String stockSummary;
    private boolean isExpired;
    private boolean warnExpiry;
    private static SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
    private static Date presentDate;

    private String itemName;

    public Stock(String itemName, double amount, String unit) {
        this.itemName = itemName;
        this.amount = amount;
        this.unit = unit;
        this.isExpired = false;
        this.warnExpiry = false;
        presentDate = new Date();
        this.stockSummary = unit;
    }

    public void setDateArrived(String date) {
        try {
            this.invoice = formatdate.parse(date);
        } catch (ParseException err) {
            err.printStackTrace();
            System.out.println("Cannot parse stock date of " + this.itemName);
        }

        // System.out.println("PresentDate : " + formatdate.format(presentDate));
        this.stockSummary = this.stockSummary + " " + date;
    }

    public void setExpiryDate(String date) {
        try {
            this.expiry = formatdate.parse(date);
        } catch (ParseException err) {
            err.printStackTrace();
            System.out.println("Cannot parse expiry date of " + this.itemName);
        }

        checkifExpired();
        // System.out.println("expired: " + this.isExpired);
        this.stockSummary = this.stockSummary + " " + date;
    }

    public void checkifExpired() {
        int compareDate = this.expiry.compareTo(presentDate);
        long dateDifference = calcDateDifference();

        if (compareDate <= 0) {
            this.isExpired = true;
        } else {
            this.isExpired = false;

            if (dateDifference == 0) {
                this.warnExpiry = true;
            } else {
                this.warnExpiry = false;
            }
        }
    }

    private long calcDateDifference() {
        long diff;
        diff = this.expiry.getTime() - presentDate.getTime(); // millisecond format
        diff = TimeUnit.MILLISECONDS.toDays(diff); // day format

        // System.out.println("Difference: " + diff);

        return diff;
    }

    public String getStockSummary() { // returns string for storing during file updates
        return amount + " " + this.stockSummary;
    }

    public String getStringAmount() {
        return this.amount + " " + unit;
    }

    public double getAmount (){
        return this.amount;
    };

    public String getInvoice() {
        return formatdate.format(this.invoice);
    }

    public String getExpiry() {
        return formatdate.format(this.expiry);
    }

    public String getItemName (){
        return this.itemName;
    }

    public void printStock() {
        String expiryDate = formatdate.format(this.expiry);
        String arrivalDate = formatdate.format(this.invoice);

        System.out.print("\t");
        System.out.printf("%sAmount:%s %s %-3s", AnsiAdd.BLUE, AnsiAdd.RESET, this.amount, this.unit);
        System.out.printf("\t%sArrived on:%s %s", AnsiAdd.BLUE, AnsiAdd.RESET, arrivalDate);
        System.out.printf("\t%sExpiry:%s %s", AnsiAdd.BLUE, AnsiAdd.RESET, expiryDate);

        if (this.isExpired) {
            System.out.print("\t" + AnsiAdd.RED + "EXPIRED" + AnsiAdd.RESET);
        } else if (this.warnExpiry) {
            System.out.print("\t" + AnsiAdd.RED + "Expires Tomorrow" + AnsiAdd.RESET);
        }

        System.out.println();
    }

    public void viewStock (){
        System.out.println("        Amount: " + amount + " " + unit);
        System.out.println("        Arrived on: " + invoice);
        System.out.println("        Expiry: " + expiry);
        if (this.isExpired){
            System.out.println("        EXPIRED");
        } else if (this.warnExpiry){
            System.out.println("        Expires Tomorrow");
        }
    }

    public void withdraw(double amount) {
        if (this.amount >= amount) {
            this.amount -= amount;
        } else {
            System.out.println("Insufficient stock amount to withdraw.");
        }
    }
    
    public void deposit(double amount) {
        this.amount += amount;
    }
    
}
