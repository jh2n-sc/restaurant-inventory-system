import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Inventory {

    private String restaurantName;
    private ArrayList<Category> categories = new ArrayList<>();
    public static String categoryNames;
    private static String filePath = "./content/index.txt";
    private boolean categoriesExist;

    private Label prevClickedCategory;
    private static Label optionClicked;
    private static BorderPane transactionPane = new BorderPane();
    private static StackPane stack;
    private GridPane grid;
    private static TextField fieldStore;

    public Inventory(){
        this.categoriesExist =  false;
        createPane(transactionPane);
        stack = new StackPane();
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


    private void addCategory(String name){
        Category newcategory = new Category(name);
        categories.add(newcategory);
        categoryNames = categoryNames + name.toLowerCase() + ";";
        addGridTab(this.grid, (BorderPane) this.grid.getParent());
        updateFile();
    }

    private void removeCategory(Category remove){
        categories.remove(remove);
        categoryNames = categoryNames.replace(remove.category_name.toLowerCase(), "");
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
        removeCategoryTab(categories.size() + 2, grid);
        addCategoryTab(categories.size() + 1, grid);
        
        for(int i = categories.size() - 1; i >= 0; i--){

            BorderPane innerPane = new BorderPane();
            current = categories.get(i);
            Label label = FX_Utility.createTabLabel(current.category_name);
                    grid.add(label, i, 0);
            stack.getChildren().add(innerPane);
            current.addTable(innerPane, inventoryPane, fieldStore);

            addStackFunctions(stack, innerPane, label);

            if(i == 0){
                label.setStyle("-fx-border-radius: 10px 10px 0 0; -fx-background-color: rgb(67, 20, 7); -fx-background-radius: 10px 10px 0 0;");
            }
        }
    }

    public void addStackLayers(BorderPane inventoryPane, TextField field){
        this.grid = FX_Utility.createGrid();
        fieldStore = field;
        addGridTab(grid, inventoryPane);

        inventoryPane.setTop(grid);
        inventoryPane.setCenter(stack);
    }

    private void addCategoryTab(int columnIndex, GridPane grid){
        Label label = FX_Utility.createTabLabel("+");
            label.setStyle("-fx-border-radius: 10px 10px 0 0; -fx-background-color: rgb(177, 62, 17); -fx-background-radius: 10px 10px 0 0;");
            grid.add(label, columnIndex, 0);

        BorderPane addCategoryPane = new BorderPane();
            TextField field = new TextField();  
            Button btn = new Button("Create Category");      

            FX_Utility.contentPaneField(addCategoryPane, field, btn, "Add Category");
            
            setAddFunction(field, btn);
                   
            addCategoryPane.setId("category");

        addTransactionPaneFunctions(label, addCategoryPane);
    }

    private void removeCategoryTab(int columnIndex, GridPane grid){
        Label label = FX_Utility.createTabLabel("-");
            label.setStyle("-fx-border-radius: 10px 10px 0 0; -fx-background-color: rgb(177, 70, 17); -fx-background-radius: 10px 10px 0 0;");
            grid.add(label, columnIndex, 0);

        BorderPane removeCategoryPane = new BorderPane();
            TextField field = new TextField();  
            Button btn = new Button("Remove Category");      

            FX_Utility.contentPaneField(removeCategoryPane, field, btn, "Remove Category");
            
            setRemoveFunction(field, btn);
                   
            removeCategoryPane.setId("category");

        addTransactionPaneFunctions(label, removeCategoryPane);
    }

    private void setRemoveFunction(TextField field, Button btn){
        btn.setOnMouseClicked(click -> {

            if(field.getText().isEmpty()){
                FX_Utility.showAlert(Alert.AlertType.ERROR, stack.getScene().getWindow(), "ERROR", "Please provide an input");
                return;
            }
            
            String text = field.getText();
            String textEdited = text.toLowerCase();

            if(!categoryNames.contains(textEdited)){
                FX_Utility.showAlert(Alert.AlertType.ERROR, stack.getScene().getWindow(), "ERROR", "Category name does not exist");
                return;
            }

            Category remove = null;

            for(int i = 0; i < this.categories.size(); i++){
                Category current = this.categories.get(i);
                if(current.category_name.toLowerCase().equals(textEdited)){
                    remove = current;
                    break;
                }
            }

            if(remove == null){
                FX_Utility.showAlert(Alert.AlertType.CONFIRMATION, field.getScene().getWindow(), "ERROR!!", "Category " + text + " was not found.");
                    return;
            }

            removeCategory(remove);
            FX_Utility.showAlert(Alert.AlertType.CONFIRMATION, stack.getScene().getWindow(), "Success!!", "Category " + text + " has been removed.");
            transactionPane.getChildren().clear();

            System.out.println("btn: " + click.getTarget());
        });
    }

    private void setAddFunction(TextField field, Button btn){
        btn.setOnMouseClicked(click -> {

            if(field.getText().isEmpty()){
                FX_Utility.showAlert(Alert.AlertType.ERROR, stack.getScene().getWindow(), "ERROR", "Please provide an input");
                return;
            }
            
            String text = field.getText();
            String textEdited = text.toLowerCase();

            if(categoryNames.contains(textEdited)){
                FX_Utility.showAlert(Alert.AlertType.ERROR, stack.getScene().getWindow(), "ERROR", "Category name already exists");
                return;
            }

            addCategory(text);
            FX_Utility.showAlert(Alert.AlertType.CONFIRMATION, stack.getScene().getWindow(), "Success!!", "Category " + text + " has been created.");
            transactionPane.getChildren().clear();

            System.out.println("btn: " + click.getTarget());
        });
    }

    private void addStackFunctions(StackPane stack, BorderPane innerPane, Label label){
        label.setOnMouseClicked(event -> {

            if(this.prevClickedCategory == label){
                if(optionClicked == null){
                    return;
                } else {
                    optionClicked = null;
                }
            } else {
                FX_Utility.changeColorOnClick(label, this.prevClickedCategory);
            }


            stack.getChildren().remove(innerPane);
            stack.getChildren().add(innerPane);
            transactionPane.getChildren().clear();
            if(stack.getChildren().contains(transactionPane)){
                stack.getChildren().remove(transactionPane);
            }
            System.out.println("clicked " + event.getClass());

            this.prevClickedCategory = label;
            if(optionClicked != null){
                optionClicked = null;
            }
        });

        this.prevClickedCategory = label;
    }

    public static void addTransactionPaneFunctions(Label label, BorderPane contentPane){
        label.setOnMouseClicked(clicked -> {
            if(label == optionClicked){
                    return;
            }

            optionClicked = label;
            transactionPane.setCenter(contentPane);
            stack.getChildren().remove(transactionPane);
            stack.getChildren().add(transactionPane);

            System.out.println("Transaction: " + clicked.getTarget());
        });
    }

    private void createPane(BorderPane pane){
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setStyle("-fx-background-color: rgb(67, 20, 7)");
    }

    // FX
}
