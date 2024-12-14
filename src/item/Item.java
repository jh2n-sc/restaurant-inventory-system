/**
 * Represents an inventory item, including its name and associated stock information.
 * The class allows adding stock, retrieving stock summaries, and printing item details.
 */

package src.item;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import src.utils.FX_Utility;

public class Item {
   //main
   public String item_Name;
   // public Queue<Stock> stocks = new LinkedList<>();
   public LinkedList<Stock> stocks = new LinkedList<>();
   public double totalStock;
   public String unit;
   //main
   
   //flag
   public boolean stockExists; //to verify if an item has stock or not
   //flag
   
   //remember //for sorting purposes
   public Stock latestStock;
   public String latestStockDate;
   //
   
   //table fx
   private final TableView<Stock> stockTable;
   private boolean tablePrefWidth = false;
   
   public Item(String name){
      this.item_Name = name;
      this.stockExists = false;
      this.totalStock = 0;
      this.stockTable = FX_Utility.createTable();
   }
   
   public void addStock(String stock){
      Scanner scanString = new Scanner(stock);
      
      double size = scanString.nextDouble();
      String unit = scanString.next();
      String dateArrival = scanString.next();
      String dateExpiry = scanString.next();
      
      Stock current = checkIfStockExists(dateArrival, dateExpiry);
      
      if(current != null){
         current.setAmount(size);
      } else {
         Stock newstock = new Stock(this.item_Name, size, unit);
         newstock.setDateArrived(dateArrival);
         newstock.setExpiryDate(dateExpiry);
         this.stocks.add(newstock); //add to the list
         
         sortList();
      }
      
      if(!this.stockExists){
         this.stockExists = true;
      }
      
      
      scanString.close();
      this.totalStock = this.totalStock + size;
   }
   
   private Stock checkIfStockExists(String dateArrival, String dateExpiry){
      
      ListIterator<Stock> iterateStock = stocks.listIterator();
      Stock current;
      while(iterateStock.hasNext()){
         current = iterateStock.next();
         if(dateArrival.equals(current.getInvoice())){
            if(dateExpiry.equals(current.getExpiry())){
               return current;
            }
         }
      }
      return null;
   }
   
   private void sortList(){
      if(this.stocks.size() > 1){
         this.stocks.sort(Comparator.comparing(Stock::getExpiry));
      }
      setLatestStock(this.stocks.getLast(), this.stocks.getLast().getInvoice());
   }
   
   private void setLatestStock(Stock latest, String date){
      this.latestStock = latest;
      this.latestStockDate = date;
   }
   
   public String getItem_Name(){//for tableview access
      return item_Name;
   }
   
   public String getTotalStock(){//for tableview access
      
      String unit = this.latestStock.unit;
      
      int total = (int) this.totalStock;
      if(total == this.totalStock){
         return total + " " + unit;
      }
      
      return this.totalStock + " " + unit;
   }
   
   public Stock getLatestStock(){
      return this.latestStock;
   }
   
   
   public String getLatestStockDate(){//for tableview access
      return this.latestStockDate;
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
   
   public void updateTable(){
      this.stockTable.getItems().clear(); //maybe this is not needed
      ObservableList<Stock> data = FXCollections.observableArrayList(this.stocks);
      this.stockTable.setItems(data);
   }
   
   //CLI section
   public void printItem(){
      System.out.println("item name: " + item_Name);
      
      Iterator<Stock> traverse = this.stocks.iterator();
      
      Stock stockItem;
      while(traverse.hasNext()){
         stockItem = traverse.next();
         stockItem.printStock();
      }
   }
   //CLI section
   
   //FX section
   public void addStockTable(BorderPane viewPane, BorderPane headerPane){
      if(!this.tablePrefWidth){
         this.stockTable.prefWidthProperty().bind(viewPane.widthProperty());
         tablePrefWidth = true;
      }
      
      
      ObservableList<Stock> stockList = FXCollections.observableArrayList(this.stocks);
      
      this.stockTable.setItems(stockList);
      
      
      addHeader(headerPane);
      viewPane.setCenter(stockTable);
   }
   
   private void addHeader(BorderPane viewPane){
      VBox box = new VBox();
      box.setSpacing(4);
      int totalInt = (int) this.totalStock;
      String totalString = "";
      if(totalInt == this.totalStock){
         totalString = totalInt + " " + this.latestStock.unit;
      } else {
         totalString = this.totalStock + " " + this.latestStock.unit;
      }
      
      Label name = FX_Utility.createTabLabel("Item: " + this.item_Name);
      name.setFont(Font.font("sans serif", FontWeight.BLACK, 25));
      name.setStyle(null);
      name.setTextAlignment(TextAlignment.LEFT);
      name.setMaxWidth(Double.MAX_VALUE);
      Label total = FX_Utility.createTabLabel("Total: " + totalString);
      total.setFont(Font.font("sans serif", FontWeight.BLACK, 25));
      total.setStyle(null);
      total.setTextAlignment(TextAlignment.LEFT);
      total.setMaxWidth(Double.MAX_VALUE);
      
      box.getChildren().addAll(name, total);
      
      box.prefWidthProperty().bind(viewPane.widthProperty());
      viewPane.setCenter(box);
   }
   //FX section
   
}