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
                playerTurn(p);
                distributeTiles();
            }
            //gameStatus = false; // Only does one round. NEEDS REMOVED...
        }
    }

    private void playerTurn(Player p){
        view.printBoard(board);
        view.printPlayerTiles(p);
        boolean validTurn = false;
        while(!validTurn){
            int c = view.getCharToInt("\nPlayer choices\n\t(P) Place Word\n\t(S) Skip Turn\n\t(Q) Quit Game\nEnter choice: ", "PSQ", 0);
            // Player decides to play a word
            if(c == 80){ // 80 is the ASCII value for S
                // Get user input
                String word = view.getString("Enter word to play: ").toUpperCase();
                int[] coordinates = {view.getInt("\nEnter Y Coordinate (1-15): ", 1, 15)-1, view.getCharToInt("\nEnter X Coordinate (A-O): ", "ABCDEFGHIJKLMNO", 65)};
                int direction = view.getCharToInt("\nEnter word direction up-to-down (D) or left-to-right (R)", "DR", 68);
                // All tiles in players hand that are playable
                ArrayList<Tile> playable = new ArrayList<>();

                // Check that word is in dictionary
                if(dictionary.contains(word.toLowerCase())) {
                    // Get tiles from player that are playable
                    String lettersNotFound = "";
                    for(int i = 0; i < word.length(); i++){
                        boolean found = false;
                        for(Tile t : p.getTiles()){
                            if(word.charAt(i) == t.getChar().charAt(0)){
                                playable.add(t);
                                p.removeTile(t);
                                found = true;
                                break;
                            }
                        }
                        if(!found){
                            lettersNotFound += word.charAt(0);
                        }
                    }

                    // Handles first play
                    if (board.isEmpty() && lettersNotFound.isEmpty()) {
                        int coords[];
                        // THIS CASE IS EMPTY BOARD & NEED TO PLAY IN CENTER
                        for(Tile t : playable){
                            coords = coordinates;
                            p.removeTile(t);
                            board.placeTile(t, coords);
                            if(direction != 0){ coords[1]+=1; }
                            else{ coords[0]+=1; }
                        }

                        if(validateBoard() && board.getLetterAtIndex(new int[]{7, 7}) != null){
                            System.out.println("Valid First Turn"); // TEMPORARY
                            validTurn = true;
                        }

                        else{
                            System.out.println("Invalid First Turn"); // TEMPORARY
                            board = new Board();
                            for(Tile t : playable){
                                p.addTileToHolder(t);
                            }
                        }

                    }

                    // Any other play except first.
                    else if (!board.isEmpty()) { // board isn't empty and some letters are (hopefully) on board
                        board.setPrevState();
                        int[] coords = coordinates;
                        int letInd = 0;
                        boolean onBoard = false;

                        if(!lettersNotFound.isEmpty()){
                            for(int i = 0; i < word.length(); i++){
                                if(board.getLetterAtIndex(coords) == null){
                                    board.placeTile(playable.get(letInd), coords);
                                    p.removeTile(playable.get(letInd));
                                    letInd+=1;
                                }
                                if(direction != 0){ coords[1]+=1; }
                                else{ coords[0]+=1; }
                            }
                            onBoard = true;
                        }
                        else{

                            for(int i = 0; i < word.length(); i++){
                                if(board.getLetterAtIndex(coords) == null){
                                    board.placeTile(playable.get(i), coords);
                                    p.removeTile(playable.get(i));
                                    letInd+=1;
                                }
                                else{
                                    onBoard = true;
                                }

                                if(direction != 0){ coords[1]+=1; }
                                else{ coords[0]+=1; }
                            }
                        }

                        if(validateBoard() && onBoard){
                            System.out.println("Valid Turn"); // TEMPORARY
                            validTurn = true;
                        }
                        else{ // Something went wrong... revert board.
                            System.out.println("Invalid Turn"); // TEMPORARY
                            board.reset();
                            for(Tile t : playable){
                                p.addTileToHolder(t);
                            }
                        }
                    }


                    else{
                        System.out.println("UNHANDLED STATE"); // TEMPORARY
                        System.out.println("Empty board: " + board.isEmpty());
                        System.out.println("Letters found empty: " + lettersNotFound.isEmpty());
                    }
                }
                else{
                    System.out.println("NOT A WORD"); // TEMPORARY
                }
            }
            else if(c == 83){ // 83 is the ASCII value for S
                // TEMPORARY
                validTurn = true;
                System.out.println(p.getName() + " decided to skip their turn.");
            }
            else{ // Player decides to quit game.
                System.exit(0);
            }
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

    private boolean validateBoard(){
        // Check all rows and columns for valid data
        for(int r = 0; r < 15; r ++){
            String rowWords = "";
            String columnWords = "";
            for(int c = 0; c < 15; c ++){
                int[] rows = {r, c};
                int[] columns = {c, r};

                if(board.getLetterAtIndex(rows)!=null){ rowWords += board.getLetterAtIndex(rows); }
                else{ rowWords += " "; }

                if(board.getLetterAtIndex(columns) != null){ columnWords += board.getLetterAtIndex(columns); }
                else{ columnWords += " "; }
            }

            // Validate all row words
            String[] rowW = rowWords.split(" ");
            for (String s : rowW) {
                if(s.length() > 1){
                    //System.out.println(s.strip().toLowerCase()); // TEMPORARY FOR TESTING
                    if (!(dictionary.contains(s.strip().toLowerCase()))) { return false; }
                }
            }

            // Validate all column words
            String[] colW = columnWords.split(" ");
            for (String s : colW) {
                if(s.length() > 1){
                    // System.out.println(s.strip().toLowerCase()); // TEMPORARY FOR TESTING
                    if (!(dictionary.contains(s.strip().toLowerCase()))) { return false; }
                }
            }
        }

        return true;
    }

    private ArrayList<Integer> getCharIndexes(String word, char c){
        ArrayList<Integer> a = new ArrayList<>();
        for(int i = 0; i<word.length(); i++){
            if(word.charAt(i) == c) a.add(i);
        }
        return a;
    }
}
