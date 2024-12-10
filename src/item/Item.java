/**
 * Represents an inventory item, including its name and associated stock information.
 * The class allows adding stock, retrieving stock summaries, and printing item details.
 */

package src.item;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class Item {
   
   /**
    * The name of the item.
    */
   private final String item_Name;
   
   /**
    * A queue to store stock entries for the item.
    */
   private final Queue<Stock> stocks = new LinkedList<>();
   
   /**
    * The total stock quantity of the item.
    */
   private int totalStock;
   
   /**
    * A flag indicating if the item has any stock.
    */
   public boolean stockExists;
   
   /**
    * Iterator for accessing the stock entries in the queue.
    */
   private final Iterator<Stock> traverse = stocks.iterator();
   
   /**
    * Constructs an Item with the specified name.
    *
    * @param name The name of the item.
    */
   public Item(String name) {
      this.item_Name = name;
      this.stockExists = false;
      this.totalStock = 0;
   }
   
   /**
    * Gets the name of the item.
    *
    * @return The name of the item.
    */
   public String getName() {
      return item_Name;
   }
   
   /**
    * Retrieves the stock entry at the front of the stock queue.
    *
    * @return The stock at the front of the queue.
    */
   public Stock getFront() {
      return stocks.peek();
   }
   
   /**
    * Gets the number of stock entries for the item.
    *
    * @return The number of stock entries.
    */
   public int getQueueSize() {
      return stocks.size();
   }
   
   /**
    * Adds a new stock entry to the item based on the provided string.
    * The stock string should contain the quantity, unit, arrival date, and expiry date.
    *
    * @param stock A string representing the stock data in the format: size unit dateArrived expiryDate.
    */
   public void addStock(String stock) {
      Scanner scanString = new Scanner(stock);
      int size = scanString.nextInt();
      String unit = scanString.next();
      String date;
      
      Stock newStock = new Stock(this.item_Name, size, unit);
      date = scanString.next();
      newStock.setDateArrived(date);
      date = scanString.next();
      newStock.setExpiryDate(date);
      
      stocks.add(newStock); //enqueue
      
      if (!this.stockExists) {
         this.stockExists = true;
      }
      
      this.totalStock = this.totalStock + size;
      scanString.close();
   }
   
   /**
    * Summarizes all the stock details into a single string.
    *
    * @return A string containing the summary of all stock entries.
    */
   public String getItemStockSummary() {
      StringBuilder summary = new StringBuilder();
      Stock current;
      while (this.traverse.hasNext()) {
         current = traverse.next();
         summary.append(current.getStockSummary());
         
         if (this.traverse.hasNext()) {
            summary.append("\n");
         }
      }
      return summary.toString();
   }
   
   /**
    * Prints the details of the item and all its associated stock entries to the console.
    */
   public void printItem() {
       System.out.println("item name: " + item_Name);
       
       for (Stock stockitem : stocks) {
           stockitem.printStock();
       }
   }

   
   
   //CLI section
   
   //FX section
   //FX section
   
}