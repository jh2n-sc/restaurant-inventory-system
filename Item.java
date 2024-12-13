import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Item {
    public String item_Name;
    public LinkedList<Stock> stocks = new LinkedList<>();
    public double totalStock;
    public String unit;
    public boolean stockExists;
    public Stock latestStock;
    public String latestStockDate;

    public Item(String name) {
        this.item_Name = name;
        this.stockExists = false;
        this.totalStock = 0;
    }

    public void addStock(String stock) {
        Scanner scanString = new Scanner(stock);
    
        double size = scanString.nextDouble();
        String unit = scanString.next();
    
        String date;
    
        Stock newstock = new Stock(this.item_Name, size, unit);
    
        date = scanString.next();
        newstock.setDateArrived(date);
        setLatestStock(newstock, date);
    
        date = scanString.next();
        newstock.setExpiryDate(date);
    
        stocks.add(newstock);
    
        if (!this.stockExists) {
            this.stockExists = !this.stockExists;
        }
    
        this.totalStock = this.totalStock + size;
    
        scanString.close();
    
        sortList();
    }
    

    private void sortList() {
        if (stocks.size() <= 1) {
            return;
        }

        Collections.sort(stocks, Comparator.comparing(Stock::getExpiry));
    }

    private void setLatestStock(Stock latest, String date) {
        this.latestStock = latest;
        this.latestStockDate = date;
    }

    public String getItem_Name() {
        return item_Name;
    }

    public String getTotalStock() {
        String unit = this.latestStock.unit;
        int total = (int) this.totalStock;
        if (total == this.totalStock) {
            return total + " " + unit;
        }
        return this.totalStock + " " + unit;
    }

    public Stock getLatestStock() {
        return this.latestStock;
    }

    public String getLatestStockDate() {
        return this.latestStockDate;
    }

    public Stock getFront() {
        return stocks.peek();
    }

    public int getQueueSize() {
        return stocks.size();
    }

    public String getItemStockSummary() {
        String summary = "";
        Iterator<Stock> traverse = this.stocks.iterator();
        Stock current;
        while (traverse.hasNext()) {
            current = traverse.next();
            summary = summary + current.getStockSummary();
            if (traverse.hasNext()) {
                summary = summary + "\n";
            }
        }
        return summary;
    }

    public double getTotalStockAmount (){
        return totalStock;
    }

    public void removeStock (double amount){
        if (totalStock >= amount){
            totalStock -= amount;
        }
    }

    public void printItemStock() {
        Iterator<Stock> stockIterator = stocks.iterator();
        if (stockIterator.hasNext()){
            Stock stock = stockIterator.next();
            stock.viewStock();
        }
    }
}