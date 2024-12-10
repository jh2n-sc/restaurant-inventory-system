import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Inventory_Restaurant extends Application{
    @Override
    public void start(Stage inventoryStage){

        StackPane mainStack = new StackPane();
            FX_Utility.applyBackground(mainStack, Color.BURLYWOOD);

        Scene scene = new Scene(mainStack);
        inventoryStage.setScene(scene);
            inventoryStage.setTitle("Inventory3");
            inventoryStage.setMinHeight(700);
            inventoryStage.setMinWidth(990);
        inventoryStage.show();
    }



    public static void main(String[] args){
        launch(args);
    }
}
