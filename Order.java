import java.util.*;

public class Order extends Menu {
    private ArrayList<String> orderedItems;
    private ArrayList<Recipe> cookItems;
    private ArrayList<Integer> orderedQuantities;
    private ArrayList<String> menuItemList;

    Order (){
        this.orderedItems = new ArrayList<>();
        this.cookItems = new ArrayList<>();
        this.orderedQuantities = new ArrayList<>();
        
        instantiateMenuList();
    }

    private void instantiateMenuList (){
        Menu MenuList = new Menu();
        this.menuItemList = MenuList.getMenuList(); 
    }

    @Override
    public void viewOrder (){
        System.out.println("Ordered Items: ");

        int i = 0;
        for (String temp : orderedItems){
            System.out.println(temp + ": " + orderedQuantities.get(i));
            i++;
        }
    }

    public void addOrder(String item, int quantity) {
            orderedItems.add(item);
            orderedQuantities.add(quantity);
    }

    private void initializeRecipes(){
        for (String item : orderedItems){
            int x = 0;
            for (int i = 0; i < orderedQuantities.get(x); i++){
                cookItems.add(new Recipe(item));
            }
            x++;
        }
    }

    @Override
    public void openOrderUI (){
        Scanner scanOrder = new Scanner(System.in);
        boolean confirmOrder = false;
        do {
            System.out.println("Menu List: ");
            for (String menuItem : menuItemList){
                System.out.println(menuItem);
            }

            boolean validOrder = false;
            while (!validOrder) { 
                String order;

                System.out.println("What would you like to order?");
                System.out.print("Item Name: ");
                order = scanOrder.nextLine();

                for (String menuItem : menuItemList){
                    if (order.equals(menuItem)){
                        validOrder = true;
                        break;
                    }
                }

                if (validOrder){
                    orderedItems.add(order);

                    boolean validQuantity = false;
                    int amount = 0;
                    while (!validQuantity){
                        System.out.println("How many?");
                        System.out.print("Amount: ");
                        amount = scanOrder.nextInt();
                        scanOrder.nextLine();

                        if (amount <= 0){
                            System.out.println("Invalid amount!");
                        } else {
                            validQuantity = true;
                            orderedQuantities.add(amount);
                        }
                    }
                } else {
                    System.out.println("Entered item is not in the menu list!");
                }
            }

            while (true){ 
                String continueOrdering;
                System.out.print("Continue ordering? Y/N: ");
                continueOrdering = scanOrder.nextLine();
                if (continueOrdering.equals("Y")){
                    confirmOrder = false;
                    break;
                } else if (continueOrdering.equals("N")){
                    confirmOrder = true;
                    break;
                } else {
                    System.out.println("Invalid operation!");
                }
            }

        } while (!confirmOrder);

        initializeRecipes();
    }
}
