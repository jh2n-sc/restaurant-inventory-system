import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Category {
    public String category_name;
    private ArrayList<Item> itemList = new ArrayList<>();

    private int item_Number;
    private boolean isEmpty;
    private String directory = "./content/";
    //Table fx
    private TableView<Item> itemTable;
    private boolean tablePrefWidth = false;
    private String itemNames;

    public Category(String name){
        this.category_name = name;
        this.item_Number = 0;
        this.isEmpty = true;//this feels redundant but hmmmm
        this.itemTable = new TableView<>();
            FX_Utility.createTable(this.itemTable);
        this.itemNames = "";

        name = editName(name);

        this.directory = this.directory + name + ".txt";
        initializeItems(new File(this.directory));
    }

    private String editName(String name){
        if(name.contains(" ")){
            name = name.replace(" ", "_");
            System.out.println("(if)name of category: " + name);
        }
        return name;
    }

    public void setCategoryName(String name){
        editName(name);
        this.category_name = name;
        this.directory = this.directory + name + ".txt";
    }

    private void initializeItems(File categoryItems){
        try{
            if(!categoryItems.exists()){
                boolean wasCreated = categoryItems.createNewFile();

                if(wasCreated){
                    System.out.println("Your file was created");
                }
            }
        } catch(IOException err){
            err.printStackTrace();
            System.out.println("Cannot create file " + this.category_name);
        }
        
        try(Scanner scanItems = new Scanner(categoryItems);){
            //temporary variables
            Item current;
            String store = "";
            String nameString = "";
            boolean isEmpty = true; //note: this is redundant, pwedeng i-use nalang yung this.isEmpty
            int number = 0;
            //temporary variables

            while(scanItems.hasNextLine()){
                store = scanItems.nextLine();
                    current = new Item(store);
                    nameString = nameString + store + ";";
                while(scanItems.hasNextDouble()){
                    store = scanItems.nextLine();
                    current.addStock(store);
                }    

                itemList.add(current);

                if(isEmpty){
                    isEmpty = !isEmpty;
                }
                number++;
            }

            this.itemNames = nameString.toLowerCase();
            this.isEmpty = isEmpty;
            this.item_Number = number;
            sortList();

        } catch(FileNotFoundException err){
            err.printStackTrace();
            System.out.println("Did not find Category for " + this.category_name);
        }
    }

    private void addItem(String name){
        Item newItem = new Item(name);
            this.itemList.add(newItem);
            this.itemNames = this.itemNames + name.toLowerCase() + ";";
            System.out.println("name: " + this.itemNames);
            sortList();
            setData();
            updateFile();
    }

    private void removeItem(Item item){
        this.itemList.remove(item);
        String remove = item.getItem_Name().toLowerCase() + ";";
        this.itemNames = this.itemNames.replace(remove, "");
        System.out.println("name: " + this.itemNames);
        sortList();
        setData();
        updateFile();
    }

    private void sortList(){
        if(this.itemList.size() > 1){
            Collections.sort(this.itemList, Comparator.comparing(Item::getItem_Name));
        }
    }

    public void updateFile(){
        int flagValue = 0;
        try{
            FileWriter categoryWrite = new FileWriter(this.directory, false);
            BufferedWriter categoryBuffer = new BufferedWriter(categoryWrite);

            Item current;
            for(int i = 0; i < itemList.size(); i++){
                current = itemList.get(i);
                categoryBuffer.write(current.getItem_Name());
                    categoryBuffer.newLine();
                if(current.stockExists){
                    categoryBuffer.write(current.getItemStockSummary());
                        categoryBuffer.newLine();
                }
                flagValue++;
            }

            categoryBuffer.close();
        } catch(IOException err){
            System.out.println("Error. Did not UPDATE category " + this.category_name);
            System.out.println("Flag value: " + flagValue);
            err.printStackTrace();
        }
    }

    // CLI
    public void printCategoryList(){
        System.out.println("Category: " + category_name);
        Item item;

        if(isEmpty){
            System.out.println("[Empty]");
            return;
        }

        for(int i = 0; i < item_Number; i++){
            item = itemList.get(i);
            item.printItem();
        }
    }
    //  CLI

    // FX
    public void setData(){
        ObservableList<Item> data = FXCollections.observableArrayList(this.itemList);
        this.itemTable.setItems(data);
    }

    public void addTable(BorderPane innerPane, BorderPane inventoryPane){
        if(!this.tablePrefWidth){
            this.itemTable.prefWidthProperty().bind(innerPane.widthProperty());
            tablePrefWidth = true;
        }
        
        setData();

        GridPane grid = addGridButtons();

        addRowFunction(innerPane, inventoryPane, this.itemTable);
        innerPane.setCenter(this.itemTable);
        innerPane.setBottom(grid);
    }

    private GridPane addGridButtons(){
        GridPane grid = new GridPane();
        grid.setHgap(2);
        String[] array = {"Add Item", "Remove Item"};

        for(int i = 0; i < 2; i++){
            Label label = new Label(array[i]);
                label.setFont(Font.font("sans serif", FontWeight.BLACK, 18));
                label.setTextFill(Color.WHITE);
                label.setPrefHeight(30);
                label.setMaxWidth(200);
                label.setPadding(new Insets(2, 10, 2, 10));

                label.setStyle("-fx-background-color: rgb(177, 62, 17); -fx-background-radius: 0 0 5px 5px;");
                grid.add(label, i, 0);

                BorderPane contentPane = createContent(array[i]);

            Inventory.addTransactionPaneFunctions(label, contentPane);                    
        }
        return grid;
    }

    private BorderPane createContent(String text){
        BorderPane contentPane = new BorderPane();
            TextField field = new TextField();
            Button btn = new Button(text);

            FX_Utility.contentPaneField(contentPane, field, btn, text);

            setAddFunction(field, btn, text);

        return contentPane;
    }

    private void setAddFunction(TextField field, Button btn, String text){
        if(text.equals("Add Item")){
            btn.setOnMouseClicked(clicked -> {

                if(field.getText().isEmpty()){
                    FX_Utility.showAlert(Alert.AlertType.ERROR, field.getScene().getWindow(), "ERROR", "Please provide an input");
                    return;
                }

                String txt = field.getText();
                String txtEdited = txt.toLowerCase();

                if(this.itemNames.contains(txtEdited)){
                    FX_Utility.showAlert(Alert.AlertType.ERROR, field.getScene().getWindow(), "ERROR", "Item name already exists");
                return;
                }

                addItem(txt);
                FX_Utility.showAlert(Alert.AlertType.CONFIRMATION, field.getScene().getWindow(), "Success!!", "Item " + text + " has been created.");
                System.out.println("Clicked " + clicked.getTarget());
            });
        } else {
            btn.setOnMouseClicked(clicked -> {

                if(field.getText().isEmpty()){
                    FX_Utility.showAlert(Alert.AlertType.ERROR, field.getScene().getWindow(), "ERROR", "Please provide an input");
                    return;
                }

                String txt = field.getText();
                String txtEdited = txt.toLowerCase();

                if(!this.itemNames.contains(txtEdited)){
                    FX_Utility.showAlert(Alert.AlertType.ERROR, field.getScene().getWindow(), "ERROR", "Item name does not exist.");
                return;
                }

                Item delete = null;

                for(int i = 0; i < this.itemList.size(); i++){
                    Item current = this.itemList.get(i);
                    if(current.getItem_Name().toLowerCase().equals(txtEdited)){
                        delete = current;
                        break;
                    }
                }

                if(delete == null){
                    FX_Utility.showAlert(Alert.AlertType.CONFIRMATION, field.getScene().getWindow(), "ERROR!!", "Item " + txt + " was not found.");
                    return;
                }

                System.out.println("Delete: " + delete.getItem_Name());

                removeItem(delete);
                FX_Utility.showAlert(Alert.AlertType.CONFIRMATION, field.getScene().getWindow(), "Success!!", "Item " + txt + " has been removed.");
                System.out.println("Clicked " + clicked.getTarget());
            });
        }


    }

    private void addRowFunction(BorderPane innerPane, BorderPane inventoryPane, TableView<Item> itemTable){
        BorderPane viewPane = new BorderPane();
            viewPane.setPadding(new Insets(10, 3, 3, 3));
            viewPane.setStyle("-fx-background-color: rgba(67, 20, 7, 0.3); -fx-background-radius: 20px 20px 0 0; -fx-border-radius: 20px 20px 0px 0px;");
            viewPane.prefWidthProperty().bind(inventoryPane.widthProperty().multiply(0.3));

        BorderPane headerPane = new BorderPane();
            headerPane.prefHeightProperty().bind(viewPane.heightProperty().multiply(0.3));
            viewPane.setTop(headerPane);

        Image icon = new Image(getClass().getResourceAsStream("./png/close.png"));
            ImageView exitIcon = new ImageView(icon);
            FX_Utility.addToTilePane(exitIcon, headerPane);

        itemTable.setRowFactory(table -> {
            TableRow<Item> selectedItem = new TableRow<>();
                selectedItem.setOnMouseClicked(clicked -> {
                    if(!selectedItem.isEmpty()){
                        Item selected = selectedItem.getItem();
                            System.out.println("Selected :" + selected.getItem_Name());
                        selected.addStockTable(viewPane, headerPane);

                        inventoryPane.setRight(viewPane);
                    }
                    System.out.println("selected " + clicked.getTarget());
                });
            
            System.out.println("clicked " + table.getComparator());
            return selectedItem;
        });

        exitIcon.setOnMouseClicked(clickExit -> {
            if(inventoryPane.getChildren().contains(viewPane)){
                inventoryPane.getChildren().remove(viewPane);
                System.out.println("Clicked " + clickExit.getTarget());
            }
        });
    }
    // FX


}