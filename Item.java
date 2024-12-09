import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Item {
    //main
    private String item_Name;
    private Queue<Stock> stocks = new LinkedList<>();
    private int totalStock;
    //main

    //flag
    public boolean stockExists; //to verify if an item has stock or not
    //flag

    public Item(String name){
        this.item_Name = name;
        this.stockExists = false;
        this.totalStock = 0;
    }

    public void addStock(String stock){
        Scanner scanString = new Scanner(stock);

        int size = scanString.nextInt();
        String unit = scanString.next();
        String date;

        Stock newstock = new Stock(this.item_Name, size, unit);
        
        date = scanString.next();
            newstock.setDateArrived(date);
        date = scanString.next();
            newstock.setExpiryDate(date);

        stocks.add(newstock); //enqueue

        if(!this.stockExists){
            this.stockExists = !this.stockExists;
        }

        this.totalStock = this.totalStock + size;

        scanString.close();
    }

    public String getName(){
        return item_Name;
    }

    public Stock getFront(){
        return stocks.peek();
    }

    //CLI section
    public void printItem(){
        System.out.println("item name: " + item_Name);

        Iterator<Stock> traverse = stocks.iterator();
        Stock stockitem;
        while(traverse.hasNext()){
            stockitem = traverse.next();
            stockitem.printStock();
        }

    }
    //CLI section

    //FX section
    //FX section

}