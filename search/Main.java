package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        TextField searchBar = new TextField();
        ListView<String> listView = new ListView<>();
        
        searchBar.setLayoutX(24.0);
        searchBar.setLayoutY(24.0);
        searchBar.setPrefHeight(35.0);
        searchBar.setPrefWidth(553.0);
        
        listView.setLayoutX(24.0);
        listView.setLayoutY(69.0);
        listView.setPrefHeight(309.0);
        listView.setPrefWidth(553.0);
        
        root.getChildren().addAll(searchBar, listView);
        
        Controller controller = new Controller(searchBar, listView);
        controller.initialize();
        
        primaryStage.setTitle("Search");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}