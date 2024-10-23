/**
 * Scrabble's controller class. Holds the game logic.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 10-22-2024
 */

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
        wordsOnBoard = new HashMap<>();
    }

    /**
     * Setup has finished. Starting game and going until user quits or completion.
     */
    public void startGame(){
        addPlayers(); // Get all players for game and their names.
        determinePlayerOrder(); // Determine order or play.
        distributeTiles(); // Give each player 7 tiles to start.

        int scorelessTurns = 0; // Keeps track of number of scoreless turns.
        boolean tilesRemain = true; // Flags when a player runs out of tiles.

        // This limits the amount of scoreless turns before game ends from 6-8. 6 for 2-3 players, 8 for 4 players...
        while(scorelessTurns <= 6 && tilesRemain){
            // Iterate through player order to constitute turns.
            for(Player p : players){
                int initialScore = p.getScore(); // Store initial score to keep track of scoreless turns

                playerTurn(p); // Give player their turn.
                distributeTiles(); // Fill up each player's tile holder.
                p.addToScore(scoreBoard()); // Calculate the player's score after turn and add their turn score to their overall score.

                // Track number of scoreless turns
                if(initialScore == p.getScore()){ scorelessTurns += 1; }
                else{ scorelessTurns = 0; }

                // A player has 0 tiles left.
                if(p.getTiles().isEmpty()){ tilesRemain = false; }
            }
        }
        determineWinner();
    }

    /**
     * Determines/enables the logistics and abilities behind a player turn provided the player whose turn it is.
     *
     * @param p The player whose turn it is.
     */
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
                    if(direction == 0) {
                        if (coordinates[0] + word.length() > 15){ outOfBounts = true; } // The word is being written down and goes off the board downwards.
                    }
                    else {
                        if (coordinates[1] + word.length() > 15){ outOfBounts = true; } // The word is being written to the right and goes off the board to the right.
                    }
                    // Still good to keep going, not out of bounds.
                    if(!outOfBounts){
                        // All tiles in players hand that are playable
                        ArrayList<Tile> playable = new ArrayList<>();
                        // Store the letters not found in the players tile holder.
                        StringBuilder lettersNotFound = new StringBuilder();
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
                            // If the tile isn't found in the player's tile holder then declare it as not found.
                            if(!found){ lettersNotFound.append(word.charAt(0)); }
                        }

                        // Handles first play
                        if (board.isEmpty() && (lettersNotFound.isEmpty())) {
                            int[] coords;

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
                            boolean onBoard = true;

                            // The player is missing some of the tiles for the word (the rest should be on the board).
                            if(!lettersNotFound.isEmpty()){
                                for(int i = 0; i < word.length(); i++){
                                    if(board.getLetterAtIndex(coords) == null){
                                        // Try to place a tile in it
                                        for(Tile t : playable){
                                            if(t.getChar().charAt(0) == word.charAt(i)){
                                                board.placeTile(t, coords);
                                                // Make sure only one of those tiles is removed from user.
                                                for(int x = 0; x < p.getTiles().size(); x+=1){
                                                    if(p.getTiles().get(x).getChar().charAt(0) == t.getChar().charAt(0)){
                                                        p.removeTileFromIndex(x);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        // Still empty?
                                        if(board.getLetterAtIndex(coords) == null){ onBoard = false; }
                                    }
                                    // Letters aren't on board if they aren't equal to letter at specific index in word.
                                    else if(board.getLetterAtIndex(coords).charAt(0) != word.charAt(i)){
                                        onBoard = false; // Letter that's supposed to be there isn't there.
                                    }

                                    if(direction != 0){ coords[1]+=1; }
                                    else{ coords[0]+=1; }
                                }
                            }
                            // The player has all the letters for the word, but some are supposed to be on the board.
                            else{
                                onBoard = false;
                                for(int i = 0; i < word.length(); i++){
                                    if(board.getLetterAtIndex(coords) == null){
                                        board.placeTile(playable.get(i), coords);
                                        p.removeTile(playable.get(i));
                                    }
                                    else{
                                        onBoard = true;
                                    }
                                    if(direction != 0){ coords[1]+=1; }
                                    else{ coords[0]+=1; }
                                }
                            }

                            // Once all tiles are placed on the board, check if the board is valid and whether the word was placed on the board.
                            if(validateBoard() && onBoard){
                                view.viewPrint("Valid Turn"); // TEMPORARY
                                validTurn = true;
                            }
                            // If something about the board isn't right, revert changes and give the player their tiles back.
                            else{ // Something went wrong... revert board.
                                view.viewPrint("Invalid Turn");
                                board.reset(); // Revert changes to board
                                // Give player their tiles back.
                                for(Tile t : playable){
                                    p.addTileToHolder(t);
                                }
                            }
                        }
                        // If the player enters a word that they don't have all the tiles for on first turn.
                        else if (board.isEmpty() && (!lettersNotFound.isEmpty()))
                            view.viewPrint("DON'T HAVE ALL TILES");
                        // Used for testing - TEMPORARY SHOULD BE REMOVED
                        else
                            view.viewPrint("UNHANDLED STATE\nEmpty board: " + board.isEmpty() + "Letters found empty: " + (lettersNotFound.isEmpty()));
                    }
                    // Player tried to play word out of bounds.
                    else{
                        view.viewPrint("OUT OF BOUNDS");
                    }
                }
                // Word isn't in the dictionary.
                else{
                    view.viewPrint("NOT A WORD"); // TEMPORARY
                }
            }

            // Player decides to skip their turn.
            else if(c == 83){ // 83 is the ASCII value for S
                validTurn = true;
                view.viewPrint(p.getName() + " decided to skip their turn.");
            }
            // Player decides to quit game.
            else{
                view.viewPrint("Thanks for playing!");
                System.exit(0);
            }
        }
    }

    /**
     * Add all players according to how many players the user would like to play with. Get each player's name.
     */
    private void addPlayers(){
        // Get number of players.
        int numPlayers = view.getInt("\nEnter number of players (2-4): ", 2, 4); // Get number of players to initialize players
        for(int i = 1; i<= numPlayers; i++){
            // Get each player's name.
            players.add(new Player(view.getString("\nEnter player " + i + "'s name: ")));
        }
    }

    /**
     * Have each player draw a tile and determine who goes first.
     * Note: currently doesn't handle redraws. If two players draw, oh well...
     */
    private void determinePlayerOrder(){
        // Used to determine whether players ties
        boolean redraw = false; // Not used for now...
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
                    redraw = true; // Not used for now...
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
            // Give player 7 - (# tiles in tile holder) tiles
            for(int i = 7 - p.numTiles();i>0;i--){
                if(tiles.returnSize() != 0){ p.addTileToHolder(tiles.popTile()); }
            }
        }
    }

    /**
     * Determines whether the board is valid (i.e., all words in all rows and columns are in the dictionary).
     *
     * @return Board validity (true : false).
     */
    private boolean validateBoard(){
        // Check all rows and columns for valid data
        for(int r = 0; r < 15; r ++){
            StringBuilder rowWords = new StringBuilder();
            StringBuilder columnWords = new StringBuilder();
            for(int c = 0; c < 15; c ++){
                int[] rows = {r, c};
                int[] columns = {c, r};
                // If there is a letter at certain row coordinate, add it to rowWords. Otherwise, add a space to rowWords.
                if(board.getLetterAtIndex(rows)!=null){ rowWords.append(board.getLetterAtIndex(rows)); }
                else{ rowWords.append(" "); }
                // If there is a letter at certain column coordinate, add it to columnWords. Otherwise, add a space to columnWords.
                if(board.getLetterAtIndex(columns) != null){ columnWords.append(board.getLetterAtIndex(columns)); }
                else{ columnWords.append(" "); }
            }

            // Validate all row words
            String[] rowW = rowWords.toString().split(" ");
            for (String s : rowW) {
                if(s.length() > 1){
                    // view.viewPrint(s.strip().toLowerCase()); // TEMPORARY FOR TESTING
                    if (!(dictionary.contains(s.strip().toLowerCase()))) { return false; } // If the dictionary doesn't contain one of the words then board isn't valid.
                }
            }

            // Validate all column words
            String[] colW = columnWords.toString().split(" ");
            for (String s : colW) {
                if(s.length() > 1){
                    // view.viewPrint(s.strip().toLowerCase()); // TEMPORARY FOR TESTING
                    if (!(dictionary.contains(s.strip().toLowerCase()))) { return false; } // If the dictionary doesn't contain one of the words then board isn't valid.
                }
            }
        }
        return true; // The board is valid. All words on the board are in the dictionary.
    }

    /**
     * Calculates the total score from a player's turn.
     * Note: currently a player's score per turn is only determine by sum of all word scores. WordScore = TileValue1 + --- + TileValueN. *BONUS BOARD SQUARES NOT SCORED*
     *
     * @return The sum of all letter scores for the player.
     */
    private int scoreBoard(){
        int score = 0; // Score for a players turn. Addition of all letter tiles in the words they created.
        int [] tileValues = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10}; // All letter values alphabetical A-Z
        // Hashmap to store words that are on board after players turn. Used to compare to past words. Format: < Word, # of Occurrences >
        HashMap<String, Integer> allWords = new HashMap<>();
        for(int r = 0; r < 15; r ++){
            StringBuilder rowWords = new StringBuilder();
            StringBuilder columnWords = new StringBuilder();
            for(int c = 0; c < 15; c ++){
                int[] rows = {r, c};
                int[] columns = {c, r};
                // Get all row words in a string
                if(board.getLetterAtIndex(rows)!=null){ rowWords.append(board.getLetterAtIndex(rows)); }
                else{ rowWords.append(" "); }
                // Get all column words a string
                if(board.getLetterAtIndex(columns) != null){ columnWords.append(board.getLetterAtIndex(columns)); }
                else{ columnWords.append(" "); }
            }
            // Get row words in a string array then add them to hash map.
            String[] rowW = rowWords.toString().split(" ");
            for (String s : rowW) {
                if(s.length() > 1){
                    s = s.strip();
                    if(allWords.containsKey(s)) allWords.put(s, allWords.get(s) + 1);
                    else allWords.put(s, 1);
                }
            }
            // Get column words in a string array then add them to hash map.
            String[] colW = columnWords.toString().split(" ");
            for (String s : colW) {
                if(s.length() > 1){
                    s = s.strip();
                    if(allWords.containsKey(s)) allWords.put(s, allWords.get(s) + 1);
                    else allWords.put(s, 1);
                }
            }
        }
        /* For each word on the board generated after the new turn, compare it to the current words on the board.
        This helps determine which words the player just played and therefore get credit for.
        If the word belongs to the player, give its point values to the player. */
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
     * Determines winner(s) and has view display winners.
     */
    private void determineWinner(){
        // Alert user that the game is over
        view.viewPrint("");

        // Adjust scores for tiles. Deduct remaining tile points from user. Add all remaining tile points to players with no tiles left.
        sortRemainingTilePoints();
        // Bubble sort players based on their scores
        sortPlayersByScore();

        // Print all player scores
        StringBuilder sortedPlayers = new StringBuilder();
        view.viewPrint("\nFINAL SCORES: ");
        for(Player p : players){
            sortedPlayers.append("Name: ").append(p.getName()).append("\t\tFinal Score: ").append(p.getScore()).append(" pts").append("\t\tNumber of Tiles Remaining: ").append(p.getTiles().size()).append("\n");
        }
        view.viewPrint(String.valueOf(sortedPlayers)); // Print the list of sorted players.

        // Check for ties
        checkForTiedPlayers();

        // Print winner(s)
        StringBuilder winner = new StringBuilder();
        if(players.size() == 1){
            winner.append("Congratulations, ").append(players.getFirst().getName()).append("! You've won with a score of ").append(players.getFirst().getScore()).append(" points!");
        }
        else{
            winner.append("A tie has occurred!");
            for(Player p : players){ winner.append("\n\tName: ").append(p.getName()).append("\tPoints: ").append(p.getName()); }
        }
        view.viewPrint(String.valueOf(winner));
    }

    /**
     * Deducts remaining tile points from player scores. Adds all remaining tile points to players with no tiles left.
     */
    private void sortRemainingTilePoints(){
        // Get sum of all remaining tile point values and deduct them from each player.
        int remainingPoints = 0;
        for(Player p : players){
            for(Tile t : p.getTiles()){
                p.addToScore(-1 * t.getValue());
                remainingPoints += t.getValue();
            }
        }

        /* If a user played all of their tiles, they get the sum of all other players unplaced tiles
        added to their score.
         */
        for(Player p : players){ if(p.getTiles().isEmpty()) { p.addToScore(remainingPoints); } }
    }

    /**
     * Sorts players based on their final scores.
     */
    private void sortPlayersByScore(){
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < players.size()-1; j++){
                if(players.get(j).getScore() < players.get(j+1).getScore()){
                    Player p = players.get(j);
                    players.set(j, players.get(j+1));
                    players.set(j+1, p);
                }
            }
        }
    }

    /**
     * Removes all players that don't have the highest score.
     */
    private void checkForTiedPlayers(){
        ArrayList<Player> tiedPlayers = new ArrayList<>();
        tiedPlayers.add(players.getFirst());
        for(int i = 1; i < players.size(); i++){
            if(players.get(i).getScore() == tiedPlayers.getFirst().getScore()) { tiedPlayers.add(players.get(i)); }
        }

        players = tiedPlayers;
    }
    /**
     * Attempts to open then read and save the contents of the dictionary text file.
     *
     * @throws FileNotFoundException When file trying to be read can not be found in specified directory.
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
