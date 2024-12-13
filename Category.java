import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Category {
    public String category_name;
    private ArrayList<Item> itemList = new ArrayList<>();
    private int item_Number;
    private boolean isEmpty;
    private String directory = "./content/";

    public Category(String name) {
        this.category_name = name;
        this.item_Number = 0;
        this.isEmpty = true; // this feels redundant but hmmmm
        editName(name);
        this.directory = this.directory + name + ".txt";
        initializeItems(new File(this.directory));
    }

    private void editName(String name) {
        if (name.indexOf(" ") != -1) {
            name.replace(' ', '_');
        }
    }

    public void setCategoryName(String name) {
        editName(name);
        this.category_name = name;
        this.directory = this.directory + name + ".txt";
    }

    private void initializeItems(File categoryItems) {
        try (Scanner scanItems = new Scanner(categoryItems);) {
            Item current;
            String store = "";
            boolean isEmpty = true;
            int number = 0;

            while (scanItems.hasNextLine()) {
                store = scanItems.nextLine();
                current = new Item(store);

                while (scanItems.hasNextDouble()) {
                    store = scanItems.nextLine();
                    current.addStock(store);
                }

                itemList.add(current);

                if (isEmpty) {
                    isEmpty = !isEmpty;
                }
                number++;
            }

            this.isEmpty = isEmpty;
            this.item_Number = number;

        } catch (FileNotFoundException err) {
            err.printStackTrace();
            System.out.println("Did not find Category for " + this.category_name);
        }
    }

    public String getCategoryName (){
        return category_name;
    }

    public ArrayList<Item> getItemList (){
        return itemList;
    }

    public void updateFile() {
        int flagValue = 0;
        try {
            FileWriter categoryWrite = new FileWriter(this.directory, false);
            BufferedWriter categoryBuffer = new BufferedWriter(categoryWrite);

            Item current;
            for (int i = 0; i < itemList.size(); i++) {
                current = itemList.get(i);
                categoryBuffer.write(current.getItem_Name());
                categoryBuffer.newLine();
                if (current.stockExists) {
                    categoryBuffer.write(current.getItemStockSummary());
                    categoryBuffer.newLine();
                }
                flagValue++;
            }

            categoryBuffer.close();
        } catch (IOException err) {
            System.out.println("Error. Did not UPDATE category " + this.category_name);
            System.out.println("Flag value: " + flagValue);
            err.printStackTrace();
        }
    }

    public void printCategoryList() {
        System.out.println("Category: " + category_name);
        Item item;

        if (isEmpty) {
            System.out.println("[Empty]");
            return;
        }

        for (int i = 0; i < item_Number; i++) {
            item = itemList.get(i);
            item.printItemStock();
        }
    }
}
