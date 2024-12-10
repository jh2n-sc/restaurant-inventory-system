package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import src.category.Category;

public class Inventory {

    private String restaurantName;
    private final ArrayList<Category> categories = new ArrayList<>();
    private static final String filePath = "./content/index.txt";
    private boolean categoriesExist;

    public Inventory(){
        this.categoriesExist =  false;
        initializeCategories();
    }

    private void initializeCategories(){
        File indexFile = new File(filePath);
        StringBuilder names = new StringBuilder();

        try(Scanner scanIndex = new Scanner(indexFile)){
            Category current;
            String storeName;
            this.restaurantName = scanIndex.nextLine();

            while(scanIndex.hasNextLine()){
                storeName = scanIndex.nextLine();
                current = new Category(storeName);
                categories.add(current);
                names.append(storeName).append(";");

                if(!this.categoriesExist){
                    this.categoriesExist = true;
                }
            }

        } catch(FileNotFoundException err){
            err.printStackTrace();
            System.out.println("!!File was not Found!!");
        }
       
       String categoryNames = names.toString();
    }

    public void updateFile(){
        try{
            FileWriter indexWrite = new FileWriter(filePath, false);
            BufferedWriter indexBuffer = new BufferedWriter(indexWrite);

            indexBuffer.write(restaurantName);
                indexBuffer.newLine();

            Category current;
            for(int i = 0; i < categories.size(); i++){
                current = categories.get(i);
                indexBuffer.write(current.category_name);
                
                if(i < categories.size() - 1){
                    indexBuffer.newLine();
                }

                current.updateFile();
            }

            indexBuffer.close();
        } catch(IOException err){
            System.out.println("ERROR. Did not UPDATE index.");
            err.printStackTrace();
        }
    }

    // CLI
    public void printInventoryList(){

        System.out.println(restaurantName);

        if(!categoriesExist){
            System.out.println("No Categories Initialized");
            return;
        }

        Category category;
       for (Category value : categories) {
          category = value;
          category.printCategoryList();
       }
    }
    // CLI

    public String getRestaurantName(){
        return this.restaurantName;
    }

    // FX
    public void addStackLayers(BorderPane inventoryPane){
        Category current;
        GridPane grid = FX_Utility.createGrid();
        StackPane stack = new StackPane();
            stack.setStyle(FX_Utility.fx);
        BorderPane innerPane; 

        for(int i = 0; i < categories.size(); i++){
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
