import java.util.ArrayList;
import java.util.HashSet;
import java.io.*;
public class ScrabbleController {
    private ScrabbleView view;
    private TileBag tiles;
    private ArrayList<Player> players;
    private Board board;
    private HashSet<String> dictionary;

    public ScrabbleController(){
        // Initialize view
        view = new ScrabbleView();
        // Initialize Models
        tiles = new TileBag();
        players = new ArrayList<>();
        board = new Board();
        // Initialize dictionary
        dictionary = new HashSet<>();

        try{
            getWordsFromFile();
        }
        catch(Exception e){
            System.out.println(e);
        }



    }

    public void addPlayers(){
        int numPlayers = view.getNumPlayers(); // Get number of players to initialize players
        for(int i = 1; i<= numPlayers; i++){
            players.add(new Player(view.getPlayerName(i)));
        }
    }

    public void determinePlayerOrder(){
        // Used to determine whether players ties
        boolean redraw = false;
        // Have each player draw a tile
        for(Player p : players){
            p.addTileToHolder(tiles.popTile());
        }
        // Bubble sort based on tile ASCII value
        for(int i = 0; i < players.size()-1; i++){
            for(int j = 0; j < players.size() - i-1; j++){
                if(players.get(j).showLastTile().getChar().charAt(0) > players.get(j + 1).showLastTile().getChar().charAt(0)){
                    Player temp = players.get(j);
                    players.set(j, players.get(j+1));
                    players.set(j+1, temp);
                }
                else if(players.get(j).showLastTile().getChar().charAt(0) == players.get(j + 1).showLastTile().getChar().charAt(0)){
                    redraw = true;
                }
            }
        }
        // Return all tiles back to tile bag
        for(Player p : players){
            Tile t = p.popLastTile();
            while(t!=null){
                tiles.returnTile(t);
                t = p.popLastTile();
            }
        }
    }

    public void distributeTiles(){
        for(Player p : players){
            for(int i = 7 - p.numTiles();i>0;i--){
                p.addTileToHolder(tiles.popTile());
            }
        }
    }

    public void startGame(){
        boolean gameStatus = true;
        while(gameStatus){
            for(Player p : players){
                view.printBoard(board);
                view.printPlayerTiles(p);
                String c = view.playerTurnChoice();
                if(c.equals("P")){
                    playWord(view.getWord());
                }
                else if(c.equals("S")){
                    // TEMPORARY
                    System.out.println(p.getName() + "decided to skip their turn.");
                }
                else{
                    System.exit(0);
                }
            }
            gameStatus = false;
        }
    }

    private void playWord(String word){
        if(dictionary.contains(word)){
            // TEMPORARY
            System.out.println("Worked");
        }
        else{
            // TEMPORARY
            System.out.println("Didn't work");
        }
    }

    /**
     * @throws FileNotFoundException
     */
    private void getWordsFromFile() throws FileNotFoundException {
        BufferedReader r = new BufferedReader(new FileReader("./Words.txt"));
        try {
            String word = r.readLine();
            while (word != null) {
                dictionary.add(word);
                word = r.readLine();
            }
        } catch (Exception e) {
            //TEMPORARY
            System.out.println("IOException: " + e);
        }
    }

}
