import java.util.*;
import java.io.*;

class InitializeCategories {
    String[] categoryNames;

    public static void Initialize (){
        File category = new File("categories.txt");
        categoryNames = new String[sizeOfCategory];
        try (Scanner scanner = new Scanner(category)){
            while (scanner.hasNext()){
                categoryNames[i] = scanner.nextLine();
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found(1): " + e.getMessage());
        }
    }

    private int sizeOfCategory(){
        File category = new File("categories.txt");
        Scanner scanner = new Scanner(category);

        int size = 0;
        while (scanner.hasNext){
            size++;
        }
        
        scanner.close();

        return size;
    }

    public String[] getCategories (){
        return 
    }
}