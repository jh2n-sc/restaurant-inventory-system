import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Stock {

    // main
    private String unit; //Cases/Bags/Boxes/Crates/Pallets/Tubs/Bottles/Jars
    private Date invoice; //Date of Arrival?
    private Date expiry; //Obvious
    private int amount;
    private boolean isExpired;
    // main

    //static
    private static SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
    //static

    private String itemName;

    public Stock(String itemName, int amount, String unit){
        this.itemName = itemName;
        this.amount = amount;
        this.unit = unit;
        this.isExpired = false;
    }

    public void setDateArrived(String date){
        try{
            this.invoice = formatdate.parse(date);
        } catch(ParseException err){
            err.printStackTrace();
            System.out.println("Cannot parse stock date of " + this.itemName);
        }
    }

    public void setExpiryDate(String date){
        try{
            this.expiry = formatdate.parse(date);
        } catch(ParseException err){
            err.printStackTrace();
            System.out.println("Cannot parse expiry date of " + this.itemName);
        }


    }

    public boolean checkifExpired(){
        
    }
}