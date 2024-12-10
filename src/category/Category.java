/**
 * The Category class represents a category of items in a restaurant inventory system.
 */
package src.category;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import src.item.Item;


public class Category {
   
   /**
    * The name of the category.
    */
   public String category_name;
   
   /**
    * A list of items in the category.
    */
   private final ArrayList<Item> itemList = new ArrayList<>();
   
   /**
    * The number of items in the category.
    */
   private int item_Number;
   
   /**
    * Indicates if the category is empty.
    */
   private boolean isEmpty;
   
   /**
    * The directory path for the category file.
    */
   private String directory = "./content/";
   
   /**
    * Constructs a Category object with the specified name.
    * Initializes the associated file for storing category data.
    *
    * @param name The name of the category.
    */
   public Category(String name) {
      this.category_name = name;
      this.item_Number = 0;
      this.isEmpty = true;
      
      if (name.contains(" ")) {
         name = name.replace(" ", "_");
      }
      
      this.directory = this.directory + name + ".txt";
      initializeItems(new File(this.directory));
   }
   
   /**
    * Initializes items from a given file.
    *
    * @param categoryItems The file containing category item data.
    */
   private void initializeItems(File categoryItems) {
      try (Scanner scanItems = new Scanner(categoryItems)) {
         Item current;
         String store;
         boolean isEmpty = true;
         int number = 0;
         
         while (scanItems.hasNextLine()) {
            store = scanItems.nextLine();
            current = new Item(store);
            
            while (scanItems.hasNextInt()) {
               store = scanItems.nextLine();
               current.addStock(store);
            }
            
            itemList.add(current);
            
            if (isEmpty) {
               isEmpty = false;
            }
            number++;
         }
         
         this.isEmpty = isEmpty;
         this.item_Number = number;
         
      } catch (FileNotFoundException err) {
         err.printStackTrace();
         System.out.println("Did not find Category file for " + this.category_name);
      }
   }
   
   /**
    * Updates the category file to reflect the current state of items in the category.
    */
   public void updateFile() {
      int flagValue = 0;
      try (BufferedWriter categoryBuffer = new BufferedWriter(new FileWriter(this.directory, false))) {
         Item current;
         for (Item item : itemList) {
            current = item;
            categoryBuffer.write(current.getName());
            categoryBuffer.newLine();
            if (current.stockExists) {
               categoryBuffer.write(current.getItemStockSummary());
               categoryBuffer.newLine();
            }
            flagValue++;
         }
      } catch (IOException err) {
         System.out.println("Error. Did not UPDATE category " + this.category_name);
         System.out.println("Flag value: " + flagValue);
         err.printStackTrace();
      }
   }
   
   /**
    * Prints the list of items in the category to the console.
    * If the category is empty, "[Empty]" is displayed.
    */
   public void printCategoryList() {
      System.out.println("Category: " + category_name);
      if (isEmpty) {
         System.out.println("[Empty]");
         return;
      }
      
      for (Item item : itemList) {
         item.printItem();
      }
   }
}
