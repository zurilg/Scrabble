import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
                playCoordinates.clear();
                if(score == 0) model.invalidTurn();
                else model.validTurn(score);
                //playCoordinates.clear();
                break;
            case "R":
                break;
            case "S":
                playCoordinates.clear();
                model.skipTurn();
                break;
        }
    }

    public void clearPlayCoordinates(){
        playCoordinates.clear();
    }
}
