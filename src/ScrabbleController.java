import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
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
                if(model.checkAI()) playAI();
                break;
            case "R":
                break;
            case "S":
                playCoordinates.clear();
                model.skipTurn();
                if(model.checkAI()) playAI();
                break;
        }
    }

    public void clearPlayCoordinates(){
        playCoordinates.clear();
    }

    public void playAI(){
        // Available plays according to AI player
        ArrayList<ArrayList<int[]>> plays = ((PlayerAI) model.getCurrentPlayer()).getValidPlays(model.getBoard(), model.getDictionary());

        System.out.println("\n\nNUMBER OF AI PLAYS " + plays.size());
        // Points per play
        ArrayList<Integer> playScores = new ArrayList<>();
        // Go through each play, pla
        for(ArrayList<int[]> play : plays){
            int[] blanks = ((PlayerAI) model.getCurrentPlayer()).findBlanks();
            for(int[] playInfo : play){
                if(playInfo[0] >= 65){
                    model.getCurrentPlayer().getTile(blanks[blanks[0]]).setChar(String.valueOf((char)playInfo[0]));
                    playInfo[0] = blanks[blanks[0]];
                    blanks[0] -= 1;
                }
                if(playInfo[0] != -1){
                    model.handleTileSelection(playInfo[0]);
                    model.handleBoardPlacement(playInfo[1], playInfo[2]);
                    playCoordinates.add(new int[]{playInfo[1], playInfo[2]});
                }
            }
            int playScore = model.validateAndScoreBoard(playCoordinates);
            if(playScore != 0 && !playCoordinates.isEmpty()){
                System.out.println(String.format("PLAY SCORE: %d", playScore));
                System.out.println("\nCOORDINATES PLAYED:");
                for(int[] c : playCoordinates) System.out.printf("%d %d, ", c[0], c[1]);
            }
            playScores.add(model.validateAndScoreBoard(playCoordinates));
            model.invalidTurn();
            playCoordinates.clear();
        }

        ArrayList<Integer> sortedIndexes = sortScores(playScores);
        boolean played = false;
        System.out.println("INDEX SIZE" + sortedIndexes.size());
        for(int index : sortedIndexes){
            System.out.println("FINAL AI PLAY ATTEMPT");
            int[] blanks = ((PlayerAI) model.getCurrentPlayer()).findBlanks();
            for(int[] playInfo : plays.get(index)){
                if(playInfo.length > 0){
                    if(playInfo[0] >= 65){
                        model.getCurrentPlayer().getTile(blanks[blanks[0]]).setChar(String.valueOf((char)playInfo[0]));
                        blanks[0] -= 1;
                    }
                    if(playInfo[0] != -1){
                        model.handleTileSelection(playInfo[0]);
                        model.handleBoardPlacement(playInfo[1], playInfo[2]);
                        playCoordinates.add(new int[]{playInfo[1], playInfo[2]});
                    }
                }
            }
            System.out.println("\nCOORDINATES PLAYED:");
            for(int[] c : playCoordinates) System.out.printf("%d %d, ", c[0], c[1]);
            int turnScore = model.validateAndScoreBoard(playCoordinates);
            System.out.println("AI TURN SCORE " + turnScore);

            if(turnScore > 0){
                model.validTurn(turnScore);
                played = true;
                break;
            }
            else model.invalidTurn();
        }

        if(!played) model.skipTurn();
        if(model.checkAI()) playAI();
    }

    private ArrayList<Integer> sortScores(ArrayList<Integer> scores){
        ArrayList<Integer> sortedIndexes = new ArrayList<>();
        for(int i = 0; i < scores.size(); i++) sortedIndexes.add(i);

        boolean swap;
        for(int i = 0; i < scores.size() - 1; i++){
            swap = false;
            for(int j = 0; j < scores.size()-i-1; j++){
                if(scores.get(j) > scores.get(j+1)){
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
