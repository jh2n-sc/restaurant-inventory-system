import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class FX_Utility {

    static String fx = "-fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: red;";

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

            label.setStyle("-fx-border-radius: 10px 10px 0 0; -fx-background-color: rgb(67, 20, 7); -fx-background-radius: 10px 10px 0 0;");
        return label;
    }
}
