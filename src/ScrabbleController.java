import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Scrabble Controller class.
 * This class acts as the controller in Scrabbles model-view-controller architecture. It is responsible
 * for handling the interactions between frame and the model.
 * It listens for user actions and processes them as commands for the model to execute.
 * Assists in the decision-making processing and execution of AI player turns.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-24-2024
 */
public class ScrabbleController implements ActionListener {
    ScrabbleModel model;
    HashSet<int []> playCoordinates;
    /**
     * Constructor method for ScrabbleController.
     * Initializes an instance of ScrabbleModel and an ArrayList of the coordinates of the tiles played
     * during the current turn.
     * @param model The model object created by the model view frame.
     */
    public ScrabbleController(ScrabbleModel model){
        this.model = model;
        playCoordinates = new HashSet<>();
    }
    /**
     * Processes an event triggered by the view and delegates the command to the appropriate
     * ScrabbleModel method.
     * Initiates AI play following human play.
     * @param e The event to be processed.
     */
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
    /**
     * Clears play coordinates.
     */
    public void clearPlayCoordinates(){
        playCoordinates.clear();
    }
    /**
     * Determines and executes AI player turns.
     */
    public void playAI(){
        // Available plays according to AI player
        ArrayList<ArrayList<int[]>> plays = ((PlayerAI) model.getCurrentPlayer()).getValidPlays(model.getBoard(), model.getDictionary());
        // Points per play
        ArrayList<Integer> playScores = new ArrayList<>();

        // Go through each play, record the score of each play.
        for(ArrayList<int[]> play : plays){
            int[] blanks = ((PlayerAI) model.getCurrentPlayer()).findBlanks();
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
            model.skipTurn();
            return;
        }
        if(playScores.getFirst() == 0) {
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
    /**
     * Sorts play scores in descending order.
     *
     * @param scores Unsorted array list of play scores.
     * @return Sorted array list of play scores.
     */
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
