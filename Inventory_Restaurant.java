import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    
    public Inventory inventory;
    public static TextField field;

    @Override
    public void start(Stage inventoryStage){
        StackPane mainStack = new StackPane();
            FX_Utility.applyBackground(mainStack, Color.BURLYWOOD);

        addLayers(mainStack);

        Scene scene = new Scene(mainStack);
        inventoryStage.setScene(scene);
            inventoryStage.setTitle("Inventory3");
            inventoryStage.setMinHeight(800);
            inventoryStage.setMinWidth(1000);
        inventoryStage.show();
    }

    public void addLayers(StackPane mainStack){
        inventory = new Inventory(); //inventory instatiate;

        BorderPane pane = new BorderPane();
            pane.setPadding(new Insets(5, 5, 5, 5));
            pane.setStyle("-fx-border-color: black");


        addHeaderLayer(pane);
        addInventoryBody(pane);


        mainStack.getChildren().add(pane);
            StackPane.setAlignment(pane, Pos.CENTER);
    }

    public void addHeaderLayer(BorderPane pane){
        TilePane tile = new TilePane(Orientation.HORIZONTAL);
            tile.setMinHeight(50);
            tile.setStyle(FX_Utility.fx);
            tile.setPrefWidth(990);
            tile.setPadding(new Insets(5, 5, 5, 5));
            tile.setVgap(5);;

        GridPane grid = new GridPane();
            grid.setVgap(5);
        tile.getChildren().add(grid);
        
        Label label = new Label(inventory.getRestaurantName());
            label.setFont(Font.font("Gill Sans Ultra Bold", FontWeight.BLACK, 40));
            label.setTextFill(Color.BLACK);
            label.setMinHeight(50);
        grid.add(label, 0, 0);

            field = new TextField();
            field.prefWidthProperty().bind(tile.widthProperty().multiply(0.4));
            field.setPromptText("Search");
            field.setStyle("-fx-font-size: 18px; -fx-padding: 3px;");
        grid.add(field, 0, 1);


        pane.setTop(tile);
    }

    public void addInventoryBody(BorderPane pane){

        BorderPane inventoryPane = new BorderPane();
            inventoryPane.setPadding(new Insets(5, 5, 5, 5));

        inventory.addStackLayers(inventoryPane, field);

        pane.setCenter(inventoryPane);
    }

    public static void main(String[] args){
        launch(args);
    }
}
