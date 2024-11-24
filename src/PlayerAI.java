import java.util.*;

public class PlayerAI extends Player{
    public PlayerAI(String name){
        super(name);
    }

    // Outer array list -> represents the collection of all possible plays.
    // Inner array lists -> each one represents a possible set of plays.
    // Plays consist of multiple tile placements (integer arrays) that have the form [Tile Holder Index, Board Row, Board Column]
    public ArrayList<ArrayList<int[]>> getValidPlays(Board board, HashSet<String> dictionary){
        ArrayList<ArrayList<int[]>> plays = new ArrayList<>();
        if(board.getSqAtIndex(7, 7).getTile() != null){
            // First, we need to read all the words off of the board.
            StringBuilder rowWords = new StringBuilder();
            StringBuilder colWords = new StringBuilder();
            ArrayList<ArrayList<int[]>> rowWordCoordinates = new ArrayList<>();
            ArrayList<ArrayList<int[]>> colWordCoordinates = new ArrayList<>();
            for(int r = 0; r < Board.BOARD_SIZE; r++){
                ArrayList<int[]> rowCoords = new ArrayList<>();
                ArrayList<int[]> colCoords = new ArrayList<>();
                boolean end = false;
                for(int c = 0; c < Board.BOARD_SIZE; c++){
                    // Get row words
                    if(board.getSqAtIndex(r,c).getTile() != null){
                        rowWords.append(board.getSqAtIndex(r,c).getTile().getChar());
                        rowCoords.add(new int[]{r, c});
                    }
                    else {
                        if(!rowCoords.isEmpty()){
                            rowWordCoordinates.add(new ArrayList<>(rowCoords));
                            rowCoords.clear();
                            end = true;
                        }
                        if(end){
                            rowWords.append(",");
                            end = false;
                        }
                    }
                    // Get column words
                    if(board.getSqAtIndex(c,r).getTile() != null){
                        colWords.append(board.getSqAtIndex(c,r).getTile().getChar());
                        colCoords.add(new int[]{c, r});
                    }
                    else {
                        if(!colCoords.isEmpty()){
                            colWordCoordinates.add(new ArrayList<>(colCoords));
                            colCoords.clear();
                            end = true;
                        }
                        if(end) {
                            colWords.append(",");
                            end = false;
                        }
                    }
                }
            }

            String[] rw = rowWords.toString().split(",");
            String[] cw = colWords.toString().split(",");

            for(int i = 0; i < rw.length; i++) {
                ArrayList<String> possiblePlays = new ArrayList<>();  // Keeps track of all possible plays for rw[i]
                for (String word : dictionary) {
                    if (word.contains(rw[i]) && !word.equals(rw[i]) && (word.length() <= TILE_HOLDER_SIZE + rw[i].length()) && i < rowWordCoordinates.size()) {
                        ArrayList<int[]> rowPlay = checkResources(word, rw[i], rowWordCoordinates.get(i), true);
                        if(rowPlay != null){
                            boolean valid = true;
                            for(int[] o : rowPlay){
                                if(o[0] != -1 && board.getSqAtIndex(o[1], o[2]).getTile() != null){
                                    valid = false;
                                    break;
                                }
                            }
                            if(valid) plays.add(rowPlay);
                        }
                    }
                }
            }

            for(int i = 0; i < cw.length; i++) {
                ArrayList<String> possiblePlays = new ArrayList<>();  // Keeps track of all possible plays for rw[i]
                for (String word : dictionary) {
                    if (word.contains(cw[i]) && !word.equals(cw[i]) && (word.length() <= TILE_HOLDER_SIZE + cw[i].length()) && word.length() > 1 && i < colWordCoordinates.size()) {
                        ArrayList<int[]> colPlay = checkResources(word, cw[i], colWordCoordinates.get(i), false);
                        if(colPlay != null) {
                            boolean valid = true;
                            for (int[] o : colPlay) {
                                if (o[0] != -1 && board.getSqAtIndex(o[1], o[2]).getTile() != null) {
                                    valid = false;
                                    break;
                                }
                            }
                            if (valid) plays.add(colPlay);
                        }
                    }
                }
            }
            return plays;
        }

        else{
            System.out.println("HERE");
            for(int i = 0; i < TILE_HOLDER_SIZE; i++){
                ArrayList<String> words = new ArrayList<>();
                for(String word : dictionary) if(word.contains(getTile(i).getChar()) && word.length() <= TILE_HOLDER_SIZE && word.length() > 1) words.add(word);
                for(String word : words){
                    ArrayList<int[]> play = new ArrayList<>(word.length());
                    // Used to keep track of tile/word comparisons
                    StringBuilder w = new StringBuilder(word);
                    boolean[] claimed = new boolean[TILE_HOLDER_SIZE];
                    int[] order = new int[word.length()];
                    // Find the tile
                    for(int s = 0; s < word.length(); s++){
                        if(word.charAt(s) == getTile(i).getChar().charAt(0)) {
                            w.setCharAt(s, '-');
                            claimed[i] = true;
                            order[s] = i;
                        }
                    }
                    // Find remaining tiles
                    for(int s = 0; s < word.length(); s++){
                        if(w.charAt(s) != '-'){
                            for(int k = 0; k < TILE_HOLDER_SIZE; k++){
                                if(w.charAt(s) == getTile(k).getChar().charAt(0) && !claimed[k]){
                                    w.setCharAt(s, '-');
                                    claimed[k] = true;
                                    order[s] = k;
                                }
                            }
                        }
                    }
                    // Check if we have the complete word
                    boolean complete = true;
                    for(int s = 0; s < word.length(); s++) if(w.charAt(s) != '-') complete = false;
                    // If we found complete word, return that play just playing from middle to the right.
                    if(complete){
                        int c = 7;
                        for(int z = 0; z < w.length(); z++){
                            System.out.println("COMPLETE WORD: " + word);
                            play.add(new int[]{order[z], 7, c});
                            c++; // increment column since playing row word
                        }
                        plays.add(play);
                        return plays;
                    }
                }
            }
            return null;
        }
    }

