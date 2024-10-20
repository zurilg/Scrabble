import java.util.ArrayList;
import java.util.HashSet;
import java.io.*;

public class ScrabbleController {
    // View
    private ScrabbleView view;
    // Models
    private TileBag tiles;
    private ArrayList<Player> players;
    private Board board;

    // 10,000 Acceptable Words
    private HashSet<String> dictionary;

    /**
     * Constructor for ScrabbleController class. Initializes view and models and gets all words for dictionary.
     */
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
            System.out.println(e); // TEMPORARY
        }
    }

    /**
     * Game setup has finished, ready to start.
     */
    public void startGame(){
        addPlayers(); // Get all players for game and their names.
        determinePlayerOrder(); // Determine order or play.
        distributeTiles(); // Give each player 7 tiles to start.

        boolean gameStatus = true;
        while(gameStatus){
            // Iterate through player order to constitute turns.
            for(Player p : players){
                view.printBoard(board);
                view.printPlayerTiles(p);
                int c = view.getCharToInt("\nPlayer choices\n\t(P) Place Word\n\t(S) Skip Turn\n\t(Q) Quit Game\nEnter choice: ", "PSQ", 0);
                if(c == 80){
                    playWord(view.getString("Enter word to play: "));
                }
                else if(c == 83){
                    // TEMPORARY
                    System.out.println(p.getName() + "decided to skip their turn.");
                }
                else{
                    System.exit(0);
                }
            }
            gameStatus = false; // Only does one round. NEEDS REMOVED...
        }
    }

    /**
     * Add all players according to how many players the user would like to play with. Get each player's name.
     */
    private void addPlayers(){
        int numPlayers = view.getInt("\nEnter number of players (2-4): ", 2, 4); // Get number of players to initialize players
        for(int i = 1; i<= numPlayers; i++){
            players.add(new Player(view.getString("\nEnter player " + i + "'s name: ")));
        }
    }

    /**
     * Have each player draw a tile and determine who goes first.
     */
    private void determinePlayerOrder(){
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

    /**
     * When a player needs tiles, append the appropriate amount of new tiles.
     */
    private void distributeTiles(){
        for(Player p : players){
            for(int i = 7 - p.numTiles();i>0;i--){
                p.addTileToHolder(tiles.popTile());
            }
        }
    }

    private void playWord(String word){
        if(dictionary.contains(word.toLowerCase())){
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
