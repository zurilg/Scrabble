import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;

public class ScrabbleController implements ActionListener {
    ScrabbleModel model;
    HashSet<int []> playCoordinates;
    public ScrabbleController(ScrabbleModel model){
        this.model = model;
        playCoordinates = new HashSet<>();
    }
    @Override
    public void actionPerformed(ActionEvent e){
        String[] commandParam = e.getActionCommand().split(",");
        switch (commandParam[0]){
            case "B":
                model.handleBoardPlacement(Integer.parseInt(commandParam[1]), Integer.parseInt(commandParam[2]));
                playCoordinates.add(new int[]{Integer.parseInt(commandParam[1]), Integer.parseInt(commandParam[2])});
                break;
            case "U":
                model.handleTileSelection(Integer.parseInt(commandParam[1]));
                break;
            case "P":
                int score = model.validateAndScoreBoard(playCoordinates);
                if(score == 0) model.invalidTurn();
                else model.validTurn(score);
                playCoordinates.clear();
                while(model.checkAI()) playAI();
                break;
            case "R":
                break;
            case "S":
                playCoordinates.clear();
                model.skipTurn();
                while(model.checkAI()) playAI();
                break;
        }
    }

    public void clearPlayCoordinates(){
        playCoordinates.clear();
    }

    public void playAI(){
        // Available plays according to AI player
        ArrayList<ArrayList<int[]>> plays = ((PlayerAI) model.getCurrentPlayer()).getValidPlays(model.getBoard(), model.getDictionary());
        // Points per play
        ArrayList<Integer> playScores = new ArrayList<>();

        // Go through each play, record the score of each play.
        for(ArrayList<int[]> play : plays){
            int[] blanks = ((PlayerAI) model.getCurrentPlayer()).findBlanks();
            System.out.println("\n--------------------");
            // Get each specific placement. [Tile Index, Row, Column]
            for(int[] playInfo : play){
                int tile = playInfo[0];
                if(tile >= 65){
                    if(model.getCurrentPlayer().getTile(blanks[blanks[0]]) != null){
                        model.getCurrentPlayer().getTile(blanks[blanks[0]]).setChar(String.valueOf((char)tile));
                        tile = blanks[blanks[0]];
                        blanks[0] -= 1;
                    }
                }
                if(tile != -1){
                    System.out.printf("%s, %d, %d  ||  ", model.getCurrentPlayer().getTile(tile).getChar(), playInfo[1], playInfo[2]);
                    model.handleTileSelection(tile);
                    model.handleBoardPlacement(playInfo[1], playInfo[2]);
                    playCoordinates.add(new int[]{playInfo[1], playInfo[2]});
                }
            }
            int playScore = model.validateAndScoreBoard(playCoordinates);
            playScores.add(playScore);
            model.invalidTurn();
            playCoordinates.clear();
        }
        // Sort the scores into ascending. Need the original indexes, so we sort the original indexes with the scores.
        ArrayList<Integer> sortedIndexes = sortScores(playScores);

        if(playScores.isEmpty()){
            System.out.println("COUNT NOT FIND ANY PLAYS AT ALL");
            model.skipTurn();
            return;
        }
        if(playScores.getFirst() == 0) {
            System.out.println("ALL PLAYS ENDED UP BEING 0");
            model.skipTurn();
            return;
        }
        // AI Player hasn't played an actual turn yet
        boolean played = false;
        // Go from the highest scoring index to the lowest and try to play each one.
        for(int index : sortedIndexes){
            int[] blanks = ((PlayerAI) model.getCurrentPlayer()).findBlanks();
            for(int[] playInfo : plays.get(index)){
                if (playInfo[0] >= 65) {
                    model.getCurrentPlayer().getTile(blanks[blanks[0]]).setChar(String.valueOf((char) playInfo[0]));
                    playInfo[0] = blanks[blanks[0]];
                    blanks[0] -= 1;
                }
                if(playInfo[0] != -1){
                    System.out.println(String.format("FINAL PLAY: %s, %d, %d", model.getCurrentPlayer().getTile(playInfo[0]).getChar(), playInfo[1], playInfo[2]));
                    model.handleTileSelection(playInfo[0]);
                    model.handleBoardPlacement(playInfo[1], playInfo[2]);
                    playCoordinates.add(new int[]{playInfo[1], playInfo[2]});
                }
            }
            int turnScore = model.validateAndScoreBoard(playCoordinates);

            if(turnScore > 0){
                model.validTurn(turnScore);
                played = true;
                break;
            }
            else model.invalidTurn();
        }

        if(!played) model.skipTurn();
    }

    private ArrayList<Integer> sortScores(ArrayList<Integer> scores){
        ArrayList<Integer> sortedIndexes = new ArrayList<>();
        for(int i = 0; i < scores.size(); i++) sortedIndexes.add(i);

        boolean swap;
        for(int i = 0; i < scores.size() - 1; i++){
            swap = false;
            for(int j = 0; j < scores.size()-i-1; j++){
                if(scores.get(j) < scores.get(j+1)){
                    int temp = scores.get(j+1);
                    scores.set(j+1, scores.get(j));
                    scores.set(j, temp);

                    temp = sortedIndexes.get(j+1);
                    sortedIndexes.set(j+1, sortedIndexes.get(j));
                    sortedIndexes.set(j, temp);
                    swap = true;
                }
            }
            if(!swap) break;
        }
        return sortedIndexes;
    }
}