    private ArrayList<int[]> checkResources(String word, String wordOnBoard, ArrayList<int[]> coords, boolean wordType) {
        ArrayList<int[]> play = new ArrayList<>();
        for(int z = 0; z < word.length(); z++) play.add(new int[]{});
        int[] coordinate = new int[]{coords.getFirst()[0], coords.getFirst()[1]};

        StringBuilder w = new StringBuilder(word);
        // Find where the word board occurs in the dictionary word
        for (int j = 0; j < word.length(); j++) {
            if (wordOnBoard.charAt(0) == word.charAt(j)) {
                boolean correctIndex = false; // Makes sure that letters aren't incorrectly written over.
                for(int k = 0; k < wordOnBoard.length(); k++){
                    if(wordOnBoard.charAt(k) == word.charAt(j+k)) correctIndex = true;
                    else{
                        correctIndex = false;
                        break;
                    }
                }
                if(correctIndex){
                    for(int k = 0; k < wordOnBoard.length() && k < w.length() && k < coords.size(); k++){
                        w.setCharAt(j + k, '-');
                        play.set(j, new int[]{-1, coords.get(k)[0], coords.get(k)[1]}); // -1 indicates tile is already on the board at the given coordinates
                    }
                    if(wordType) coordinate[1] -= j; // Row word, so shift column over appropriate amount. This sets beginning coordinate.
                    else coordinate[0] -= j; // Column word, so shift row over appropriate amount. This sets beginning coordinate.

                    if(coordinate[0] < 0 || coordinate[1] < 0){
                        return null; // Make sure not out of bounds.
                    }
                    break;
                }
            }
        }

        int[] blanks = findBlanks(); // User can only have max 2 blank tiles (there are only 2 in the whole game).
        for (int i = 0; i < TILE_HOLDER_SIZE; i++) {
            int[] tempCoord = new int[]{coordinate[0], coordinate[1]};
            for (int j = 0; j < word.length(); j++) {
                if(tempCoord[0] > 14 || tempCoord[1] > 14) return null; // Make sure not out of bounds.
                if(getTile(i) != null){
                    if (getTile(i).getChar().charAt(0) == word.charAt(j)) {
                        w.setCharAt(j, '-');
                        play.set(j, new int[]{i, tempCoord[0], tempCoord[1]});
                        break;
                    }
                }
                if(wordType) tempCoord[1] += 1;
                else tempCoord[0] += 1;
            }
        }

        for (int i = 0; i < word.length(); i++) {
            if (w.charAt(i) != '-') {
                if (blanks[0] != 0) {
                    play.set(i, new int[]{(int)word.charAt(i), coordinate[0], coordinate[1]});
                    blanks[0] -= 1;
                } else {
                    return null;
                }
            }
        }
        for(int[] o : play){ if(o.length == 0) return null; }
        return play;
    }

    public int[] findBlanks(){
        int[] blanks = {0, -1, -1}; // Initially haven't found any blanks. 0 found, index -1, index -1
        for(int i = 0; i < TILE_HOLDER_SIZE; i++){
            if(getTile(i)!=null){
                if(getTile(i).getChar().charAt(0) == ' '){
                    blanks[0] += 1;
                    for(int k = 1; k < blanks.length; k++) if(blanks[k] == -1) blanks[k] = i;
                }

            }
        }
        return blanks;
    }
}