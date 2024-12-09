import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Stock {

    // main
    private String unit; //Cases/Bags/Boxes/Crates/Pallets/Tubs/Bottles/Jars
    private Date invoice; //Date of Arrival?
    private Date expiry; //Obvious
    private int amount;
    private boolean isExpired;
    private String expiryPrompt;
    // main

    //static
    private static SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
    private static Date presentDate;
    //static

    private String itemName;

    public Stock(String itemName, int amount, String unit){
        this.itemName = itemName;
        this.amount = amount;
        this.unit = unit;
        this.isExpired = false;
        presentDate = new Date();
    }

    public void setDateArrived(String date){
        try{
            this.invoice = formatdate.parse(date);
        } catch(ParseException err){
            err.printStackTrace();
            System.out.println("Cannot parse stock date of " + this.itemName);
        }

        System.out.println("PresentDate : " + formatdate.format(presentDate));
    }

    public void setExpiryDate(String date){
        try{
            this.expiry = formatdate.parse(date);
        } catch(ParseException err){
            err.printStackTrace();
            System.out.println("Cannot parse expiry date of " + this.itemName);
        }
        
        checkifExpired();
        System.out.println("expired: " + this.isExpired);
    }

    public void checkifExpired(){
        int compareDate = this.expiry.compareTo(presentDate);
        long dateDifference = calcDateDifference();

        if(compareDate <= 0 || dateDifference == 0){
            this.isExpired = true;
            if(dateDifference == 0){
                expiryPrompt = "Expires Tomorrow";
            } else {
                expiryPrompt = "EXPIRED";
            }
        } else {
            this.isExpired = false;
        }
    }

    private long calcDateDifference(){
        long diff;
            diff = this.expiry.getTime()- presentDate.getTime(); //millisecond format
            diff = TimeUnit.MILLISECONDS.toDays(diff); // day format

            System.out.println("Difference: " + diff);

        return diff;
    }

    public void printStock(){
        String expiryDate = formatdate.format(this.expiry);
        String arrivalDate = formatdate.format(this.invoice);


        System.out.print("\t");
        System.out.printf("%sAmount:%s %-2d %-3s",AnsiAdd.BLUE, AnsiAdd.RESET, this.amount, this.unit);
        System.out.printf("\t%sArrived on:%s %s", AnsiAdd.BLUE, AnsiAdd.RESET, arrivalDate);
        System.out.printf("\t%sExpiry:%s %s", AnsiAdd.BLUE, AnsiAdd.RESET, expiryDate);

        if(this.isExpired){
            System.out.print("\t" + AnsiAdd.RED + this.expiryPrompt + AnsiAdd.RESET);
        }

        System.out.println();
    }
}