import java.util.ArrayList;
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

    public void printBoard(Board b){
        System.out.println(b.toString());
    }

    public void printPlayerTiles(Player p){
        ArrayList<Tile> tiles = p.getTiles();
        String tileCharacters = p.getName() + ": ";
        for(Tile t : tiles){
            tileCharacters += t.getChar() + "  ";
        }
        tileCharacters += "\n";
        System.out.println(tileCharacters);
    }

    public String playerTurnChoice(){
        Scanner scan = new Scanner(System.in);
        System.out.print("\nPlayer choices\n\t(P) Place Word\n\t(S) Skip Turn\n\t(Q) Quit Game\nEnter choice: ");
        String playerChoice = scan.nextLine();
        return playerChoice;
    }

    public String getWord(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter word to place: ");
        return scan.nextLine();
    }
    public int[] getCoordinates(){

        Scanner scan = new Scanner(System.in);
        System.out.print("\nEnter Column (A-O): ");
        int x = scan.nextInt();
        scan.nextLine();

        System.out.print("\nEnter Row (1-15): ");
        String y = scan.nextLine();
        int[] coords = {x-1,y.charAt(0)-65};
        return coords;
    }
}
