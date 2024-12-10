/*
 * The Category class represents a category in a restaurant's inventory system.
 * It stores the category name, a list of items within the category, 
 * and provides methods to initialize and print the category details.
*/

/*
 * Important Class Members:
 * category_name: Stores the name of the category.
 * itemList: An ArrayList to store the items within the category.
 * item_Number: Stores the number of items in the category.
 * isEmpty: A boolean flag to indicate if the category has any items.
 * directory: Stores the directory path to the category's items file.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Category {
    public String category_name;
    private ArrayList<Item> itemList = new ArrayList<>();

    private int item_Number;
    private boolean isEmpty;
    private String directory = "./content/";

    public Category(String name){
        this.category_name = name;
        this.item_Number = 0;
        this.isEmpty = true;//this feels redundant but hmmmm

        if(name.indexOf(" ") != -1){
            // break; put a string editor to replace " " with "_"
        }

        this.directory = this.directory + name + ".txt";
        initializeItems(new File(this.directory));
    }

    private void initializeItems(File categoryItems){
        try(Scanner scanItems = new Scanner(categoryItems);){
            //temporary variables
            Item current;
            String store = "";
            boolean isEmpty = true;
            int number = 0;
            //temporary variables

            while(scanItems.hasNextLine()){
                store = scanItems.nextLine();
                    current = new Item(store);
                
                while(scanItems.hasNextInt()){
                    store = scanItems.nextLine();
                    current.addStock(store);
                }    

                itemList.add(current);

                if(isEmpty){
                    isEmpty = !isEmpty;
                }
                number++;
            }

            this.isEmpty = isEmpty;
            this.item_Number = number;

        } catch(FileNotFoundException err){
            err.printStackTrace();
            System.out.println("Did not find Category for " + this.category_name);
        }

    }

    public void printCategoryList(){
        System.out.println("Category: " + category_name);
        Item item;

        if(isEmpty){
            System.out.println("[Empty]");
            return;
        }

        for(int i = 0; i < item_Number; i++){
            item = itemList.get(i);
            item.printItem();
        }
    }

    public Item findItemByName(String itemName){
        for (Item item : itemList){
            if (item.getName().equals(itemName)){
                return item;
            }
        } 
        return null;
    }
}