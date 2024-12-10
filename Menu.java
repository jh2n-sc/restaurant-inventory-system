import java.io.*;
import java.util.*;

class Menu {
    private String filePath = "./menu/menu.txt";
    private ArrayList<String> menuItemList;
    private int numOfItemsInMenu = 0;

    Menu (){
        this.menuItemList = new ArrayList<>();

        parseMenuFile();
    }

    private void parseMenuFile (){
        File menuFile = new File(filePath);
        String itemName;

        try (Scanner scanMenuFile = new Scanner(menuFile)){
            while (scanMenuFile.hasNext()){ 
                itemName = scanMenuFile.nextLine();
                menuItemList.add(itemName);
            }
        } catch (FileNotFoundException e){
            System.out.println("Menu.txt file not found: " + e.getMessage());
        }
    }

    public void displayMenu (){
        System.out.println("Available Menu: ");
        for (String menuItem : menuItemList){
            System.out.println(menuItem);
        }
    }

    public ArrayList<String> getMenuList (){
        return menuItemList;
    }



    // Order Class Overrides
    public void openOrderUI(){}
    
    public void viewOrder(){}
}