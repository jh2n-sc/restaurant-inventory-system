import java.io.*;
import java.util.*;

class InitializeCategories {
    String[] categories;
    File categoryFile;

    // This method scans throught the categories.txt file and stores all the input into the categories array.
    // Then, it returns the categories array.
    public String[] getCategories (){
        categoryFile = new File("categories.txt");
        categories = new String[getSize()];

        try (Scanner scanner = new Scanner(categoryFile)){
            int i = 0;
            while (scanner.hasNext()){
                categories[i] = scanner.nextLine();
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        return categories;
    }

    // Private method for getting the number of categories in categories.txt
    private int getSize (){
        categoryFile = new File("categories.txt");

        int size = 0;
        try (Scanner scanner = new Scanner(categoryFile)){
            while (scanner.hasNext()){
                size++;
                scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        return size;
    }
}