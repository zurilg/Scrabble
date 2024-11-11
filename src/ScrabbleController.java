import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ScrabbleController implements ActionListener{
    ScrabbleModel model;
    ArrayList<int []> playCoordinates;
    public ScrabbleController(ScrabbleModel model){
        this.model = model;
        playCoordinates = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String[] commandParam = e.getActionCommand().split(",");
        switch(commandParam[0]){
            case "B":
                model.handleBoardSquare(Integer.parseInt(commandParam[1]), Integer.parseInt(commandParam[2]));
                playCoordinates.add(new int[]{Integer.parseInt(commandParam[1]), Integer.parseInt(commandParam[2])});
                break;
            case "U":
                model.handleUserTile(Integer.parseInt(commandParam[1]));
                break;
            case "P":
                model.validateAndScoreBoard(playCoordinates);
                break;
            case "S":
                model.skipTurn();
                break;
            default:
                System.out.println("ERROR!"); // TODO: Remove. Temporary.
        }
    }
}
