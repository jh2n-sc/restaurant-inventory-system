import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Inventory {

    private String restaurantName;
    private ArrayList<Category> categories = new ArrayList<>();
    public static String categoryNames;
    private static String filePath = "./content/index.txt";
    private boolean categoriesExist;

    private Label prevClickedCategory;
    private Label optionClicked;
    private BorderPane transactionPane = new BorderPane();
    private StackPane stack;

    public Inventory(){
        this.categoriesExist =  false;
        createPane(this.transactionPane);
        this.stack = new StackPane();
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

        
        addCategoryTab(categories.size() + 1, grid);
        
        for(int i = categories.size() - 1; i >= 0; i--){

            BorderPane innerPane = new BorderPane();
            current = categories.get(i);
            Label label = FX_Utility.createTabLabel(current.category_name);
                    grid.add(label, i, 0);
            this.stack.getChildren().add(innerPane);
            current.addTable(innerPane, inventoryPane);

            addStackFunctions(this.stack, innerPane, label);

            if(i == 0){
                label.setStyle("-fx-border-radius: 10px 10px 0 0; -fx-background-color: rgb(67, 20, 7); -fx-background-radius: 10px 10px 0 0;");
            }
        }

        inventoryPane.setTop(grid);
        inventoryPane.setCenter(this.stack);

    }

    private void addCategoryTab(int columnIndex, GridPane grid){
        Label label = FX_Utility.createTabLabel("+");
            label.setStyle("-fx-border-radius: 10px 10px 0 0; -fx-background-color: rgb(177, 30, 17); -fx-background-radius: 10px 10px 0 0;");
            grid.add(label, columnIndex, 0);

        BorderPane addCategoryPane = new BorderPane();
            Label title = new Label("Add Category");
                title.setFont(Font.font("General sans", FontWeight.BLACK, 80));
                title.setTextFill(Color.WHITE);
                title.setPrefHeight(30);
                title.setPrefWidth(Double.MAX_VALUE);
                title.setTextAlignment(TextAlignment.CENTER);
                title.setAlignment(Pos.CENTER);
                title.setPadding(new Insets(20, 4, 20, 4));
                title.setStyle(FX_Utility.fx);
            addCategoryPane.setTop(title);

            TextField field = new TextField();
                field.maxWidthProperty().bind(addCategoryPane.widthProperty().multiply(0.5));
                field.setPrefHeight(50);
                field.setText
            addCategoryPane.setCenter(field);
                
            addCategoryPane.setStyle(FX_Utility.fx);

        this.stack.getChildren().add(addCategoryPane);
            

        addTransactionPaneFunctions(label, addCategoryPane);
    }

    private void addStackFunctions(StackPane stack, BorderPane innerPane, Label label){
        label.setOnMouseClicked(event -> {

            if(this.prevClickedCategory == label){
                return;
            }

            FX_Utility.changeColorOnClick(label, this.prevClickedCategory);

            stack.getChildren().remove(innerPane);
            stack.getChildren().add(innerPane);
            this.transactionPane.setCenter(null);
            this.optionClicked = null;
            System.out.println("clicked " + event.getClass());

            this.prevClickedCategory = label;
        });

        this.prevClickedCategory = label;
    }

    private void addTransactionPaneFunctions(Label label, BorderPane contentPane){
        label.setOnMouseClicked(clicked -> {
            if(label == this.optionClicked){
                    return;
            }

            this.optionClicked = label;
            this.transactionPane.setCenter(contentPane);
            this.stack.getChildren().remove(this.transactionPane);
            this.stack.getChildren().add(this.transactionPane);

            System.out.println("Transaction: " + clicked.getTarget());
        });
    }

    private void createPane(BorderPane pane){
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setStyle("-fx-background-color: rgb(67, 20, 7)");
    }

    // FX
}
