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
    
    // main
    public String unit; //Cases/Bags/Boxes/Crates/Pallets/Tubs/Bottles/Jars //constant
    private Date invoice; //Date of Arrival //constant
    private Date expiry; //Obvious //constant
    private double amount;
    private String stockSummary; //constant values in a string
    // main
    
    // sub
    private boolean isExpired; //if the stock is already expired
    private boolean warnExpiry; // if the stock expires the next day
    // sub
    
    //static
    private static final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    private static Date presentDate;
    //static
    
    private final String itemName;
    
    public Stock(String itemName, double amount, String unit){
        this.itemName = itemName;
        this.amount = amount;
        this.unit = unit;
        this.isExpired = false;
        this.warnExpiry =false;
        
        presentDate = new Date();
        
        this.stockSummary = unit;
    }
    
    public void setAmount(double amount){
        this.amount = this.amount + amount;
    }
    
    public void setDateArrived(String date){
        try{
            this.invoice = formatDate.parse(date);
        } catch(ParseException err){
            err.printStackTrace();
            System.out.println("Cannot parse stock date of " + this.itemName);
        }
        
        System.out.println("PresentDate : " + formatDate.format(presentDate));
        this.stockSummary = this.stockSummary + " " + date;
    }
    
    public void setExpiryDate(String date){
        try{
            this.expiry = formatDate.parse(date);
        } catch(ParseException err){
            err.printStackTrace();
            System.out.println("Cannot parse expiry date of " + this.itemName);
        }
        
        checkIfExpired();
        System.out.println("expired: " + this.isExpired);
        this.stockSummary = this.stockSummary + " " + date;
    }
    
    public void checkIfExpired(){
        int compareDate = this.expiry.compareTo(presentDate);
        long dateDifference = calcDateDifference();
        
        if(compareDate <= 0){//I feel like im overcomplicating this block
            this.isExpired = true;
            this.warnExpiry = false;
        } else {
            this.isExpired = false;
            
            if(dateDifference <= 4 && dateDifference >= 0) {
                this.warnExpiry = true;
            } else {
                this.warnExpiry = false;
            }
        }
    }
    
    private long calcDateDifference(){
        long diff;
        diff = this.expiry.getTime()- presentDate.getTime(); //millisecond format
        diff = TimeUnit.MILLISECONDS.toDays(diff); // day format
        
        System.out.println("Difference: " + diff);
        
        return diff;
    }
    
    public String getStockSummary(){ //returns string for storing during file updates
        return amount + " " + this.stockSummary;
    }
    
    public String getAmount(){
        return this.amount + " " + unit;
    }
    
    public String getInvoice(){
        return formatDate.format(this.invoice);
    }
    
    public String getExpiry(){
        
        
        return formatDate.format(this.expiry);
    }
    
    public String isExpired(){
        if(isExpired){
            return "expired";
        } else if(warnExpiry){
            return "warn";
        }
        return null;
    }
    
    public void printStock(){
        String expiryDate = formatDate.format(this.expiry);
        String arrivalDate = formatDate.format(this.invoice);
        
        
        System.out.print("\t");
        System.out.printf("%sAmount:%s %s %-3s",AnsiAdd.BLUE, AnsiAdd.RESET, this.amount, this.unit);
        System.out.printf("\t%sArrived on:%s %s", AnsiAdd.BLUE, AnsiAdd.RESET, arrivalDate);
        System.out.printf("\t%sExpiry:%s %s", AnsiAdd.BLUE, AnsiAdd.RESET, expiryDate);
        
        if(this.isExpired){
            System.out.print("\t" + AnsiAdd.RED + "EXPIRED" + AnsiAdd.RESET);
        } else if(this.warnExpiry){
            System.out.print("\t" + AnsiAdd.RED + "Expires Tomorrow" + AnsiAdd.RESET);
        }
        
        System.out.println();
    }
}