import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Inventory {

    private String restaurantName;
    private ArrayList<Category> categories = new ArrayList<>();
    private static String[] categoryNames;

    public Inventory(){
        initializeCategories();
    }

    private void initializeCategories(){
        File indexFile = new File("./content/index.txt");
        

        try(Scanner scanIndex = new Scanner(indexFile)){

        } catch(FileNotFoundException err){
            err.printStackTrace();
            System.out.println("!!File was not Found!!");
        }
    }
}
