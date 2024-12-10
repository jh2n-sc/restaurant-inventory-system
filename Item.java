import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Item {
    //main
    private String item_Name;
    private Queue<Stock> stocks = new LinkedList<>();
    private double totalStock;
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

        double size = scanString.nextDouble();
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

    public int getQueueSize(){
        return stocks.size();
    }

    public String getItemStockSummary(){ //summarizes all the stock into one string
        String summary = "";

        Iterator<Stock> traverse = this.stocks.iterator();

        Stock current;
        while(traverse.hasNext()){
            current = traverse.next();
            summary = summary + current.getStockSummary();

            if(traverse.hasNext()){
                summary = summary + "\n";
            }
        }

        return summary;
    }

    //CLI section
    public void printItem(){
        System.out.println("item name: " + item_Name);

        Iterator<Stock> traverse = this.stocks.iterator();

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