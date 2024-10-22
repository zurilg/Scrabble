/**
 * Scrabble's view class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 10-22-2024
 */

import java.util.Scanner;

public class ScrabbleView {
    /**
     * Constructor for scrabble's view class. Prints out a fancy title for the game.
     */
    public ScrabbleView(){
        // Show fancy title
        System.out.println(
                "  _____                _     _     _ \n" +
                " / ____|              | |   | |   | |\n" +
                "| (___   ___ _ __ __ _| |__ | |__ | | ___ \n" +
                " \\___ \\ / __| '__/ _` | '_ \\| '_ \\| |/ _ \\ \n" +
                " ____) | (__| | | (_| | |_) | |_) | |  __/\n" +
                "|_____/ \\___|_|  \\__,_|_.__/|_.__/|_|\\___|\n");
    }

    /**
     * Prints a string provided to it to the view.
     */
    public void viewPrint(String str){
        System.out.println(str);
    }

    /**
     * Retrieve a string from the user.
     *
     * @param message The message to display to user to prompt input.
     * @return String input provided by the user.
     */
    public String getString(String message){
        Scanner scan = new Scanner(System.in);
        String str = "";
        System.out.print(message);
        str = scan.nextLine();
        while(str.isEmpty()){
            System.out.print("Invalid input.");
            System.out.print(message);
            str = scan.nextLine();
        }
        return str;
    }

    /**
     * Retrieves an integer value from the user with a constrained lower and upper bound.
     *
     * @param message The message to display to the user when getting an integer input.
     * @param lowerBound The lower bound of the integer input.
     * @param upperBound The upper bound of the integer input.
     * @return Appropriate integer input from user.
     */
    public int getInt(String message, int lowerBound, int upperBound){
        Scanner scan = new Scanner(System.in);
        int input = -5;
        while(input < lowerBound || input > upperBound){
            System.out.print(message);
            try{ input = scan.nextInt(); }
            catch(Exception e){ System.out.print("Enter an integer value between " + lowerBound + " and " + upperBound + ". "); }
            finally { scan.nextLine(); }
            if(input < lowerBound || input > upperBound){System.out.print("Invalid input.\n");}
        }
        return input;
    }

    /**
     * Retrieve a character value from user and convert it to desired integer value.
     *
     * @param message Message which prompts user for a character input.
     * @param allowedCharacters A string that contains all allowed characters.
     * @param shift Integer value to subtract from char ASCII value for appropriate representation for use.
     * @return Integer to handle state of user character input.
     */
    public int getCharToInt(String message, String allowedCharacters, int shift){
        Scanner scan = new Scanner(System.in);
        boolean valid = false;
        String str = ""; // Take input as a string
        do{
            System.out.print(message); // Show user message
            str = scan.nextLine().toUpperCase(); // Get users input
            for(int i = 0; i < allowedCharacters.length(); i++){
                if(!str.isEmpty()){
                    if(allowedCharacters.charAt(i) == str.charAt(0)){
                        valid = true;
                        break;
                    }
                }
            }
            if(!valid){ System.out.println("Invalid input. Try again."); }
        }while(!valid);

        // Return appropriate integer representation of the character
        return str.charAt(0) - shift;
    }
}
