package sample;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    
    private TextField searchBar;
    private ListView<String> listView;
    
    ArrayList<String> words = new ArrayList<>(
            Arrays.asList("Chicken", "Pork (belly or other cuts)", "Shrimp", "Oxtail",
                    "Tomatoes", "Onion", "Radish", "Eggplant", "Okra", "String beans",
                    "Water spinach (kangkong)", "Banana blossoms", "Bok choy", "Carrots",
                    "Cabbage", "Bell pepper", "Soy sauce", "Vinegar", "Garlic", "Bay leaves",
                    "Peppercorns", "Salt", "Sugar", "Fish sauce", "Tamarind mix (or fresh tamarind)",
                    "Shrimp paste (bagoong)", "Peanut butter", "Ground rice (toasted)",
                    "Annatto powder or seeds", "Oyster sauce", "Green chili (siling haba)",
                    "Pancit canton noodles", "Water", "Chicken broth", "Vegetable oil",
                    "Canola oil (for frying Lechon Kawali)", "Lobster", "Seaweed", "Chicken breast")
    );
    
    public Controller(TextField searchBar, ListView<String> listView) {
        this.searchBar = searchBar;
        this.listView = listView;
    }
    
    public void initialize() {
        listView.getItems().addAll(words);
        
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            listView.getItems().clear();
            listView.getItems().addAll(searchList(newValue, words));
        });
        
        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                listView.getItems().clear();
                listView.getItems().addAll(searchList(searchBar.getText(), words));
            }
        });
    }
    
    private List<String> searchList(String searchWords, List<String> listOfStrings) {
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));
        
        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }
}