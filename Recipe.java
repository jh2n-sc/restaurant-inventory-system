import java.io.*;
import java.util.*;

public class Recipe {
    private String foodItemName;
    private ArrayList<String> ingredientName;
    private ArrayList<Integer> ingredientQuantityRequirement;
    private String filePath = "./menu/";

    Recipe (String foodItemName){
        this.foodItemName = foodItemName;
        this.ingredientName = new ArrayList<>();
        this.ingredientQuantityRequirement = new ArrayList<>();

        parseRecipeFile();
    }

    private void parseRecipeFile (){
        File recipeFile = new File(filePath + foodItemName + ".txt");

        try (Scanner scanIngredient = new Scanner(recipeFile)){
            String ingredient;
            int ingredientAmount;
            while (scanIngredient.hasNext()){
                ingredient = scanIngredient.nextLine();
                ingredientName.add(ingredient);
                ingredientAmount = Integer.parseInt(scanIngredient.nextLine());
                ingredientQuantityRequirement.add(ingredientAmount);
            }
        } catch (FileNotFoundException e){
            System.out.println("Recipe file of " + foodItemName + " not found: " + e.getMessage());
        }
    }

    public String getName (){
        return foodItemName;
    }

    public void viewRecipe (){
        System.out.println(foodItemName + " Recipe List: ");

        int i = 0;
        for (String name : ingredientName){
            System.out.print(name + ": " + ingredientQuantityRequirement.get(i));
            i++;
        }
    }
}