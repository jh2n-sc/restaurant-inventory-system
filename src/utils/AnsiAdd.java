package src.utils;

public class AnsiAdd {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    public static final String CLEAR = "\u001B[2J";
    public static final String HOME = "\u001B[H";

    public static void clearScreen(){
        System.out.print(CLEAR + HOME);
        System.out.flush();
    }
}
