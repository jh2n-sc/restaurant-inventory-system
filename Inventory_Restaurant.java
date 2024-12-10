

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Inventory_Restaurant extends Application{
    
    Inventory inventory;

    @Override
    public void start(Stage inventoryStage){
        StackPane mainStack = new StackPane();
            FX_Utility.applyBackground(mainStack, Color.BURLYWOOD);

        addLayers(mainStack);

        Scene scene = new Scene(mainStack);
        inventoryStage.setScene(scene);
            inventoryStage.setTitle("Inventory3");
            inventoryStage.setMinHeight(700);
            inventoryStage.setMinWidth(990);
        inventoryStage.show();
    }

    public void addLayers(StackPane mainStack){
        inventory = new Inventory();

        BorderPane pane = new BorderPane();
            pane.setPadding(new Insets(5, 5, 5, 5));
            pane.setStyle(FX_Utility.fx);


        addHeaderLayer(pane);
        addInventoryBody(pane);


        mainStack.getChildren().add(pane);
            StackPane.setAlignment(pane, Pos.CENTER);
    }

    public void addHeaderLayer(BorderPane pane){
        TilePane tile = new TilePane();
            tile.setMinHeight(50);
            tile.setStyle(FX_Utility.fx);
            tile.setPrefWidth(990);
            tile.setPadding(new Insets(5, 5, 5, 5));
        
        Label label = new Label(inventory.getRestaurantName());
            label.setFont(Font.font("Gill Sans Ultra Bold", FontWeight.BLACK, 30));
            label.setTextFill(Color.BLACK);
            label.setStyle(FX_Utility.fx);
            label.setMinHeight(50);
        tile.getChildren().add(label);
            TilePane.setAlignment(label, Pos.CENTER_LEFT);

        pane.setTop(tile);
    }

    public void addInventoryBody(BorderPane pane){

        BorderPane inventoryPane = new BorderPane();
            inventoryPane.setStyle(FX_Utility.fx);
            inventoryPane.setPadding(new Insets(5, 5, 5, 5));

        BorderPane viewPane = new BorderPane();
            viewPane.setStyle("-fx-border-color: blue; -fx-border-width: 1px; -fx-border-style: solid;");
            viewPane.setMaxWidth(400);
            viewPane.setPrefWidth(300);

        inventory.addStackLayers(inventoryPane, viewPane);

        pane.setCenter(inventoryPane);
    }



    public static void main(String[] args){
        launch(args);
    }
}
