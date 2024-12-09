import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Inventory {

    private String restaurantName;
    private ArrayList<Category> categories = new ArrayList<>();
    private static String categoryNames;
    private static String filePath = "./content/index.txt";
    private boolean categoriesExist;

    public Inventory(){
        this.categoriesExist =  false;
        initializeCategories();
    }

    private void initializeCategories(){
        File indexFile = new File(filePath);
        String names = "";

        try(Scanner scanIndex = new Scanner(indexFile)){
            Category current;
            String storename;
            this.restaurantName = scanIndex.nextLine();

            while(scanIndex.hasNextLine()){
                storename = scanIndex.nextLine();
                current = new Category(storename);
                categories.add(current);
                names = names + storename + ";";

                if(!this.categoriesExist){
                    this.categoriesExist = true;
                }
            }

        } catch(FileNotFoundException err){
            err.printStackTrace();
            System.out.println("!!File was not Found!!");
        }

        categoryNames = names;
    }

    // CLI
    public void printInventoryList(){

        System.out.println(restaurantName);

        if(!categoriesExist){
            System.out.println("No Categories Initialized");
            return;
        }

        Category category;
        for(int i = 0; i < categories.size(); i++){
            category = categories.get(i);
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
        //     pane.setStyle(FX_Utility.fx);
        //     pane.setTop(grid);
        
        // BorderPane innerPane = new BorderPane();
        //     innerPane.setStyle(FX_Utility.fx);
        // pane.setCenter(innerPane);

        // inventoryStack.getChildren().addAll(pane);
        //     StackPane.setAlignment(pane, Pos.CENTER);

        inventoryPane.setTop(grid);
        inventoryPane.setCenter(stack);

    }
    // FX
}
