import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Item {
    //main
    public String item_Name;
    // public Queue<Stock> stocks = new LinkedList<>();
    public LinkedList<Stock> stocks = new LinkedList<>();
    public double totalStock;
    public String unit;
    //main

    //flag
    public boolean stockExists; //to verify if an item has stock or not
    //flag

    //remember //for sorting purposes
    public Stock latestStock;
    public Date latestStockDate;
    public String latestStockString;
    //

    //table fx
    private TableView<Stock> stockTable;
    private boolean tablePrefWidth = false;

    // for referencing
    Category category;

    public Item(String name, Category category){
        this.item_Name = name;
        this.category = category;
        this.stockExists = false;
        this.totalStock = 0;
        this.stockTable = FX_Utility.createTable();
        this.unit = "unit";
        this.latestStock = new Stock(name, 0, unit);
        initialLatestStock();
    }

    private void initialLatestStock(){
        this.latestStockString = "Not Yet Restocked";
    }

    public void addStock(String stock){
        Scanner scanString = new Scanner(stock);

        double size = scanString.nextDouble();
        String unit = scanString.next();
        String dateArrival = scanString.next();
        String dateExpiry = scanString.next();

        Stock current = checkIfStockExists(dateArrival, dateExpiry);

        if(current != null){
            current.setAmount(size);
        } else {
            Stock newstock = new Stock(this.item_Name, size, unit);
                newstock.setDateArrived(dateArrival);
                newstock.setExpiryDate(dateExpiry);
            this.stocks.add(newstock); //add to the list

            sortList();
        }

        if(!this.stockExists){
            this.stockExists = !this.stockExists;
        }

        if(this.unit.equals("unit")){
            this.unit = unit;
        }

        
        scanString.close();
        this.totalStock = this.totalStock + size;
    }

    private Stock checkIfStockExists(String dateArrival, String dateExpiry){

        ListIterator<Stock> iterateStock = stocks.listIterator();
        Stock current;
        while(iterateStock.hasNext()){
            current = iterateStock.next();
            if(dateArrival.equals(current.getInvoiceString())){
                if(dateExpiry.equals(current.getExpiryString())){
                    return current;
                }
            }
        }
        return null;
    }

    private void sortList(){
        if(this.stocks.size() > 1){
            Collections.sort(this.stocks, Comparator.comparing(Stock::getExpiry));
        }
        setLatestStock(this.stocks.getLast());
    }

    private void setLatestStock(Stock latest){
        this.latestStock = latest;
        this.latestStockDate = latest.invoice;
        this.latestStockString = latest.getInvoiceString();
    }

    public String getItem_Name(){//for tableview access
        return item_Name;
    } 

    public String getUnit(){
        return this.unit;
    }

    public Double getTotalStock(){//for tableview access

        return this.totalStock;
    }

    public String getTotal(){
        int totalInt = (int) this.totalStock;
        String totalString = "";
        if(totalInt == this.totalStock){
            totalString = totalInt + "";
            
        } else {
            totalString = this.totalStock + "";
        }

        return totalString;
    }

    public Double getAmount(){
        return this.totalStock;
    }

    public Stock getLatestStock(){
        return this.latestStock;
    }

    public Date getLatestStockDate(){//for tableview access
        return this.latestStockDate;
    }

    public String getLatestStockString(){
        return this.latestStockString;
    }

    public Stock getFront(){
        return stocks.peek();
    }

    public String getItemStockSummary(){ //summarizes all the stock into one string
        String summary = "";

        Iterator<Stock> traverse = this.stocks.iterator();

        Stock current;
        while(traverse.hasNext()){
            current = traverse.next();
            summary = summary + current.getStockSummary();

            if(traverse.hasNext()){
                summary = summary + "\n";
            }
        }

        return summary;
    }

    public void updateTable(){
        this.stockTable.getItems().clear(); //maybe this is not needed
        ObservableList<Stock> data = FXCollections.observableArrayList(this.stocks);
        this.stockTable.setItems(data);
    }

    //CLI section
    public void printItem(){
        System.out.println("item name: " + item_Name);

        Iterator<Stock> traverse = this.stocks.iterator();

        Stock stockitem;
        while(traverse.hasNext()){
            stockitem = traverse.next();
            stockitem.printStock();
        }
    }
    //CLI section

    //FX section
    public void addStockTable(BorderPane viewPane, BorderPane headerPane){
        if(!this.tablePrefWidth){
            this.stockTable.prefWidthProperty().bind(viewPane.widthProperty());
            tablePrefWidth = true;
        }
        
        ObservableList<Stock> stocklist = FXCollections.observableArrayList(this.stocks);

        this.stockTable.setItems(stocklist);

        addHeader(headerPane);
        viewPane.setCenter(stockTable);
        viewPane.setBottom(createBottomContent());
    }

    private void addHeader(BorderPane headerPane){
        VBox box = new VBox();
            box.setSpacing(4);
        int totalInt = (int) this.totalStock;
        String totalString = "";
        if(totalInt == this.totalStock){
            totalString = totalInt + " " + this.unit;
            
        } else {
            totalString = this.totalStock + " " + this.unit;
        }

        Label name = FX_Utility.createTabLabel("Item: " + this.item_Name);
            name.setFont(Font.font("sans serif", FontWeight.BLACK, 25));
            name.setStyle(null);
            name.setTextAlignment(TextAlignment.LEFT);
            name.setMaxWidth(Double.MAX_VALUE);
        Label total = FX_Utility.createTabLabel("Total: " + totalString);
            total.setFont(Font.font("sans serif", FontWeight.BLACK, 25));
            total.setStyle(null);
            total.setTextAlignment(TextAlignment.LEFT);
            total.setMaxWidth(Double.MAX_VALUE);

        box.getChildren().addAll(name, total);

        box.prefWidthProperty().bind(headerPane.widthProperty());
        headerPane.setCenter(box);
    }

    private BorderPane createBottomContent(){
        BorderPane pane = new BorderPane();
            pane.setPadding(new Insets(1, 1, 1, 1));
        VBox box = new VBox();
            box.setSpacing(2);
            box.prefWidthProperty().bind(pane.widthProperty());
        pane.setCenter(box);

        String[] array = {"Add Stock", "Withdraw Stock"};

        for(int i = 0; i < 2; i++){
            Label label = new Label(array[i]);
                label.setFont(Font.font("sans serif", FontWeight.BLACK, 18));
                label.setTextFill(Color.WHITE);
                label.setPrefHeight(30);
                label.setMaxWidth(Double.MAX_VALUE);
                label.setPadding(new Insets(2, 10, 2, 10));

                label.setStyle("-fx-background-color: rgb(177, 62, 17); -fx-background-radius: 5px 5px 5px 5px; -fx-alignment: center");

                box.getChildren().add(label);

                BorderPane contentPane = createContent(array[i]);
            Inventory.addTransactionPaneFunctions(label, contentPane);
        }

        return pane;
    }
    
    private BorderPane createContent(String text) {
        BorderPane contentPane = new BorderPane();
        TextField fieldAmount = new TextField();
        TextField fieldUnit = new TextField();
        TextField fieldDateArrival = new TextField();
        TextField fieldDateExpiry = new TextField();
        Button btn = new Button(text);
    
        VBox fields = new VBox(5);
        fields.getChildren().add(new Label("Amount:"));
        fields.getChildren().add(fieldAmount);
        
        if (text.equals("Add Stock")) {
            fields.getChildren().addAll(
                new Label("Unit:"), fieldUnit,
                new Label("Date of Arrival (yyyy-MM-dd):"), fieldDateArrival,
                new Label("Date of Expiry (yyyy-MM-dd):"), fieldDateExpiry
            );
        }
    
        contentPane.setCenter(fields);
        contentPane.setBottom(btn);
    
        btn.setOnMouseClicked(event -> {
            if (text.equals("Add Stock")) {
                if (fieldAmount.getText().isEmpty() || fieldUnit.getText().isEmpty() || fieldDateArrival.getText().isEmpty() || fieldDateExpiry.getText().isEmpty()) {
                    FX_Utility.showAlert(Alert.AlertType.ERROR, fieldAmount.getScene().getWindow(), "ERROR", "Please provide all inputs");
                    return;
                }
    
                double amount = Double.parseDouble(fieldAmount.getText());
                String unit = fieldUnit.getText();
                String dateArrival = fieldDateArrival.getText();
                String dateExpiry = fieldDateExpiry.getText();
    
                addNewStock(amount, unit, dateArrival, dateExpiry);
                FX_Utility.showAlert(Alert.AlertType.CONFIRMATION, fieldAmount.getScene().getWindow(), "Success!", "Stock added successfully.");
            } else if (text.equals("Withdraw Stock")) {
                if (fieldAmount.getText().isEmpty()) {
                    FX_Utility.showAlert(Alert.AlertType.ERROR, fieldAmount.getScene().getWindow(), "ERROR", "Please provide the amount to withdraw");
                    return;
                }
    
                double amount = Double.parseDouble(fieldAmount.getText());
    
                withdrawStock(amount);
                FX_Utility.showAlert(Alert.AlertType.CONFIRMATION, fieldAmount.getScene().getWindow(), "Success!", "Stock withdrawn successfully.");
            }
        });
    
        return contentPane;
    }
    
    
    // NEW METHODS ADDED BY JEM

    public void updateFile() {
        String categoryName = this.category.getCategoryName();

        if (categoryName.contains(" ")){
            categoryName = categoryName.replaceAll("\\s+", "_"); 
        }

        String categoryFileName = "./content/" + categoryName + ".txt"; // Construct file path using category name
        File categoryFile = new File(categoryFileName);
    
        StringBuilder fileContent = new StringBuilder();
        boolean itemFound = false;
    
        try (Scanner scanner = new Scanner(categoryFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileContent.append(line).append("\n");
    
                // If the item name matches, append the updated stock information
                if (line.equalsIgnoreCase(this.item_Name)) {
                    itemFound = true;
    
                    // Append each stock's summary
                    for (Stock stock : this.stocks) {
                        fileContent.append(stock.getStockSummary()).append("\n");
                    }
    
                    // Skip existing stock lines in the file
                    while (scanner.hasNextLine()) {
                        line = scanner.nextLine();
                        if (line.trim().isEmpty() || Character.isLetter(line.trim().charAt(0))) {
                            fileContent.append(line).append("\n");
                            break;
                        }
                    }
                }
            }
    
            // If the item is not found, append the item and its stock to the end of the file
            if (!itemFound) {
                fileContent.append(this.item_Name).append("\n");
                for (Stock stock : this.stocks) {
                    fileContent.append(stock.getStockSummary()).append("\n");
                }
            }
    
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Category file not found: " + categoryFileName);
        }
    
        // Write the updated content back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(categoryFile))) {
            writer.write(fileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error updating category file: " + categoryFileName);
        }
    }
    
    public void addNewStock(double amount, String unit, String dateArrival, String dateExpiry) {
        Stock newStock = checkIfStockExists(dateArrival, dateExpiry);
    
        if (newStock != null) {
            newStock.setAmount(amount);
        } else {
            newStock = new Stock(this.item_Name, amount, unit);
            newStock.setDateArrived(dateArrival);
            newStock.setExpiryDate(dateExpiry);
            this.stocks.add(newStock);
            sortList();
        }
    
        if (!this.stockExists) {
            this.stockExists = true;
        }
    
        if (this.unit.equals("unit")) {
            this.unit = unit;
        }
    
        this.totalStock += amount;
        updateTable();
        updateFile();
    }
    
    public void withdrawStock(double amount) {
        Iterator<Stock> iterator = this.stocks.iterator();
        while (iterator.hasNext() && amount > 0) {
            Stock currentStock = iterator.next();
            double currentAmount = currentStock.getAmount();
            
            if (currentAmount > amount) {
                currentStock.setAmount(-amount);
                this.totalStock -= amount;
                InventoryLogger.logWithdraw(this.item_Name, amount, this.category.getCategoryName());
                amount = 0;
            } else {
                amount -= currentAmount;
                this.totalStock -= currentAmount;
                InventoryLogger.logWithdraw(this.item_Name, amount, this.category.getCategoryName());
                iterator.remove();
            }
        }
        
        if (this.stocks.isEmpty()) {
            this.stockExists = false;
            this.unit = "unit";
        }
        
        updateTable();
        updateFile();
    }    

    //FX section

}