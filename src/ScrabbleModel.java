import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.*;
/**
 * Scrabble Model class.
 * This class is responsible for handling all the game logic after the player has made the move,
 * including the validity of the move and the score the player gained.
 * Scrabble Model class has an instance of all the other models of Scrabble and handles the interactions between
 * them.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-24-2024
 */
public class ScrabbleModel implements Serializable {
    // Constants and enumerations
    public enum Status {ONGOING, OVER}
    public static final int MAX_PLAYERS = 4;
    public static final int MIN_PLAYERS = 2;
    // List of observers
    ArrayList<ScrabbleModelView> views;
    // Class attributes
    private final Board board; // The scrabble game board
    private Status status; // The scrabble game status
    private int playerTurn; // Index of player in players whose turn it is
    private final TileBag tileBag; // The scrabble game tile bag
    private int scorelessTurns; // The number of scoreless turns to help update game status.
    private int selectedUserTile; //
    private final ArrayList<Player> players;
    private final HashSet<String> dictionary; // All valid words. Read from a .txt file.
    /**
     * Constructor method for ScrabbleModel
     * Initializes the board, the tile bag, dictionary of valid words, HashMaps of words on the board
     * previous words, HashSets of rows and columns played and the players.
     */
    public ScrabbleModel(){
        // Initialize observers list
        views = new ArrayList<>();
        // Initialize all legal words
        dictionary = new HashSet<>();
        try { getDictionaryFromFile(); }
        catch(Exception e){ ScrabbleModelViewFrame.fileReadError("File 'Words.txt' not found."); }
        // Initialize tile bag
        tileBag = new TileBag();
        //Initialize players as an empty array list. View called initPlayers
        players = new ArrayList<>();
        playerTurn = 0; // First person in players goes.
        selectedUserTile = -1;
        // Initialize board
        board = new Board();
        // Initialize game status
        status = Status.ONGOING;
    }
    /**
     * Accessor method for the game's board.
     *
     * @return The Scrabble game board.
     */
    public Board getBoard() { return board; } // Returns the Scrabble game's board.
    /**
     * Accessor method for the game's status.
     *
     * @return The current status of the Scrabble game.
     */
    public Status getStatus(){ return status; }
    /**
     * Accessor method for the index of the current player's turn.
     *
     * @return The index of the current player's turn.
     */
    public int getPlayerTurn() { return playerTurn; } // Gets the index of the current player's turn.
    /**
     * Accessor method for the game's players.
     *
     * @return All player's in the game.
     */
    public ArrayList<Player> getPlayers() { return players; } // Returns all players.
    /**
     * Accessor method for the game's dictionary.
     *
     * @return The game's dictionary (valid words).
     */
    public HashSet<String> getDictionary(){ return dictionary; } // Returns the entire dictionary (all 10,000 words).
    /**
     * Adds view to list of views.
     */
    public void addScrabbleView(ScrabbleModelViewFrame view){ this.views.add(view); } // Used to add view to update.
    /**
     * Returns the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer() { return players.get(playerTurn); } // Returns the current player object
    /**
     * Replaces empty tiles in a player's tile holder.
     */
    private void fillPlayerTiles(Player p){ for(int i = 0; i < Player.TILE_HOLDER_SIZE; i++) if(p.getTile(i) == null) if(tileBag.size() > 0) p.addTile(i, tileBag.popTile()); }
    /**
     * Determines whether the current player is an AI player.
     *
     * @return True if it's an AI player's turn, False otherwise.
     */
    public boolean checkAI(){ return getCurrentPlayer() instanceof PlayerAI; } // Checks to see if it's the computer's turn

