import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Inventory {

    private String restaurantName;
    private ArrayList<Category> categories = new ArrayList<>();
    public static String categoryNames;
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


    public void addCategory(String name){
        Category newcategory = new Category(name);
        categories.add(newcategory);
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

        int columnIndex = categories.size();    
        for(int i = 0; i < categories.size(); i++){
            BorderPane innerPane = new BorderPane();
            current = categories.get(i);
            Label label = FX_Utility.createTabLabel(current.category_name);
                    grid.add(label, columnIndex, 0);
            stack.getChildren().add(innerPane);
            current.addTable(innerPane, inventoryPane);
            columnIndex--;

            addStackFunctions(stack, innerPane, label);
        }
        

        inventoryPane.setTop(grid);
        inventoryPane.setCenter(stack);

    }

    private void addStackFunctions(StackPane stack, BorderPane innerPane, Label label){
        label.setOnMouseClicked(event -> {
            stack.getChildren().remove(innerPane);
            stack.getChildren().add(innerPane);
            System.out.println("clicked " + event.getClass());
        });
    }
    // FX
}
