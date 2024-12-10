public class testMain {
    public static void main(String[] args){
        // Inventory instance = new Inventory();

        // instance.printInventoryList();

        Menu instance = new Order();
        instance.openOrderUI();
        instance.viewOrder();
    }
}
