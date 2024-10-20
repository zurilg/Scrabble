import java.util.Scanner;
public class ScrabbleView {
    public ScrabbleView(){
        System.out.println("Welcome to Scrabble!");
    }

    public int getNumPlayers(){
        Scanner scan = new Scanner(System.in);
        int input = 0;
        while(input < 2 || input > 4){
            System.out.print("Enter the number of players (between 2-4): ");
            try{
                input = scan.nextInt();
            }
            catch(Exception e){
                System.out.println("Enter an integer value between 2 and 4.");
                scan.nextLine();
            }
            if(input < 2 || input > 4){System.out.println("Invalid input.");}
        }
        return input;
    }

    public String getPlayerName(int playerNum){
        Scanner scan = new Scanner(System.in);
        System.out.printf("\nEnter player %d's name: ", playerNum);
        String name = scan.nextLine();
        return name;
    }
}
