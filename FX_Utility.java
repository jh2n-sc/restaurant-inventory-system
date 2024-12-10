import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FX_Utility {
    public static void applyBackground(StackPane stack, Color color){
        Rectangle rectangle = new Rectangle();
            rectangle.setFill(color);
            rectangle.widthProperty().bind(stack.widthProperty());
            rectangle.heightProperty().bind(stack.heightProperty());
        stack.getChildren().add(rectangle);
    }
}
