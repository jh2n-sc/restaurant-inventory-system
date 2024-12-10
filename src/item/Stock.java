/**
 * Represents stock information for a specific item, including its quantity, unit of measurement,
 * arrival date, and expiry date. Provides methods to manage and check the stock's expiry status.
 * The stock is considered expired if its expiry date has passed, and it will warn if the stock is
 * set to expire the following day.
 *
 * <p>This class includes utilities for setting stock arrival and expiry dates, checking the stock
 * status, and printing detailed stock information to the console.</p>
 *
 * @see utils.AnsiAdd
 */

package src.item;

import src.utils.AnsiAdd;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Stock {
    
    private final String unit; // Unit of measurement for the stock (e.g., Cases, Bags)
    private Date invoice; // Date when the stock arrived
    private Date expiry; // Expiry date of the stock
    private final int amount; // Quantity of the stock
    private String stockSummary; // A summary string of the stock's details
    
    private boolean isExpired; // Indicates if the stock is expired
    private boolean warnExpiry; // Indicates if the stock expires tomorrow
    
    private static final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd"); // Date format for parsing
    private static Date presentDate; // Current date used for comparison
    
    private final String itemName; // The name of the item this stock represents
    
    /**
     * Creates a new Stock object for a specific item.
     *
     * @param itemName the name of the item
     * @param amount the quantity of the stock
     * @param unit the unit of measurement for the stock
     */
    public Stock(String itemName, int amount, String unit) {
        this.itemName = itemName;
        this.amount = amount;
        this.unit = unit;
        this.isExpired = false;
        this.warnExpiry = false;
        presentDate = new Date();
        this.stockSummary = unit;
    }
    
    /**
     * Sets the date when the stock arrived.
     *
     * @param date the date of arrival in "yyyy-MM-dd" format
     */
    public void setDateArrived(String date) {
        try {
            this.invoice = formatDate.parse(date);
        } catch (ParseException err) {
            err.printStackTrace();
            System.out.println("Cannot parse stock date of " + this.itemName);
        }
        this.stockSummary = this.stockSummary + " " + date;
    }
    
    /**
     * Sets the expiry date for the stock.
     *
     * @param date the expiry date in "yyyy-MM-dd" format
     */
    public void setExpiryDate(String date) {
        try {
            this.expiry = formatDate.parse(date);
        } catch (ParseException err) {
            err.printStackTrace();
            System.out.println("Cannot parse expiry date of " + this.itemName);
        }
        checkIfExpired();
        this.stockSummary = this.stockSummary + " " + date;
    }
    
    /**
     * Checks if the stock is expired or will expire tomorrow.
     */
    public void checkIfExpired() {
        int compareDate = this.expiry.compareTo(presentDate);
        long dateDifference = calcDateDifference();
        
        if (compareDate <= 0) {
            this.isExpired = true;
        } else {
            this.isExpired = false;
            this.warnExpiry = dateDifference == 0;
        }
    }
    
    /**
     * Calculates the difference in days between the expiry date and the current date.
     *
     * @return the difference in days
     */
    private long calcDateDifference() {
        long diff = this.expiry.getTime() - presentDate.getTime();
        diff = TimeUnit.MILLISECONDS.toDays(diff);
        return diff;
    }
    
    /**
     * Returns a summary string of the stock for storing or updating files.
     *
     * @return a string representing the stock summary
     */
    public String getStockSummary() {
        return amount + " " + this.stockSummary;
    }
    
    /**
     * Prints detailed information about the stock to the console, including the amount, arrival date,
     * expiry date, and expiry status.
     */
    public void printStock() {
        String expiryDate = formatDate.format(this.expiry);
        String arrivalDate = formatDate.format(this.invoice);
        
        System.out.print("\t");
        System.out.printf("%sAmount:%s %-2d %-3s", AnsiAdd.BLUE, AnsiAdd.RESET, this.amount, this.unit);
        System.out.printf("\t%sArrived on:%s %s", AnsiAdd.BLUE, AnsiAdd.RESET, arrivalDate);
        System.out.printf("\t%sExpiry:%s %s", AnsiAdd.BLUE, AnsiAdd.RESET, expiryDate);
        
        if (this.isExpired) {
            System.out.print("\t" + AnsiAdd.RED + "EXPIRED" + AnsiAdd.RESET);
        } else if (this.warnExpiry) {
            System.out.print("\t" + AnsiAdd.RED + "EXPIRES TOMORROW" + AnsiAdd.RESET);
        }
        
        System.out.println();
    }
}
