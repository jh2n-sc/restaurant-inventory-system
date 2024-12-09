import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Inventory {

    private String restaurantName;
    private ArrayList<Category> categories = new ArrayList<>();
    private static String categoryNames;
    private static String filePath = "./content/index.txt";
    private boolean categoriesExist;

    public Inventory(){
        this.categoriesExist =  false;
        initializeCategories();
    }

    private void initializeCategories(){
        File indexFile = new File(filePath);
        String names = "";

        try(Scanner scanIndex = new Scanner(indexFile)){
            Category current;
            String storename;
            this.restaurantName = scanIndex.nextLine();

            while(scanIndex.hasNextLine()){
                storename = scanIndex.nextLine();
                current = new Category(storename);
                categories.add(current);
                names = names + storename + ";";

                if(!this.categoriesExist){
                    this.categoriesExist = true;
                }
            }

        } catch(FileNotFoundException err){
            err.printStackTrace();
            System.out.println("!!File was not Found!!");
        }

        categoryNames = names;
    }

    public void printInventoryList(){

        System.out.println(restaurantName);

        if(!categoriesExist){
            System.out.println("No Categories Initialized");
            return;
        }

        Category category;
        for(int i = 0; i < categories.size(); i++){
            category = categories.get(i);
            category.printCategoryList();
        }
    }
}
