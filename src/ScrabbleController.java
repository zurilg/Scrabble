import java.util.ArrayList;
import java.util.HashMap;
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
    // All words on board
    private HashMap<String, Integer> wordsOnBoard;

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
            view.viewPrint("File read error. Exception: " + e); // TEMPORARY
        }
        wordsOnBoard = new HashMap<String, Integer>();
    }

    /**
     * Game setup has finished, ready to start.
     */
    public void startGame(){
        addPlayers(); // Get all players for game and their names.
        determinePlayerOrder(); // Determine order or play.
        distributeTiles(); // Give each player 7 tiles to start.

        boolean gameStatus = true;
        int scorelessTurns = 0;
        Player winner = null;

        while(gameStatus && scorelessTurns !=6 && winner == null){
            // Iterate through player order to constitute turns.
            for(Player p : players){
                int initialScore = p.getScore();
                playerTurn(p);
                distributeTiles();
                p.addToScore(scoreBoard());

                // Track number of scoreless turns
                if(initialScore == p.getScore()) scorelessTurns += 1;
                else scorelessTurns = 0;

                //
                if(p.getTiles().isEmpty()){
                    winner = p;
                }

            }
            //gameStatus = false; // Only does one round. NEEDS REMOVED...
        }
    }

    private void playerTurn(Player p){
        view.viewPrint(board.toString()); // print board for player
        view.viewPrint(p.toString()); // print the player tiles
        boolean validTurn = false; // player has not yet completed a valid turn
        while(!validTurn){ // while the player has not completed a valid turn
            // Player chooses what to do
            int c = view.getCharToInt("\nPlayer choices\n\t(P) Place Word\n\t(S) Skip Turn\n\t(Q) Quit Game\nEnter choice: ", "PSQ", 0);
            // Player decides to play a word
            if(c == 80){ // 80 is the ASCII value for S
                // Check that word is in dictionary
                String word = view.getString("Enter word to play: ").toUpperCase();
                if(dictionary.contains(word.toLowerCase())) {
                    // Get coordinates and direction
                    int[] coordinates = {view.getInt("\nEnter Y Coordinate (1-15): ", 1, 15)-1, view.getCharToInt("\nEnter X Coordinate (A-O): ", "ABCDEFGHIJKLMNO", 65)};
                    int direction = view.getCharToInt("\nEnter word direction up-to-down (D) or left-to-right (R): ", "DR", 68);
                    // Check coordinates and direction. Cant go outside of scrabble board.
                    boolean outOfBounts = false;
                    if(direction == 0){
                        if(coordinates[0] + word.length() > 14) outOfBounts = true;
                    }
                    else{
                        if(coordinates[1] + word.length() > 14) outOfBounts = true;
                    }
                    // Still good to keep going, not out of bounds.
                    if(!outOfBounts){
                        // All tiles in players hand that are playable
                        ArrayList<Tile> playable = new ArrayList<>();
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
                                view.viewPrint("Valid First Turn"); // TEMPORARY
                                validTurn = true;
                            }

                            else{
                                view.viewPrint("Invalid First Turn"); // TEMPORARY
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
                                        if(playable.size() == letInd) break; // This means that the word is placed in a spot it doesn't belong.
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
                                view.viewPrint("Valid Turn"); // TEMPORARY
                                validTurn = true;
                            }
                            else{ // Something went wrong... revert board.
                                view.viewPrint("Invalid Turn"); // TEMPORARY
                                board.reset();
                                for(Tile t : playable){
                                    p.addTileToHolder(t);
                                }
                            }
                        }
                        else if (board.isEmpty() && !lettersNotFound.isEmpty())
                            view.viewPrint("DON'T HAVE ALL TILES");
                        else
                            view.viewPrint("UNHANDLED STATE\nEmpty board: " + board.isEmpty() + "Letters found empty: " + lettersNotFound.isEmpty());
                    }
                    else
                        view.viewPrint("OUT OF BOUNDS");
                }
                else
                    view.viewPrint("NOT A WORD"); // TEMPORARY
            }
            else if(c == 83){ // 83 is the ASCII value for S
                validTurn = true;
                view.viewPrint(p.getName() + " decided to skip their turn.");
            }
            else{ // Player decides to quit game.
                view.viewPrint("Thanks for playing!");
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
                if(tiles.returnSize() != 0)
                    p.addTileToHolder(tiles.popTile());
            }
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


    // Nearly identical to the validate board method
    private int scoreBoard(){
        int score = 0;
        int [] tileValues = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
        HashMap<String, Integer> allWords = new HashMap<String, Integer>();
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


            String[] rowW = rowWords.split(" ");
            for (String s : rowW) {
                if(s.length() > 1){
                    s = s.strip();
                    if(allWords.containsKey(s)) allWords.put(s, allWords.get(s) + 1);
                    else allWords.put(s, 1);
                }
            }
            String[] colW = columnWords.split(" ");
            for (String s : colW) {
                if(s.length() > 1){
                    s = s.strip();
                    if(allWords.containsKey(s)) allWords.put(s, allWords.get(s) + 1);
                    else allWords.put(s, 1);
                }
            }
        }

        for(String key : allWords.keySet()){
            int wordScore = 0;
            if (wordsOnBoard.containsKey(key)) {
                if(wordsOnBoard.get(key) != allWords.get(key)){
                    for(int i = 0; i < allWords.get(key) - wordsOnBoard.get(key); i++){
                        for(int x = 0; x < key.length(); x++){
                            wordScore += tileValues[key.charAt(x)-65];
                        }
                    }
                    wordsOnBoard.put(key, allWords.get(key));
                }
            }
            else{
                for(int i = 0; i < allWords.get(key); i++){
                    for(int x = 0; x < key.length(); x++){
                        wordScore += tileValues[key.charAt(x)-65];
                    }
                }
                wordsOnBoard.put(key, allWords.get(key));
            }
            view.viewPrint("Word: " + key + " Score: " + wordScore);
            score += wordScore;
        }

        return score;
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
            view.viewPrint("IOException: " + e);
        }
    }
}
