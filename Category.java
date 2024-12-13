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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class Category {
    public String category_name;
    private ArrayList<Item> itemList = new ArrayList<>();

    private int item_Number;
    private boolean isEmpty;
    private String directory = "./content/";
    //Table fx
    private TableView<Item> itemTable;
    private boolean tablePrefWidth = false;

    public Category(String name){
        this.category_name = name;
        this.item_Number = 0;
        this.isEmpty = true;//this feels redundant but hmmmm
        this.itemTable = new TableView<>();
            FX_Utility.createTable(this.itemTable);

        editName(name);

        this.directory = this.directory + name + ".txt";
        initializeItems(new File(this.directory));
    }

    private void editName(String name){
        if(name.indexOf(" ") != -1){
            name.replace(' ', '_');
        }
    }

    public void setCategoryName(String name){
        editName(name);
        this.category_name = name;
        this.directory = this.directory + name + ".txt";
    }

    private void initializeItems(File categoryItems){
        try(Scanner scanItems = new Scanner(categoryItems);){
            //temporary variables
            Item current;
            String store = "";
            boolean isEmpty = true; //note: this is redundant, pwedeng i-use nalang yung this.isEmpty
            int number = 0;
            //temporary variables

            while(scanItems.hasNextLine()){
                store = scanItems.nextLine();
                    current = new Item(store);
                
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

            this.isEmpty = isEmpty;
            this.item_Number = number;
            sortList();

        } catch(FileNotFoundException err){
            err.printStackTrace();
            System.out.println("Did not find Category for " + this.category_name);
        }
    }

    public void addItem(String name){
        Item newItem = new Item(name);
            this.itemList.add(newItem);
            sortList();
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

    public void updateTable(){
        this.itemTable.getItems().clear();
        ObservableList<Item> data = FXCollections.observableArrayList(itemList);
        this.itemTable.setItems(data);
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
    public void addTable(BorderPane innerPane, BorderPane inventoryPane){
        if(!this.tablePrefWidth){
            this.itemTable.prefWidthProperty().bind(innerPane.widthProperty());
            tablePrefWidth = true;
        }
        
        ObservableList<Item> data = FXCollections.observableArrayList(itemList);

        addRowFunction(innerPane, inventoryPane, this.itemTable);

        this.itemTable.setItems(data);
        innerPane.setCenter(this.itemTable);
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