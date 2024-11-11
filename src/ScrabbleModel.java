import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.Key;
import java.util.*;


/**
 * TODO: JavaDoc for Scrabble model class
 */
public class ScrabbleModel {

    private List<ScrabbleModelView> views;

    public static final int BOARD_SIZE = 15;
    public static final int NUM_PLAYER_TILES = 7;

    public enum Status {ONGOING, WIN};
    private Status status;

    private Board board;
    private TileBag tiles;
    private ArrayList<Player> players;
    private int playerTurn;

    private BoardSquare selectedBS;
    private int selectedUserTile;

    private HashSet<String> dictionary; // valid words

    private HashSet<Integer> rowsPlayed;
    private HashSet<Integer> colsPlayed;
    private HashMap<String, Integer> wordsOnBoard;
    private HashMap<String, Integer> bWordsPrev;



    public ScrabbleModel(int numPlayers){
        this.board = new Board();
        this.board.setPrevState();
        this.tiles = new TileBag();
        this.players = initPlayers(numPlayers);
        this.status = Status.ONGOING;
        playerTurn = 0; // First player's turn
        this.views = new ArrayList<ScrabbleModelView>();


        dictionary = new HashSet<>();
        try{
            getWordsFromFile();
        }
        catch(Exception e){
            System.out.println("File read error. Exception: " + e); // TODO: TEMPORARY. Replace with JOptionPane?
        }

        selectedUserTile = -1;

        rowsPlayed = new HashSet<Integer>();
        colsPlayed = new HashSet<Integer>();

        wordsOnBoard = new HashMap<>();
        bWordsPrev = new HashMap<>();

        for(Player p : players){
            p.setPrevTiles();
        }
    }

    public Board getBoard() {
        return board;
    }

    public Status getStatus() {
        return status;
    }

    public Player getCurrentPlayer(){
        return this.players.get(playerTurn);
    }

    public void addScrabbleView(ScrabbleModelViewFrame view){
        this.views.add(view);
    }

    private ArrayList<Player> initPlayers(int n){
        ArrayList<Player> ps = new ArrayList<Player>(); // Initialize array list to return

        // Initialize each player name (Player1, ..., PlayerN)
        for(int i = 1; i <= n; i++){
            ps.add(new Player(String.format("Player %d", i)));
        }

        // Give each player 7 tiles
        for(Player p : ps)
            fillTiles(p);

        return ps; // Return players
    }

    private void fillTiles(Player p){
        if(tiles.returnSize() != 0 && p.getTiles().size() < NUM_PLAYER_TILES){
            int size =  p.getTiles().size();
            for(int i = 0; i < (ScrabbleModel.NUM_PLAYER_TILES - size); i++)
                p.addTileToHolder(tiles.popTile());
        }
        else if(tiles.returnSize() != 0 && p.getTiles().size() == NUM_PLAYER_TILES){
            System.out.println("fskdjflskdjfskl");
            for(int i = 0; i < NUM_PLAYER_TILES; i++){
                if(p.getTiles().get(i) == null) p.getTiles().set(i, tiles.popTile());
            }
        }
    }

    private void changePlayer(){
        if(playerTurn == players.size()-1) playerTurn = 0;
        else playerTurn++;
    }

    public void handleUserTile(int r){
        if(r < getCurrentPlayer().getTiles().size()){
            if(getCurrentPlayer().getTiles().get(r) != null)
                selectedUserTile = r;
        }
    }

    public void handleBoardSquare(int r, int c){
        if(selectedUserTile >= 0 && selectedUserTile < NUM_PLAYER_TILES){
            if(Objects.equals(board.getLetterAtIndex(r, c), null)){
                board.placeTile(getCurrentPlayer().getTiles().get(selectedUserTile), r, c);
                rowsPlayed.add(r);
                colsPlayed.add(c);
                getCurrentPlayer().setTileAsUsed(selectedUserTile);

                for(ScrabbleModelView view : views){
                    view.handleLetterPlacement(new ScrabbleEvent(this, r, c, selectedUserTile, board, getCurrentPlayer()));
                }
            }
        }
    }

