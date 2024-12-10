/**
 * Utility class for ANSI escape sequences to format console output.
 * Provides constants for setting text colors and clearing the screen.
 */

package src.utils;

public class AnsiAdd {
    
    /**
     * Resets all formatting and colors.
     */
    public static final String RESET = "\u001B[0m";
    
    /**
     * Sets text color to red.
     */
    public static final String RED = "\u001B[31m";
    
    /**
     * Sets text color to green.
     */
    public static final String GREEN = "\u001B[32m";
    
    /**
     * Sets text color to yellow.
     */
    public static final String YELLOW = "\u001B[33m";
    
    /**
     * Sets text color to blue.
     */
    public static final String BLUE = "\u001B[34m";
    
    /**
     * Clears the screen.
     */
    public static final String CLEAR = "\u001B[2J";
    
    /**
     * Moves the cursor to the home position (top-left of the screen).
     */
    public static final String HOME = "\u001B[H";
    
    
    
    /**
     * Clears the screen and resets the cursor position to the top-left corner.
     */
    public static void clearScreen(){
        System.out.print(CLEAR + HOME);
        System.out.flush();
    }
}
