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
            for(Player p : players){ playerTurn(p); }
            gameStatus = false; // Only does one round. NEEDS REMOVED...
        }
    }

    private void playerTurn(Player p){
        view.printBoard(board);
        view.printPlayerTiles(p);
        boolean validTurn = false;
        while(!validTurn){
            int c = view.getCharToInt("\nPlayer choices\n\t(P) Place Word\n\t(S) Skip Turn\n\t(Q) Quit Game\nEnter choice: ", "PSQ", 0);
            // Player decides to play a word
            if(c == 80){
                // Get word that player wants to try to play
                String word = view.getString("Enter word to play: ").toUpperCase();

                // Get coordinates on board for player to play the word
                int[] coordinates = {view.getCharToInt("\nEnter X Coordinate (A-O): ", "ABCDEFGHIJKLMNO", 65), view.getInt("\nEnter Y Coordinate (1-15): ", 1, 15)-1};
                int direction = view.getCharToInt("\nEnter word direction up-to-down (D) or left-to-right (R)", "DR", 68);

                // All tiles in players hand that are playable
                ArrayList<Tile> playable = new ArrayList<>();

                // Check that word is in dictionary
                if(dictionary.contains(word.toLowerCase())) {
                    // ------------------ FIGURE OUT WHAT TILES THE PLAYER HAS FOR WORD -----------------------------
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
                        int coords[] = coordinates;
                        // THIS CASE IS EMPTY BOARD & NEED TO PLAY IN CENTER
                        boolean isCentered = false;
                        for(int i = 0; i < word.length(); i++){
                            if(direction == 0){
                                if (coords[0] + i == 7 && coords[1] == 7) {
                                    isCentered = true;
                                    break;
                                }
                            }
                            else{
                                if (coords[0] == 7 && coords[1] + i == 7) {
                                    isCentered = true;
                                    break;
                                }
                            }
                        }
                        // Word is centered and played correctly
                        if(isCentered){
                            validTurn = true;
                            for(int i = 0; i < word.length(); i++){
                                board.placeTile(playable.get(i), coords);
                                p.removeTile(playable.get(i));
                                if(direction != 0){ coords[1]+=1; }
                                else{ coords[0]+=1; }
                            }

                        }

                        if(validateBoard()){
                            validTurn = true;
                        }
                        else{
                            for(int j = 0; j < word.length(); j++){
                                p.addTileToHolder(board.popTile(coordinates));
                                if(direction != 0){ coordinates[1]+=1; }
                                else{ coordinates[0]+=1; }
                            }
                        }

                    }

                    // Any other play except first
                    else if (!board.isEmpty() && !lettersNotFound.isEmpty()) {
                        // Check tiles not in hand are in right spot on board
                        boolean validBoardTiles = true;


                        for(int i = lettersNotFound.length()-1; i >= 0; i--){
                            ArrayList<Integer> indexes = getCharIndexes(word, lettersNotFound.charAt(i));
                            for(Integer z : indexes){
                                System.out.println(z);
                            }
                            if(indexes.isEmpty()){
                                validBoardTiles = false;
                                break;
                            }
                            for(int x : indexes.reversed()){
                                int [] coords = coordinates;
                                if(direction != 0){ coords[1]+=x; }
                                else{ coords[0]+=x; }
                                if(board.getLetterAtIndex(coords) != null){
                                    if(board.getLetterAtIndex(coords).charAt(0) == lettersNotFound.charAt(i)){
                                        validBoardTiles = true;
                                        break;
                                    }
                                }
                                validBoardTiles = false;
                            }
                        }

                        int coords[] = coordinates;
                        int numLetters = 0;
                        for(int i = 0; i < word.length(); i++){
                            if(board.getLetterAtIndex(coords)!=null){
                                numLetters+=1;
                            }
                            if(direction != 0){ coords[1]+=1; }
                            else{ coords[0]+=1; }
                        }
                        if(numLetters != lettersNotFound.length()){
                            validBoardTiles = false;
                        }
                        System.out.println("Valid board tiles: " + validBoardTiles);
                        Board b = board;
                        if(validBoardTiles){
                            int letInd = 0;
                            for(int i = 0; i < word.length()-1; i++){
                                if(board.getLetterAtIndex(coords) == null){
                                    board.placeTile(playable.get(letInd), coords);
                                    p.removeTile(playable.get(letInd));
                                    letInd+=1;
                                }
                                if(direction != 0){ coords[1]+=1; }
                                else{ coords[0]+=1; }
                            }
                        }

                        if(validateBoard()){
                            System.out.println("Validation complete and true");
                            validTurn = true;
                        }
                        else{
                            board = b;
                            for(Tile t : playable){
                                p.addTileToHolder(t);
                            }
                        }
                    }

                }
            }
            else if(c == 83){
                // TEMPORARY
                System.out.println(p.getName() + "decided to skip their turn.");
            }
            else{
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
                if (!dictionary.contains(s.strip().toLowerCase())) { return false; }
            }

            // Validate all column words
            String[] colW = columnWords.split(" ");
            for (String s : colW) {
                if (!dictionary.contains(s.strip().toLowerCase())) { return false; }
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
