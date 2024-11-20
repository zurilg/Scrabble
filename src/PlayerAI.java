import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class PlayerAI extends Player{
    public PlayerAI(String name){
        super(name);
    }

    public void determineMove(ScrabbleModel model){
        if(model.getBoard().getSqAtIndex(7,7).getTile() == null){
            int[] tileOrder = firstPlayerAI();
            if(tileOrder == null) model.skipTurn();
            return;
        }
        System.out.println("-------------------------- AI PLAY --------------------------");
        // Find all words (both single letters and whole words). All words on board should be legal
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
                if(model.getBoard().getSqAtIndex(r,c).getTile() != null){
                    rowWords.append(model.getBoard().getSqAtIndex(r,c).getTile().getChar());
                    rowCoords.add(new int[]{r, c});
                }
                else {
                    if(!rowCoords.isEmpty()){
                        rowWordCoordinates.add((ArrayList<int[]>) rowCoords.clone());
                        rowCoords.clear();
                        end = true;
                    }
                    if(end){
                        rowWords.append(",");
                        end = false;
                    }
                }
                // Get column words
                if(model.getBoard().getSqAtIndex(c,r).getTile() != null){
                    colWords.append(model.getBoard().getSqAtIndex(c,r).getTile().getChar());
                    colCoords.add(new int[]{r, c});
                }
                else {
                    if(!colCoords.isEmpty()){
                        colWordCoordinates.add((ArrayList<int[]>) colCoords.clone());
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

        ArrayList<ArrayList<String>> possibleRowPlays = new ArrayList<>();
        ArrayList<ArrayList<String>> possibleColPlays = new ArrayList<>();
        // Convert rowWords and colWords into arrays. Find possible plays using the dictionary.
        String[] rw = rowWords.toString().split(",");
        for (String s : rw) {
            ArrayList<String> possiblePlays = new ArrayList<>();  // Keeps track of all possible plays for rw[i]
            for (String word : model.getDictionary()) {
                if (word.contains(s) && !word.equals(s) && (word.length() <= TILE_HOLDER_SIZE + s.length())) {
                    possiblePlays.add(word);
                }
            }
            // If rw[i] has no possibilities, add null to possible row plays. Otherwise, add possibilities.
            if (possiblePlays.isEmpty()) possibleRowPlays.add(null);
            else possibleRowPlays.add(possiblePlays);
        }

        String[] cw = colWords.toString().split(",");
        for (String s : cw) {
            ArrayList<String> possiblePlays = new ArrayList<>();  // Keeps track of all possible plays for cw[i]
            for (String word : model.getDictionary()) {
                if (word.contains(s) && !word.equals(s) && (word.length() <= TILE_HOLDER_SIZE + s.length())) {
                    possiblePlays.add(word);
                }
            }
            // If cw[i] has no possibilities, add null to possible row plays. Otherwise, add possibilities.
            if (possiblePlays.isEmpty()) possibleColPlays.add(null);
            else possibleColPlays.add(possiblePlays);
        }


        // Now we have to check if we have the resources to cover possible plays
        ArrayList<ArrayList<int[]>> playOrdersRow = new ArrayList<>();
        for(int i = 0; i < rw.length; i++){
            ArrayList<int[]> wordPlays = new ArrayList<>();
            if(possibleRowPlays.get(i) != null){
                for(String word : possibleRowPlays.get(i)){
                    //System.out.println(word); // TODO: print row words
                    if(haveResources(word, rw[i]) == null) wordPlays.add(null);
                    else wordPlays.add(haveResources(word, rw[i]).clone());
                }
            }
            playOrdersRow.add(wordPlays);
        }

        ArrayList<ArrayList<int[]>> playOrdersCol = new ArrayList<>();
        for(int i = 0; i < cw.length; i++){
            ArrayList<int[]> wordPlays = new ArrayList<>();
            if(possibleColPlays.get(i) != null){
                for(String word : possibleColPlays.get(i)){
                    //System.out.println(word); // TODO: print col words
                    int[] playOrder = haveResources(word, cw[i]);
                    wordPlays.add(playOrder);
                }
            }
            playOrdersCol.add(wordPlays);
        }



        // Now we have to try to play each play we came up with and see which one gives max num of points (if any)
        ArrayList<ArrayList<Integer>> playScoresRow = new ArrayList<>();
        for(int i = 0; i < playOrdersRow.size(); i++){
            ArrayList<Integer> scores = new ArrayList<>();
            for(int j = 0; j < playOrdersRow.get(i).size(); j++){
                if(playOrdersRow.get(i).get(j) != null){
                    // Find shift for top tile
                    int topShift = 0;
                    for(int k : playOrdersRow.get(i).get(j)){
                        if(k != -1) topShift -= 1;
                        else break;
                    }

                    // Get top coordinate
                    int[] beginningCord = rowWordCoordinates.get(i).getFirst().clone();
                    beginningCord[1] += topShift; // apply shift to column (since row word)

                    // Play, get points, reset.
                    HashSet<int[]> playCoordinates = new HashSet<>();
                    for(int l = 0; l < playOrdersRow.get(i).get(j).length; l++){
                        if(playOrdersRow.get(i).get(j)[l] != -1){
                            if(getTile(playOrdersRow.get(i).get(j)[l]).getChar().charAt(0) == ' '){
                                getTile(playOrdersRow.get(i).get(j)[l]).setChar(String.valueOf(possibleRowPlays.get(i).get(j).charAt(l)));
                            }
                            model.handleTileSelection(playOrdersRow.get(i).get(j)[l]);
                            //System.out.println(String.format("PLACING TILE %d %d", beginningCord[0], beginningCord[1]));
                            if((beginningCord[0] >= 0 && beginningCord[0] < Board.BOARD_SIZE) && (beginningCord[1] >= 0 && beginningCord[1] < Board.BOARD_SIZE)) {
                                model.handleBoardPlacement(beginningCord[0], beginningCord[1]);
                            }

                            playCoordinates.add(beginningCord);
                        }
                        beginningCord[1] += 1; // Increase coordinate whether tile was already on board or not
                    }

                    int score = model.validateAndScoreBoard(playCoordinates);
                    model.invalidTurn();
                    scores.add(score);
                    //System.out.println("SCORE: " + score);
                }
                else{ scores.add(0); }
            }
            playScoresRow.add(scores);
        }

        ArrayList<ArrayList<Integer>> playScoresCol = new ArrayList<>();
        // Now we have to try to play each play we came up with and see which one gives max num of points (if any)
        for(int i = 0; i < playOrdersCol.size(); i++){
            ArrayList<Integer> scores = new ArrayList<>();
            for(int j = 0; j < playOrdersCol.get(i).size(); j++){
                //System.out.println(String.format("I%d J%d", i, j));
                if(playOrdersCol.get(i).get(j) != null){
                    // Find shift for top tile
                    int topShift = 0;
                    for(int k : playOrdersCol.get(i).get(j)){
                        if(k != -1) topShift -= 1;
                        else break;
                    }

                    // Get top coordinate
                    int[] beginningCord = colWordCoordinates.get(i).getFirst().clone();
                    beginningCord[0] += topShift; // apply shift to row (since column word)

                    //System.out.println("TRYING TO PLAY: "+possibleColPlays.get(i).get(j));
                    // Play, get points, reset.
                    HashSet<int[]> playCoordinates = new HashSet<>();
                    for(int l = 0; l < playOrdersCol.get(i).get(j).length; l++){
                        if(playOrdersCol.get(i).get(j)[l] != -1){
                            if(getTile(playOrdersCol.get(i).get(j)[l]).getChar().charAt(0) == ' '){
                                getTile(playOrdersCol.get(i).get(j)[l]).setChar(String.valueOf(possibleColPlays.get(i).get(j).charAt(l)));
                            }
                            model.handleTileSelection(playOrdersCol.get(i).get(j)[l]);
                            //System.out.println(String.format("PLACING TILE %d %d", beginningCord[0], beginningCord[1]));
                            if((beginningCord[0] >= 0 && beginningCord[0] < Board.BOARD_SIZE) && (beginningCord[1] >= 0 && beginningCord[1] < Board.BOARD_SIZE)) {
                                model.handleBoardPlacement(beginningCord[0], beginningCord[1]);
                            }
                            playCoordinates.add(beginningCord);
                        }
                        beginningCord[0] += 1; // Increase coordinate whether tile was already on board or not
                    }

                    int score = model.validateAndScoreBoard(playCoordinates);
                    model.invalidTurn();
                    scores.add(score);
                    //System.out.println("SCORE: % " + score);
                }
                else{ scores.add(0); }
            }
            playScoresCol.add(scores);
        }

        int [] maxRow = {0, 0, 0};
        for(int i = 0; i < playScoresRow.size(); i++){
            for(int j = 0; j < playScoresRow.get(i).size(); j++){
                if(playScoresRow.get(i).get(j) > maxRow[0]) maxRow = new int[]{playScoresRow.get(i).get(j), i , j};
            }
        }

        int [] maxCol = {0, 0, 0};
        for(int i = 0; i < playScoresCol.size(); i++){
            for(int j = 0; j < playScoresCol.get(i).size(); j++){
                if(playScoresCol.get(i).get(j) > maxCol[0]) maxCol = new int[]{playScoresCol.get(i).get(j), i , j};
            }
        }

        // Play max column
        System.out.printf("MAX %d %d\n", maxRow[0], maxCol[0]);
        System.out.printf("MAX ROW %d %d\n", maxRow[1], maxRow[2]);
        System.out.printf("MAX COL %d %d\n", maxCol[1], maxCol[2]);
        if(maxCol[0] > maxRow[0]){
            ArrayList<int[]> colCoords = colWordCoordinates.get(maxCol[1]);
            // Find amount to shift row number by since col word
            int topShift = 0;
            for(int k : playOrdersCol.get(maxCol[1]).get(maxCol[2])){
                if(k != -1) topShift -= 1;
                else break;
            }

            if(maxCol[0] == 0) model.skipTurn();
            else{
                // Get top coordinate
                int[] beginningCord = colWordCoordinates.get(maxCol[1]).getFirst().clone();
                beginningCord[0] += topShift; // apply shift to row (since column word)

                //System.out.println("TRYING TO PLAY: "+possibleColPlays.get(i).get(j));
                // Play, get points, reset.
                HashSet<int[]> playCoordinates = new HashSet<>();
                for(int l = 0; l < playOrdersCol.get(maxCol[1]).get(maxCol[2]).length; l++){
                    if(playOrdersCol.get(maxCol[1]).get(maxCol[2])[l] != -1){
                        if(getTile(playOrdersCol.get(maxCol[1]).get(maxCol[2])[l]).getChar().charAt(0) == ' '){
                            getTile(playOrdersCol.get(maxCol[1]).get(maxCol[2])[l]).setChar(String.valueOf(possibleColPlays.get(maxCol[1]).get(maxCol[2]).charAt(l)));
                        }
                        model.handleTileSelection(playOrdersCol.get(maxCol[1]).get(maxCol[2])[l]);
                        //System.out.println(String.format("PLACING TILE %d %d", beginningCord[0], beginningCord[1]));
                        if((beginningCord[0] < 15 && beginningCord[0] >= 0) && (beginningCord[1] < 15 && beginningCord[1] >= 0)){
                            model.handleBoardPlacement(beginningCord[0], beginningCord[1]);
                            playCoordinates.add(beginningCord);
                        }
                    }
                    beginningCord[0] += 1; // Increase coordinate whether tile was already on board or not
                }

                int score = model.validateAndScoreBoard(playCoordinates);
                if(score != 0) model.validTurn(score);
            }
        }
        // Play max row (default)
        else{
            ArrayList<int[]> rowCoords = rowWordCoordinates.get(maxRow[1]);
            // Find amount to shift col number by since row word
            int topShift = 0;
            for(int k : playOrdersRow.get(maxRow[1]).get(maxRow[2])){
                if(k != -1) topShift -= 1;
                else break;
            }

            // Get top coordinate
            int[] beginningCord = rowWordCoordinates.get(maxRow[1]).getFirst().clone();
            beginningCord[1] += topShift; // apply shift to col (since row word)

            // Play, get points, reset.
            HashSet<int[]> playCoordinates = new HashSet<>();
            for(int l = 0; l < playOrdersRow.get(maxRow[1]).get(maxRow[2]).length; l++){
                if(playOrdersRow.get(maxRow[1]).get(maxRow[2])[l] != -1){
                    if(getTile(playOrdersRow.get(maxRow[1]).get(maxRow[2])[l]).getChar().charAt(0) == ' '){
                        getTile(playOrdersRow.get(maxRow[1]).get(maxRow[2])[l]).setChar(String.valueOf(possibleRowPlays.get(maxRow[1]).get(maxRow[2]).charAt(l)));
                    }
                    model.handleTileSelection(playOrdersRow.get(maxRow[1]).get(maxRow[2])[l]);
                    //System.out.println(String.format("PLACING TILE %d %d", beginningCord[0], beginningCord[1]));
                    model.handleBoardPlacement(beginningCord[0], beginningCord[1]);
                    playCoordinates.add(beginningCord);
                }
                beginningCord[1] += 1; // Increase coordinate whether tile was already on board or not
            }

            int score = model.validateAndScoreBoard(playCoordinates);

            model.validTurn(score);


        }
    }

    private int[] haveResources(String word, String wordOnBoard){
        int[] playOrder = new int[word.length()];

        StringBuilder w = new StringBuilder(word);
        StringBuilder wob = new StringBuilder(wordOnBoard);
        for(int i = 0; i < wordOnBoard.length(); i++){
            for(int j = 0; j < word.length(); j++){
                if(wordOnBoard.charAt(i) == word.charAt(j)){
                    w.setCharAt(j, '-');
                    playOrder[j] = -1;
                    break;
                }
            }
        }
        //System.out.println(w.toString());
        int[] blanks = findBlanks(); // User can only have max 2 blank tiles (there are only 2 in the whole game).
        for(int i = 0; i < TILE_HOLDER_SIZE; i++){
            for(int j = 0; j < word.length(); j++){
                if(getTile(i).getChar().charAt(0) == word.charAt(j)){
                    w.setCharAt(j, '-');
                    playOrder[j] = i;
                    break;
                }
            }
        }
        //System.out.println(w.toString());
        for(int i = 0; i < word.length(); i++){
            if(w.charAt(i) != '-'){
                if(blanks[0] != 0){
                    playOrder[i] = blanks[blanks[0]];
                    blanks[0] -= 1;
                }
                else{
                    return null;
                }
            }
        }
        //System.out.println("FOUND A VALID ONE");
        //System.out.println(Arrays.toString(playOrder));
        return playOrder;
    }

    private int[] findBlanks(){
        int[] blanks = {0, -1, -1}; // Initially haven't found any blanks. 0 found, index -1, index -1
        for(int i = 0; i < TILE_HOLDER_SIZE; i++){
            if(getTile(i).getChar()!=null){
                if(getTile(i).getChar().charAt(0) == ' '){
                    blanks[0] += 1;
                    for(int k = 1; k < blanks.length; k++) if(blanks[k] == -1) blanks[k] = i;
                }

            }

        }
        return blanks;
    }

    private int[] firstPlayerAI(){
        return null;
    }
}