    // Method to update views
    /**
     * Updates views in list of views.
     */
    private void update(){
        for(ScrabbleModelView view : views) view.updateBoard();
        selectedUserTile = -1;
    }
    /**
     * Change player to next player to play their turn.
     */
    private void changePlayer(){
        if(playerTurn == players.size()-1) playerTurn = 0; // Wrap around
        else playerTurn++; // Next player
    }
    /**
     * Determines whether the current turn is the first turn.
     *
     * @return True if it's the first turn, False otherwise.
     */
    private boolean firstTurn(){
        for(Player p : players) if(p.getScore() != 0) return false; // If a player has a score, then obviously not first turn.
        return true;
    }
    /**
     * Determines the length of the longest word out of a user's played words.
     *
     * @param wordsFound The words a user played during their turn.
     * @return The size of the largest played word.
     */
    private int getLargestWordLength(ArrayList<String> wordsFound){
        int max = 0;
        for(String s : wordsFound) if(s.length() > max) max = s.length();
        return max;
    }
    /**
     * Select a tile to be placed on the board.
     * @param index Index of tile to be placed.
     */
    public void handleTileSelection(int index){ if(getCurrentPlayer().getTile(index) != null) selectedUserTile = index; } // Player selects tile
    /**
     * Place a tile on the board.
     * @param r Row index
     * @param c Column index
     */
    public void handleBoardPlacement(int r, int c){
        if((selectedUserTile >= 0 && selectedUserTile < Player.TILE_HOLDER_SIZE) && board.getSqAtIndex(r, c).getTile() == null){
            board.getSqAtIndex(r, c).placeTile(getCurrentPlayer().popTile(selectedUserTile));
            for(ScrabbleModelView view : views) view.handleLetterPlacement(new ScrabbleEvent.TilePlacement(this, r, c, selectedUserTile));
            selectedUserTile = -1;
        }
    }
    /**
     * Initialize all players (human/AI) in the game, stores them in an array list, and randomized their order.
     *
     * @param humanNames ArrayList of the names of the players.
     * @param numAI The number of AI players.
     */
    public void initPlayers(ArrayList<String> humanNames, int numAI){
        // These arrays are used to initialize AI player names with sophisticated names.
        String[] namesAI = {"Megatron", "McLovin", "Quandale Dingle", "Billy Earl", "Kermit"};
        boolean[] availableNamesAI = {true, true, true, true, true};
        // Add human players. If their name is the same as an AI name, mark that AI name as unavailable.
        for(String name: humanNames){
            players.add(new Player(name));
            for(int i = 0; i < namesAI.length; i++) if(name.equalsIgnoreCase(namesAI[i])) availableNamesAI[i] = false;
        }
        // Initialize AI players (if any). Add them to player array list with one of the available names.
        for(int i = 0; i < numAI; i++){
            boolean added = false;
            while(!added){
                int randomName = new Random().nextInt(availableNamesAI.length);
                // If random name available, then add the AI player. Otherwise, try another name.
                if(availableNamesAI[randomName]){
                    players.add(new PlayerAI(namesAI[randomName]));
                    availableNamesAI[randomName] = false;
                    added = true;
                }
            }
        }
        // Randomize player order.
        Collections.shuffle(players);
        // Fill each player's tiles
        for(Player p : players) {
            fillPlayerTiles(p); // Give each player 7 tiles from the tile bag
            p.setPrevTiles(); // Set their previous tile states for invalid turn restoration.
        }
    }
    /**
     * Validate a players move and return a score if valid.
     * Deems whether a play is valid and returns a score accordingly. Score == 0 -> Invalid. Score >= 1 -> Valid.
     *
     * @param playCoordinates ArrayList of coordinates of the newly placed tiles.
     * @return The player's turn score (0 if invalid, 1 or more if valid).
     */
    public int validateAndScoreBoard(HashSet<int []> playCoordinates){
        if(getCurrentPlayer() instanceof PlayerAI) if(!allValid()) return 0;

        int turnScore = 0;
        ArrayList<String> wordsPlayed = new ArrayList<>(){};
        // Make sure that the coordinates provided are valid and that the center square is filled
        int coordCheckResult = checkCoordinates(playCoordinates);
        if(coordCheckResult == 0 || board.getSqAtIndex(7, 7).getTile() == null) return turnScore;


        // Hunt all solo letters. If a coordinate placed doesn't have any neighbors, illegal placement.
        HashSet<boolean[]> neighborCheck = new HashSet<>();
        for(int[] coord : playCoordinates){
            boolean[] c = {true, true, true, true}; // Check for each neighbor. True if there. False if not there.
            // +/- 1 for the row
            if(coord[0] + 1 < Board.BOARD_SIZE && coord[1] < Board.BOARD_SIZE) if(board.getSqAtIndex(coord[0] + 1, coord[1]).getTile() == null) c[0] = false;
            else c[0] = false;
            if(coord[0] - 1 >= 0 && coord[1] < Board.BOARD_SIZE) if(board.getSqAtIndex(coord[0] - 1, coord[1]).getTile() == null) c[1] = false;
            else c[1] = false;
            // +/- 1 for the column
            if(coord[1] + 1 < Board.BOARD_SIZE && coord[0] < Board.BOARD_SIZE) if(board.getSqAtIndex(coord[0], coord[1] + 1).getTile() == null) c[2] = false;
            else c[2] = false;
            if(coord[1] - 1 >= 0 && coord[0] < Board.BOARD_SIZE) if(board.getSqAtIndex(coord[0], coord[1] - 1).getTile() == null) c[3] = false;
            else c[3] = false;
            neighborCheck.add(c);
        }
        // If there is a single coordinate placed with no neighbors, invalid.
        if(neighborCheck.contains(new boolean[]{false, false, false, false})) return turnScore;

        // Must iterate through this entire process twice. Once for row word(s) and once for column word(s).
        for(int j = 0; j < 2; j++) {
            // Find beginning of each word. Read it from first to last letter, then score it.
            int i = 0;
            do {
                // Find beginning coordinate of the word played
                int[] beginningCoordinate = ((int[]) playCoordinates.toArray()[i]).clone(); // Need to clone the array so they don't refer to same object
                while (beginningCoordinate[j] > 0) {
                    if(beginningCoordinate[0] < Board.BOARD_SIZE && beginningCoordinate[1] < Board.BOARD_SIZE)
                        if (board.getSqAtIndex(j == 1 ? beginningCoordinate[0] : beginningCoordinate[0] - 1, j == 1 ? beginningCoordinate[1] - 1 : beginningCoordinate[1]).getTile() == null) break;
                    beginningCoordinate[j] -= 1;
                }

                // Read from top down. Score word. Check for bonus tile at placed index(es)
                StringBuilder word = new StringBuilder();
                int wordScore = 0;
                int wordBonus = 1;
                boolean playedCoord = false;
                while ((board.getSqAtIndex(beginningCoordinate[0], beginningCoordinate[1]).getTile() != null)) {
                    if(board.getSqAtIndex(beginningCoordinate[0], beginningCoordinate[1]).getTile().getChar().equals(" "))
                        board.getSqAtIndex(beginningCoordinate[0], beginningCoordinate[1]).getTile().setChar(ScrabbleModelViewFrame.getBlankInput());
                    word.append(board.getSqAtIndex(beginningCoordinate[0], beginningCoordinate[1]).getTile().getChar());
                    for(int[] coord : playCoordinates) {
                        if((coord[0] == beginningCoordinate [0]) && (coord[1] == beginningCoordinate[1])){
                            wordScore += board.getSqAtIndex(beginningCoordinate[0], beginningCoordinate[1]).getTile().getValue() * board.getSqAtIndex(beginningCoordinate[0], beginningCoordinate[1]).getLetterScore();
                            wordBonus *= board.getSqAtIndex(beginningCoordinate[0], beginningCoordinate[1]).getWordScore();
                            playedCoord = true;
                            break;
                        }
                    }
                    if(!playedCoord){
                        wordScore += board.getSqAtIndex(beginningCoordinate[0], beginningCoordinate[1]).getTile().getValue();
                    }
                    if(beginningCoordinate[j] != 14) beginningCoordinate[j] += 1;
                    else break;
                    playedCoord = false;
                }
                // Check if word is valid (in the dictionary). If not, return a score of 0.
                if (!dictionary.contains(word.toString())) return turnScore;


                // If length of word is greater than 1 (not just a letter), then score it
                if(word.length() > 1){
                    wordsPlayed.add(word.toString());
                    turnScore += wordScore * wordBonus;
                }
                i+=1;
            } while (j == 0 ? (i < playCoordinates.size()) && (coordCheckResult != 2) : (i < playCoordinates.size()) && (coordCheckResult != 1));
        }

        // Check that word play makes sense. If it's the first turn, then amount of tiles played should equal the largest word played. Otherwise, largest word played should be larger than number of tiles played.
        if((firstTurn() && (getLargestWordLength(wordsPlayed) != playCoordinates.size())) || (!firstTurn() && (getLargestWordLength(wordsPlayed) <= playCoordinates.size()))){
            return 0;
        }

        wordsPlayed.clear();
        playCoordinates.clear();
        return turnScore;
    }
    /**
     * Skip the current players turn.
     */
    public void skipTurn(){
        scorelessTurns += 1;
        invalidTurn(); // Invalidate current player's turn.
        changePlayer(); // Change player turns.
        update(); // Update view once player turn changes.
    }
    /**
     * Resets all necessary aspects of the game after an invalid player turn.
     */
    public void invalidTurn(){
        if(scorelessTurns == 6) status = Status.OVER;
        board.reset(); // Reset board
        getCurrentPlayer().resetTileHolder(); // Reset player tiles
        for(int i = 0; i < Player.TILE_HOLDER_SIZE; i++) if(getCurrentPlayer().getTile(i) != null) if(getCurrentPlayer().getTile(i).getValue() == 0) getCurrentPlayer().getTile(i).setChar(" "); // Reset blank tiles
        update(); // Update view.
    }
    /**
     * Saves necessary game states, increments player's score, then changes players before updating view.
     *
     * @param score Player's turn score to be added to their game score.
     */
    public void validTurn(int score){
        getCurrentPlayer().addToScore(score); // Add the turn score to the current player
        if(getCurrentPlayer().numTiles() == 0 && tileBag.size() >= 7) getCurrentPlayer().addToScore(50); // If they played 7 tiles, reward with bonus 50 points.
        board.saveState(); // Correct, so set new board prev state for possible future reset
        fillPlayerTiles(getCurrentPlayer()); // Fill current player's tiles
        getCurrentPlayer().setPrevTiles(); // Set prev state since turn was correct
        if(getCurrentPlayer().numTiles() == 0) status = Status.OVER; // If they don't have any tiles left, then the game is over.
        scorelessTurns = 0; // They scored, so reset scorelessTurns
        changePlayer(); // Change turns
        update(); // Update views
    }
    /**
     * Determines whether the play coordinates are valid and whether the word is a row word or column word.
     *
     * @param playCoordinates The coordinates played during a player's turn.
     * @return 0 if invalid, 1 if row word and valid, 2 if column word and valid
     */
    private int checkCoordinates(HashSet<int []> playCoordinates){
        if(playCoordinates.isEmpty()) return 0;

        HashSet<Integer> rowNums = new HashSet<>();
        HashSet<Integer> colNums = new HashSet<>();
        for(int[] coords : playCoordinates){
            rowNums.add(coords[0]);
            colNums.add(coords[1]);
        }
        if(rowNums.size() > 1 && colNums.size() > 1) return 0; // Can only play in one row or one column
        return rowNums.size() == 1 ?  1 : 2; // Row word (1), column word (2)
    }
    /**
     * Read the dictionary file and store the valid words in an ArrayList. Very minor, yet important part of the overall code.
     */
    private void getDictionaryFromFile() throws FileNotFoundException {
        BufferedReader r = new BufferedReader(new FileReader("./GameAssets/Words.txt"));
        try {
            String word = r.readLine();
            while (word != null) {
                dictionary.add(word.toUpperCase());
                word = r.readLine();
            }
        } catch(Exception e){ ScrabbleModelViewFrame.fileReadError("Error occurred when trying to read from 'Words.txt'."); }
    }
    /**
     * Determines whether the entire board if valid.
     * Checks that every single word on the board (length 1 or greater) is in the dictionary.
     *
     * @return True is all words on board are valid, False otherwise.
     */
    private boolean allValid(){
        StringBuilder rowWords = new StringBuilder();
        StringBuilder colWords = new StringBuilder();
        for(int r = 0; r < Board.BOARD_SIZE; r++){
            for(int c = 0; c < Board.BOARD_SIZE; c++){
                // Get row words
                if(board.getSqAtIndex(r,c).getTile() != null) rowWords.append(board.getSqAtIndex(r,c).getTile().getChar());
                else rowWords.append(" ");

                // Get column words
                if(board.getSqAtIndex(c,r).getTile() != null) colWords.append(board.getSqAtIndex(c,r).getTile().getChar());
                else colWords.append(" ");
            }
            rowWords.append(" ");
            colWords.append(" ");
        }

        String[] rw = rowWords.toString().split(" ");
        String[] cw = colWords.toString().split(" ");
        for(int i = 0; i < rw.length; i++) rw[i] = rw[i].replace(" ", "");
        for(int i = 0; i < cw.length; i++) cw[i] = cw[i].replace(" ", "");

        for(String word : rw) if(!dictionary.contains(word) && word.length() >= 2) return false;
        for(String word : cw) if(!dictionary.contains(word) && word.length() >= 2) return false;
        return true;
    }
    /**
     * Determines the final player scores and provides a string representation of the games results.
     *
     * @return A string representation of the game's results.
     */
    public String gameResults(){
        boolean[] playedOut = new boolean[players.size()];
        int[] remainingTilesSum = new int[players.size()];

        for(int i = 0; i < players.size(); i++){
            if(players.get(i).numTiles() == 0) playedOut[i] = true;
            else{
                for(int t = 0; t < Player.TILE_HOLDER_SIZE; t++) if(players.get(i).getTile(t) != null) remainingTilesSum[i] += players.get(i).getTile(t).getValue();
                players.get(i).addToScore(-1 * remainingTilesSum[i]);
            }
        }

        for(int i = 0; i < players.size(); i++) if(playedOut[i]) players.get(i).addToScore(Arrays.stream(remainingTilesSum).sum());

        StringBuilder results = new StringBuilder();
        results.append(String.format("%30s%30s%30s%30s", "Name", "Tile Remainder", "Bonus", "Final Score\n"));
        for(int i = 0; i < players.size(); i++){
            results.append(String.format("%30s%30s%30s%30s\n", players.get(i).getName(), remainingTilesSum[i], playedOut[i] ? Arrays.stream(remainingTilesSum).sum() : 0, players.get(i).getScore()));
        }

        return results.toString();
    }
}