    public void validateAndScoreBoard(ArrayList<int []> playCoordinates){
        // Validation stuff
        boolean valid = true;
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
                    if (!(dictionary.contains(s.strip().toLowerCase()))) { valid = false; } // If the dictionary doesn't contain one of the words then board isn't valid.
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
                    if (!(dictionary.contains(s.strip().toLowerCase()))) { valid = false; } // If the dictionary doesn't contain one of the words then board isn't valid.
                    else{
                        if(allWords.containsKey(s)) allWords.put(s, allWords.get(s) + 1);
                        else allWords.put(s, 1);
                    }
                }
            }

        }

        System.out.println(String.format("%s\n%s", soloRowLetters, soloColLetters));

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

            score += wordScore;
        }

        // Validate that there are no clashing (i.e. row and column should not produce single letters at same coordinate)
        String[] rowSoloCords = soloRowLetters.toString().strip().split(":");
        String[] colSoloCords = soloColLetters.toString().strip().split(":");

        for(String rs : rowSoloCords){
            for(String cs : colSoloCords){

                if(!cs.isEmpty()){
                    System.out.println(cs);
                    if(!rs.isEmpty()) System.out.println(rs);
                }
                if(rs.equalsIgnoreCase(cs) && !cs.isEmpty()){
                    valid = false; // Two solo letters can't have same coordinates!
                    System.out.println("SOLO!!!");
                    System.out.printf("\n\"%s\" \"%s\"%n", rs, cs);
                    break; // Loop can be terminated once condition is met.
                }
            }
        }

        if(board.isEmpty() || board.getLetterAtIndex(7,7) == null || score == 0){
            valid = false;
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
        System.out.println("!!!!!" + wordsPlayed.toString());

        if(!wordsPlayed.isEmpty() && !bWordsPrev.isEmpty()){
            int largestWord = 0;
            for(String key : wordsPlayed.keySet()){
                if(key.length() > largestWord) largestWord = key.strip().length();
            }

            System.out.println(String.format("Coord: %d    Word: %d", playCoordinates.size(), largestWord));
            if(largestWord <= playCoordinates.size()){
                valid = false;
            }
        }

        // Make sure they played within the same row or column.
        if((colsPlayed.size() > 1 && rowsPlayed.size() > 1)){
            System.out.println("Didn't play in same spot!!!");
            valid = false;
        }


        if(!valid){
            board.reset(); // Reset board
            getCurrentPlayer().resetTiles(); // Reset player tiles
            wordsOnBoard = new HashMap<>(bWordsPrev);
        }
        else if(getCurrentPlayer().getPlayed()){
            getCurrentPlayer().addToScore(score);
            board.setPrevState(); // Correct, so set new board prev state for possible future reset
            fillTiles(getCurrentPlayer()); // Fill player's tiles who just went
            getCurrentPlayer().setPrevTiles();
            for(ScrabbleModelView view : views) view.updateBoard(new ScrabbleEvent(this, 0, 0, selectedUserTile, board, getCurrentPlayer()));
            changePlayer(); // Change turns
            bWordsPrev = new HashMap<>(wordsOnBoard);
        }
        for(ScrabbleModelView view : views)
            view.updateBoard(new ScrabbleEvent(this, 0, 0, selectedUserTile, board, getCurrentPlayer()));

        playCoordinates.clear();
        System.out.println(wordsOnBoard.toString());

        selectedUserTile = -1;

        rowsPlayed.clear();
        colsPlayed.clear();


    }


    // TODO: skipTurn function contains a lot of the same logic as an invalid turn. They can simply be combined into a helper function...
    public void skipTurn(){
        board.reset(); // Reset board
        getCurrentPlayer().resetTiles(); // Reset player tiles
        wordsOnBoard = new HashMap<>(bWordsPrev);
        selectedUserTile = -1;
        rowsPlayed.clear();
        colsPlayed.clear();
        changePlayer(); // Change turns
        for(ScrabbleModelView view : views)
            view.updateBoard(new ScrabbleEvent(this, 0, 0, selectedUserTile, board, getCurrentPlayer()));
    }


    private void getWordsFromFile() throws FileNotFoundException {
        BufferedReader r = new BufferedReader(new FileReader("./Words.txt"));
        try {
            String word = r.readLine();
            while (word != null) {
                dictionary.add(word);
                word = r.readLine();
            }
        } catch (Exception e) {
            System.out.println("IOException: " + e); // TODO: Replace. JOptionPane? Needs to go in GUI part
        }
    }


    public ArrayList<Player> getPlayers(){ return players; }

}
