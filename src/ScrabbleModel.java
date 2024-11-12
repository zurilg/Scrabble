import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


/**
 * Scrabble Model class
 * This class is responsible for handling all the game logic after the player has made the move,
 * including the validity of the move and the score the player gained.
 *
 * Scrabble Model he class has an instance of all the other models of Scrabble and handles the interactions between
 * them.
 *
 * @author Zuri Lane-Griffore
 * @author Mohammad Ahmadi
 * @author Abdul Aziz Al-Sibakhi
 * @author Redah Eliwa
 *
 * @version 11-12-2024
 */
public class ScrabbleModel {
    // List of views to be updated
    private final List<ScrabbleModelView> views;
    // Constants and macros to be used
    public static final int BOARD_SIZE = 15;
    public static final int NUM_PLAYER_TILES = 7;
    public enum Status {ONGOING, OVER}

    private Status status; // Store current game status
    private final Board board; // Game board
    private final TileBag tiles; // Tile bag
    private final ArrayList<Player> players; // Players
    private int playerTurn; // Variable to track player turn
    private int scorelessTurns; // Keep track of number of scoreless turns

    private int selectedUserTile;
    // All playable words. Read from a .txt file currently.
    private final HashSet<String> dictionary; // valid words

    private final HashSet<Integer> rowsPlayed;
    private final HashSet<Integer> colsPlayed;
    private HashMap<String, Integer> wordsOnBoard;
    private HashMap<String, Integer> bWordsPrev;


    /**
     * Constructor method for ScrabbleModel
     * Initializes the board, the tile bag, dictionary of valid words, HashMaps of words on the board
     * previous words, HashSets of rows and columns played and the players.
     * Order of players is randomly chosen.
     * @param playerNames An ArrayList of Strings that holds all the names of all the players in the game
     */
    public ScrabbleModel(ArrayList<String> playerNames){
        this.board = new Board();
        this.board.setPrevState();
        this.tiles = new TileBag();
        this.players = initPlayers(playerNames);
        this.scorelessTurns = 0;
        this.status = Status.ONGOING;
        playerTurn = new Random().nextInt(playerNames.size()); // Pick random player to be first
        this.views = new ArrayList<>();


        dictionary = new HashSet<>();
        try{
            getWordsFromFile();
        }
        catch(Exception e){
            System.out.println("File read error."); // Will replace in future with
        }

        selectedUserTile = -1;

        rowsPlayed = new HashSet<>();
        colsPlayed = new HashSet<>();

        wordsOnBoard = new HashMap<>();
        bWordsPrev = new HashMap<>();

        for(Player p : players){
            p.setPrevTiles();
        }
    }
    /**
     * Return the current state of the board.
     * @return The board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the current player that is playing a turn.
     * @return The player playing their turn.
     */
    public Player getCurrentPlayer(){
        return this.players.get(playerTurn);
    }

    /**
     * Add an instance of ScrabbleModelView to the array list of ScrabbleModelViews.
     * @param view Array list of ScrabbleModelViews
     */
    public void addScrabbleView(ScrabbleModelViewFrame view){
        this.views.add(view);
    }

    /**
     * Initialize all the players in the game and store them in an array list.
     * @param playerNames ArrayList of the names of the players.
     * @return ArrayList of the player objects representing the players playing the game.
     */
    private ArrayList<Player> initPlayers(ArrayList<String> playerNames){
        ArrayList<Player> ps = new ArrayList<>(); // Initialize array list to return

        // Initialize each player name (Player1, ..., PlayerN)
        for(String name : playerNames)
            ps.add(new Player(name));

        // Give each player 7 tiles
        for(Player p : ps)
            fillTiles(p);

        return ps; // Return players
    }

