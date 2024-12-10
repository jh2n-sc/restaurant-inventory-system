/**
 * The Inventory class represents the inventory system for a restaurant.
 */
package src;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import src.category.Category;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;


public class Inventory {
   
   /**
    * The name of the restaurant.
    */
   private String restaurantName;
   
   /**
    * A list of categories in the inventory.
    */
   private final ArrayList<Category> categories = new ArrayList<>();
   
   /**
    * The file path for the main inventory file.
    */
   private static final String filePath = "./content/index.txt";
   
   /**
    * Indicates whether any categories exist in the inventory.
    */
   private boolean categoriesExist;
   
   
   
   
   /**
    * Constructs an Inventory object and initializes categories by reading from the file.
    */
   public Inventory() {
      this.categoriesExist = false;
      initializeCategories();
   }
   

    
    /**
     * Retrieves the name of the restaurant.
     *
     * @return The restaurant name.
     */
    public String getRestaurantName() {
        return this.restaurantName;
    }
    
    
    
    /**
     * Reads categories from the inventory file and populates the categories list.
     * Also initializes the restaurant name.
     */
   private void initializeCategories() {
      File indexFile = new File(filePath);
      StringBuilder names = new StringBuilder();
      
      try (Scanner scanIndex = new Scanner(indexFile)) {
         this.restaurantName = scanIndex.nextLine();
         
         while (scanIndex.hasNextLine()) {
            String storeName = scanIndex.nextLine();
            Category current = new Category(storeName);
            categories.add(current);
            names.append(storeName).append(";");
            
            if (!this.categoriesExist) {
               this.categoriesExist = true;
            }
         }
         
      } catch (FileNotFoundException err) {
         err.printStackTrace();
         System.out.println("!!File was not Found!!");
      }
   }
  
   
   
   
   /**
    * Updates the inventory file with the current state of categories and their data.
    */
   public void updateFile() {
      try (BufferedWriter indexBuffer = new BufferedWriter(new FileWriter(filePath, false))) {
         indexBuffer.write(restaurantName);
         indexBuffer.newLine();
         
         for (int i = 0; i < categories.size(); i++) {
            Category current = categories.get(i);
            indexBuffer.write(current.category_name);
            
            if (i < categories.size() - 1) {
               indexBuffer.newLine();
            }
            
            current.updateFile();
         }
         
      } catch (IOException err) {
         System.out.println("ERROR. Did not UPDATE index.");
         err.printStackTrace();
      }
   }
   
  
  
  
   /**
    * Prints the inventory list to the console.
    * Includes the restaurant name and all categories with their details.
    */
   public void printInventoryList() {
      System.out.println(restaurantName);
      
      if (!categoriesExist) {
         System.out.println("No Categories Initialized");
         return;
      }
      
      for (Category category : categories) {
         category.printCategoryList();
      }
   }
   
   
   // FX
   public void addStackLayers(BorderPane inventoryPane) {
      Category current;
      GridPane grid = FX_Utility.createGrid();
      StackPane stack = new StackPane();
      stack.setStyle(FX_Utility.fx);
      BorderPane innerPane;
      
      for (int i = 0; i < categories.size(); i++) {
         current = categories.get(i);
         FX_Utility.createTabLabel(current.category_name, i, grid);
         innerPane = new BorderPane();
         stack.getChildren().add(innerPane);
      }
      
      
      // BorderPane pane = new BorderPane();
      //     pane.setPadding(new Insets(0));
      //     pane.setStyle(src.FX_Utility.fx);
      //     pane.setTop(grid);
      
      // BorderPane innerPane = new BorderPane();
      //     innerPane.setStyle(src.FX_Utility.fx);
      // pane.setCenter(innerPane);
      
      // inventoryStack.getChildren().addAll(pane);
      //     StackPane.setAlignment(pane, Pos.CENTER);
      
      inventoryPane.setTop(grid);
      inventoryPane.setCenter(stack);
      
   }
   // FX
}
