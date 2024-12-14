package src.utils;

//Note: access' the Item class; must have access to Item Class

import java.util.Date;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;

import src.item.Item;
import src.item.Stock;

public class FX_Utility {
    
    public static String fx = "-fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: red;";
    static String fontFX = "-fx-font-size: ";
    static String tableFX = ".scroll-bar:vertical, .scroll-bar:horizontal {" + " -fx-opacity: 0;" + "}";
    static String alignFX = "-fx-alignment: center;";
    static DropShadow dropShadow = new DropShadow();
    
    public static void applyBackground(StackPane stack, Color color){
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(color);
        rectangle.widthProperty().bind(stack.widthProperty());
        rectangle.heightProperty().bind(stack.heightProperty());
        stack.getChildren().add(rectangle);
    }
    
    public static GridPane createGrid(){
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0));
        grid.setHgap(1);
        return grid;
    }
    
    public static Label createTabLabel(String text){
        Label label = new Label(text);
        label.setFont(Font.font("sans serif", FontWeight.BLACK, 18));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Color.WHITE);
        label.setPrefHeight(30);
        label.setMaxWidth(200);
        label.setPadding(new Insets(2, 10, 2, 10));
        
        label.setStyle("-fx-border-radius: 10px 10px 0 0; -fx-background-color: rgb(127, 90, 77); -fx-background-radius: 10px 10px 0 0;");
        return label;
    }
    
    public static void addToTilePane(ImageView icon, BorderPane headerPane){
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(0, 10, 0, 0));
        tile.prefWidthProperty().bind(headerPane.widthProperty());
        tile.setAlignment(Pos.BASELINE_RIGHT);
        icon.setFitHeight(20);
        icon.setFitWidth(20);
        tile.getChildren().add(icon);
        headerPane.setTop(tile);
        
    }
    
    public static void createTable(TableView<Item> itemTable){
        itemTable.setStyle(FX_Utility.fx);
        itemTable.setFixedCellSize(35);
        itemTable.setStyle(fontFX + "16px;");
        
        TableColumn<Item, String> itemNameColumn = new TableColumn<>("Item");
        itemNameColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.4));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("item_Name"));
        itemNameColumn.setResizable(false);
        itemNameColumn.setSortType(TableColumn.SortType.ASCENDING);
        itemNameColumn.setSortable(true);
        
        TableColumn<Item, Double> stockAmountColumn = new TableColumn<>("Amount");
        stockAmountColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.2));
        stockAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalStock"));
        stockAmountColumn.setResizable(false);
        stockAmountColumn.setStyle(alignFX);
        
        TableColumn<Item, String> restockDateColumn = new TableColumn<>("Date Restocked");
        restockDateColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.4));
        restockDateColumn.setCellValueFactory(new PropertyValueFactory<>("latestStockDate"));
        restockDateColumn.setResizable(false);
        restockDateColumn.setStyle(alignFX);
        
        itemTable.getColumns().add(itemNameColumn);
        itemTable.getColumns().add(stockAmountColumn);
        itemTable.getColumns().add(restockDateColumn);
        
        itemTable.getSortOrder().add(itemNameColumn);
        itemTable.sort();
    }
    
    public static TableView<Stock> createTable(){
        TableView<Stock> stockTable = new TableView<>();
        stockTable.setStyle(fontFX + "14px;");
        
        TableColumn<Stock, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.prefWidthProperty().bind(stockTable.widthProperty().multiply(0.2));
        amountColumn.setResizable(false);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setStyle(alignFX);
        amountColumn.setSortable(false);
        
        TableColumn<Stock, Date> invoiceColumn = new TableColumn<>("Arrival Date");
        invoiceColumn.prefWidthProperty().bind(stockTable.widthProperty().multiply(0.4));
        invoiceColumn.setResizable(false);
        invoiceColumn.setCellValueFactory(new PropertyValueFactory<>("invoice"));
        invoiceColumn.setStyle(alignFX);
        invoiceColumn.setSortable(false);
        
        TableColumn<Stock, Date> expiryColumn = new TableColumn<>("Expiry Date");
        expiryColumn.prefWidthProperty().bind(stockTable.widthProperty().multiply(0.4));
        expiryColumn.setResizable(false);
        expiryColumn.setCellValueFactory(new PropertyValueFactory<>("expiry"));
        expiryColumn.setStyle(alignFX);
        expiryColumn.setSortable(false);
        
        stockTable.getColumns().add(amountColumn);
        stockTable.getColumns().add(invoiceColumn);
        stockTable.getColumns().add(expiryColumn);
        
        stockTable.setRowFactory(stocktab -> new TableRow<>() {
            @Override
            protected void updateItem(Stock current, boolean empty){
                super.updateItem(current, empty);
                if(current == null || empty){
                    setStyle("");
                } else {
                    String str = current.isExpired();
                    if(str == null){
                        setStyle("");
                    } else if(str.equals("expired")){
                        setStyle("-fx-background-color: rgba(196, 28, 26, 0.7)");
                    } else if(str.equals("warn")){
                        setStyle("-fx-background-color: rgba(190, 104, 19, 0.7);");
                    }
                }
            }
        });
        
        stockTable.setFixedCellSize(35);
        
        return stockTable;
    }
    
    
    public static void changeColorOnClick(Label label, Label prevLabel){
        label.setStyle("-fx-border-radius: 10px 10px 0 0; -fx-background-color: rgb(67, 20, 7); -fx-background-radius: 10px 10px 0 0;");
        prevLabel.setStyle("-fx-border-radius: 10px 10px 0 0; -fx-background-color: rgb(127, 90, 77); -fx-background-radius: 10px 10px 0 0;");
    }
    
    public static VBox boxInputCreate(Label title, TextField field, Button btn){
        dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
        dropShadow.setColor(Color.rgb(10, 10, 10, 0.2));
        dropShadow.setRadius(20);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setSpread(0.5);
        VBox box = new VBox();
        box.setSpacing(60);
        box.setAlignment(Pos.BASELINE_CENTER);
        title.setFont(Font.font("General sans", FontWeight.BLACK, 80));
        title.setTextFill(Color.WHITE);
        title.setPrefHeight(30);
        title.setPrefWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        field.setPrefHeight(50);
        field.setStyle("-fx-font-size: 25px; -fx-alignment: center;");
        btn.setPrefHeight(50);
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-alignment: center; -fx-background-radius: 20px; -fx-background-color: rgb(67, 67, 67); -fx-font-size: 30px;");
        btn.setEffect(dropShadow);
        box.getChildren().addAll(title, field, btn);
        return box;
    }
    
    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
