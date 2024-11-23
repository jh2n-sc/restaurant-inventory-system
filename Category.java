import java.io.*;
import java.util.*;

class Category {
    File categoryFile;

    // HashMap key is the name of the ingredient, and HashMap value is the amount of the ingredient
    HashMap<String, Integer> ingredientList = new HashMap<>();

    // The constructor below initializes a file object based on the given category name which is stored
    // in the String categoryName. The while loop reads the file line by line, expecting a "ingredientName,amount" format.
    // The scanned ingredientName and its corresponding amount is stored in the ingredientList HashMap.     
    String categoryName;
    Category (String category){
        categoryName = category + ".txt";

        categoryFile = new File(categoryName);

        try (Scanner scanner = new Scanner(categoryFile)){
            while (scanner.hasNext()){
                String[] tempArray = scanner.nextLine().split(",");
                int amount = Integer.parseInt(tempArray[1]);

                ingredientList.put(tempArray[0], amount);
            }

            scanner.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found: " + e.getMessage());
        }
    }

    // Method for printing the ingredient and its corresponding amount
    public void printStock (){
        for (Map.Entry<String, Integer> entry : ingredientList.entrySet()){
            String ingredient = entry.getKey();
            int amount = entry.getValue();
            System.out.println(ingredient + ": " + amount);
        }
    }
}