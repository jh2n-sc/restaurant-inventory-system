import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private GridPane grid;
    // private BorderPane storeInventoryPane;

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

        categoryNames = names.toLowerCase();
    }


    public void addCategory(String name){
        Category newcategory = new Category(name);
        categories.add(newcategory);
        categoryNames = categoryNames + ";" + name;
        addGridTab(this.grid, (BorderPane) this.grid.getParent());
        updateFile();
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

    private void addGridTab(GridPane grid, BorderPane inventoryPane){
        grid.getChildren().clear();
        Category current;
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
    }

    public void addStackLayers(BorderPane inventoryPane){
        this.grid = FX_Utility.createGrid();
        addGridTab(grid, inventoryPane);

        inventoryPane.setTop(grid);
        inventoryPane.setCenter(this.stack);

        // this.storeInventoryPane = inventoryPane; //only use for updates
    }

    private void addCategoryTab(int columnIndex, GridPane grid){
        Label label = FX_Utility.createTabLabel("+");
            label.setStyle("-fx-border-radius: 10px 10px 0 0; -fx-background-color: rgb(160, 13, 0); -fx-background-radius: 10px 10px 0 0;");
            grid.add(label, columnIndex, 0);

        BorderPane addCategoryPane = new BorderPane();
            Label title = new Label("Add Category");
            TextField field = new TextField();
                field.maxWidthProperty().bind(addCategoryPane.widthProperty().multiply(0.5));  
            Button btn = new Button("Create Category");      
                btn.prefWidthProperty().bind(addCategoryPane.widthProperty().multiply(0.25));
            
            setAddFunction(field, btn);

        VBox box = FX_Utility.boxInputCreate(title, field, btn);
            addCategoryPane.setCenter(box);
                BorderPane.setAlignment(box, Pos.BOTTOM_CENTER);
                   
            addCategoryPane.setStyle("-fx-border-radius: 50px; -fx-border-color: white; -fx-border-radius: 1px;");
            addCategoryPane.setId("category");

        this.stack.getChildren().add(addCategoryPane);
            

        addTransactionPaneFunctions(label, addCategoryPane);
    }

    private void setAddFunction(TextField field, Button btn){
        btn.setOnMouseClicked(click -> {

            if(field.getText().isEmpty()){
                FX_Utility.showAlert(Alert.AlertType.ERROR, this.stack.getScene().getWindow(), "ERROR", "Please provide an input");
                return;
            }
            
            String text = field.getText();
            String textEdited = text.toLowerCase();

            if(categoryNames.contains(textEdited)){
                FX_Utility.showAlert(Alert.AlertType.ERROR, this.stack.getScene().getWindow(), "ERROR", "Category name already exists");
                return;
            }

            addCategory(text);
            FX_Utility.showAlert(Alert.AlertType.CONFIRMATION, this.stack.getScene().getWindow(), "Success!!", "Category " + text + " has been created.");

            System.out.println("btn: " + click.getTarget());
        });
    }

    private void addStackFunctions(StackPane stack, BorderPane innerPane, Label label){
        label.setOnMouseClicked(event -> {

            if(this.prevClickedCategory == label){
                if(this.optionClicked == null){
                    return;
                } else {
                    this.optionClicked = null;
                }
            } else {
                FX_Utility.changeColorOnClick(label, this.prevClickedCategory);
            }


            stack.getChildren().remove(innerPane);
            stack.getChildren().add(innerPane);
            this.transactionPane.setCenter(null);
            System.out.println("clicked " + event.getClass());

            this.prevClickedCategory = label;
            if(this.optionClicked != null){
                this.optionClicked = null;
            }
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
