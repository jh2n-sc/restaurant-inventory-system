import java.util.*;
import java.io.*;

class InitializeCategories {
    public static String[] categoryNames;

    public static void Initialize (){
        File category = new File("categories.txt");
        categoryNames = new String[sizeOfCategory()];
        try (Scanner scanner = new Scanner(category)){
            int i = 0;
            while (scanner.hasNext()){
                categoryNames[i] = scanner.nextLine();
                i++;
            }

            scanner.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found(1): " + e.getMessage());
        }
    }

    private static int sizeOfCategory(){
        File category = new File("categories.txt");

        int size = 0;
        try (Scanner scanner = new Scanner(category)){
            while (scanner.hasNext()){
                size++;
            }
            scanner.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found(2): " + e.getMessage());    
        }
    
        return size;
    }

    public String[] getCategories (){
        return categoryNames;
    }
}