    /**
     * Fill the tile holders of the player.
     * @param p The player whose tile holder is to be filled.
     */
    private void fillTiles(Player p){
        // If the player's tile holder is empty then add tile objects
        if(tiles.returnSize() != 0 && p.getTiles().size() < NUM_PLAYER_TILES){
            int size =  p.getTiles().size();
            for(int i = 0; i < (ScrabbleModel.NUM_PLAYER_TILES - size); i++)
                p.addTileToHolder(tiles.popTile());
        }
        // If the player's tile holder is full, replace non-valid tiles
        else if(tiles.returnSize() != 0 && p.getTiles().size() == NUM_PLAYER_TILES){
            for(int i = 0; i < NUM_PLAYER_TILES; i++){
                if(p.getTiles().get(i) == null) p.getTiles().set(i, tiles.popTile());
            }
        }
    }
    /**
     * Change player to next player to play their turn.
     */
    private void changePlayer(){
        if(playerTurn == players.size()-1) playerTurn = 0;
        else playerTurn++;
    }
    /**
     * Select a tile to be placed on the board.
     * @param r Index of tile to be placed.
     */
    public void handleUserTile(int r){
        if(r < getCurrentPlayer().getTiles().size()){
            if(getCurrentPlayer().getTiles().get(r) != null)
                selectedUserTile = r;
        }
    }
    /**
     * Place a tile on the board.
     * @param r Row index
     * @param c Column index
     */
    public void handleBoardSquare(int r, int c){
        if(selectedUserTile >= 0 && selectedUserTile < NUM_PLAYER_TILES){
            if(Objects.equals(board.getLetterAtIndex(r, c), null)){
                board.placeTile(getCurrentPlayer().getTiles().get(selectedUserTile), r, c);
                rowsPlayed.add(r);
                colsPlayed.add(c);
                getCurrentPlayer().setTileAsUsed(selectedUserTile);

                for(ScrabbleModelView view : views){
                    view.handleLetterPlacement(new ScrabbleEvent(this, r, c, selectedUserTile, board, getCurrentPlayer(), status));
                }
            }
        }
    }
    /**
     * Validate a players move and add the score if the move made is valid.
     * @param playCoordinates ArrayList of coordinates of the newly placed tiles.
     */
    public void validateAndScoreBoard(ArrayList<int []> playCoordinates){
        // Validation stuff
        StringBuilder soloRowLetters = new StringBuilder();
        StringBuilder soloColLetters = new StringBuilder();

        // Scoring stuff
        int score = 0;
        int [] tileValues = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
        HashMap<String, Integer> allWords = new HashMap<>();

        // Words state restore
        bWordsPrev = new HashMap<>(wordsOnBoard);
        HashMap<String, Integer> wordsPlayed = new HashMap<>();

        // Check all rows and columns for valid data
        for(int r = 0; r < BOARD_SIZE; r ++){
            // To read all words written from left-to-right and up-to-down
            StringBuilder rowWords = new StringBuilder();
            StringBuilder columnWords = new StringBuilder();
            // Read rows and columns
            for(int c = 0; c < BOARD_SIZE; c ++){
                // If there is a letter at certain row coordinate, add it to rowWords. Otherwise, add a space to rowWords.
                if(board.getLetterAtIndex(r, c)!=null){
                    rowWords.append(board.getLetterAtIndex(r, c));

                    // Find solo letters

                    if(c == 0){
                        if(board.getLetterAtIndex(r, c+1) == null) soloRowLetters.append(String.format("%d%d:", r, c));
                    }
                    else if(c == 14){
                        if(board.getLetterAtIndex(r, c-1) == null) soloRowLetters.append(String.format("%d%d:", r, c));
                    }
                    else{
                        if((board.getLetterAtIndex(r, c+1) == null) && (board.getLetterAtIndex( r, c-1) == null)) soloRowLetters.append(String.format("%d%d:", r, c));
                    }
                }
                else{ rowWords.append(" "); }

                // If there is a letter at certain column coordinate, add it to columnWords. Otherwise, add a space to columnWords.
                if(board.getLetterAtIndex(c, r) != null){
                    columnWords.append(board.getLetterAtIndex(c, r));

                    // Find solo letters
                    if(c == 0){
                        if(board.getLetterAtIndex(c + 1, r) == null) soloColLetters.append(String.format("%d%d:", c, r));
                    }
                    else if(c == 14){
                        if(board.getLetterAtIndex(c - 1, r) == null) soloColLetters.append(String.format("%d%d:", c, r));
                    }
                    else{
                        if((board.getLetterAtIndex(c + 1, r) == null) && (board.getLetterAtIndex( c - 1, r) == null)) soloColLetters.append(String.format("%d%d:", c, r));
                    }
                }
                else{ columnWords.append(" "); }
            }

            // Validate all row words
            String[] rowW = rowWords.toString().split(" ");
            for (String s : rowW) {
                if(s.length()>1){
                    // If the dictionary doesn't contain one of the words then board isn't valid.
                    if (!(dictionary.contains(s.strip().toLowerCase()))) {
                        invalidTurn(playCoordinates);
                        return;
                    }
                    else{
                        if(allWords.containsKey(s)) allWords.put(s, allWords.get(s) + 1);
                        else allWords.put(s, 1);
                    }
                }
            }

            // Validate all column words
            String[] colW = columnWords.toString().split(" ");
            for (String s : colW) {
                if(s.length()>1){
                    if (!(dictionary.contains(s.strip().toLowerCase()))) {
                        invalidTurn(playCoordinates);
                        return;
                    } // If the dictionary doesn't contain one of the words then board isn't valid.
                    else{
                        if(allWords.containsKey(s)) allWords.put(s, allWords.get(s) + 1);
                        else allWords.put(s, 1);
                    }
                }
            }

        }

        for(String key : allWords.keySet()){
            int wordScore = 0;
            if (wordsOnBoard.containsKey(key)) {
                if(!wordsOnBoard.get(key).equals(allWords.get(key))){
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

            score += wordScore;
        }

        // Validate that there are no clashing (i.e. row and column should not produce single letters at same coordinate)
        String[] rowSoloCords = soloRowLetters.toString().strip().split(":");
        String[] colSoloCords = soloColLetters.toString().strip().split(":");

        for(String rs : rowSoloCords){
            for(String cs : colSoloCords){
                if(rs.equalsIgnoreCase(cs) && !cs.isEmpty()){
                    invalidTurn(playCoordinates);
                    return;
                }
            }
        }

        // If the board is empty or
        if(board.getLetterAtIndex(7,7) == null || score == 0){
            invalidTurn(playCoordinates);
            return;
        }

        for(String k : wordsOnBoard.keySet()){
            if(bWordsPrev.containsKey(k)){
                if((allWords.get(k) != null) && (allWords.get(k) - bWordsPrev.get(k) > 0))
                    wordsPlayed.put(k, allWords.get(k) - bWordsPrev.get(k));
            }
            else if(dictionary.contains(k.toLowerCase())){
                wordsPlayed.put(k, allWords.get(k));
            }
        }

        if(!wordsPlayed.isEmpty() && !bWordsPrev.isEmpty()){
            int largestWord = 0;
            for(String key : wordsPlayed.keySet()){
                if(key.length() > largestWord) largestWord = key.strip().length();
            }

            if(largestWord <= playCoordinates.size()){
                invalidTurn(playCoordinates);
                return;
            }
        }

        // Make sure they played within the same row or column.
        if((colsPlayed.size() > 1 && rowsPlayed.size() > 1)){
            invalidTurn(playCoordinates);
            return;
        }

        if(getCurrentPlayer().getPlayed()){
            validTurn(score);
            playCoordinates.clear();
        }

        for(ScrabbleModelView view : views)
            view.updateBoard(new ScrabbleEvent(this, 0, 0, selectedUserTile, board, getCurrentPlayer(), status));
    }

    /**
     * Sets a move as invalid.
     *Resets the players tiles and returns board to previous state.
     * If 6 scoreless turns have been made, change the game status as over.
     * @param coords Coordinates of the move
     */
    private void invalidTurn(ArrayList<int[]> coords){
        if(coords != null) coords.clear();
        if(scorelessTurns == 6) status = Status.OVER;
        board.reset(); // Reset board
        getCurrentPlayer().resetTiles(); // Reset player tiles
        wordsOnBoard = new HashMap<>(bWordsPrev);
        update();
    }
    /**
     * Sets a move as valid.
     * Add to the players total score and updates the board.
     * If the player has 0 tiles left and tile bad is empty change the status of the game to over.
     * @param score The score the player achieved from the move
     */
    private void validTurn(int score){
        getCurrentPlayer().addToScore(score);
        board.setPrevState(); // Correct, so set new board prev state for possible future reset
        fillTiles(getCurrentPlayer()); // Fill player's tiles who just went
        getCurrentPlayer().setPrevTiles();
        if(getCurrentPlayer().numTiles() == 0) status = Status.OVER;
        for(ScrabbleModelView view : views) view.updateBoard(new ScrabbleEvent(this, 0, 0, selectedUserTile, board, getCurrentPlayer(), status));
        changePlayer(); // Change turns
        bWordsPrev = new HashMap<>(wordsOnBoard);
        update();
    }
    /**
     * Updates calls the views in the ArrayList to update the board.
     */
    private void update(){
        for(ScrabbleModelView view : views)
            view.updateBoard(new ScrabbleEvent(this, 0, 0, selectedUserTile, board, getCurrentPlayer(), status));
        selectedUserTile = -1;
        rowsPlayed.clear();
        colsPlayed.clear();
    }
    /**
     * Skip the turn and change turns.
     * Marks move as scoreless.
     */
    public void skipTurn(){
        scorelessTurns += 1;
        changePlayer(); // Change turns
        invalidTurn(null);
    }

    /**
     * Read the dictionary file and store the valid words in an ArrayList.
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
            System.out.println("Error when reading from file."); // Should replace eventually
        }
    }

    /**
     * Getter method for the ArrayList of players
     * @return ArrayList of players
     */
    public ArrayList<Player> getPlayers(){ return players; }

}
