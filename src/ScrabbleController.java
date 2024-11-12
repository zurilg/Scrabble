import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Scrabble Controller class.
 *
 * This class acts as the controller in Scrabbles model-view-controller architecture. It is responsible
 * for handling the interactions between frame and the model.
 *
 * It listens to user actions and processes them as commands for the model to execute.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-12-2024
 */
public class ScrabbleController implements ActionListener{
    ScrabbleModel model;
    ArrayList<int []> playCoordinates;

    /**
     * Constructor method for ScrabbleController.
     * Initializes an instance of ScrabbleModel and an ArrayList of the coordinates of the tiles played
     * during the current turn.
     * @param model The model object created by the model view frame.
     */
    public ScrabbleController(ScrabbleModel model){
        this.model = model; // Share the model with the view frame
        playCoordinates = new ArrayList<>(); // Store the coordinates played to share with model
    }

    /**
     * Processes an event triggered by the view and delegates the command to the appropriate
     * ScrabbleModel method.
     * @param e the event to be processed
     */
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
                playCoordinates.clear();
                break;
            default:
                break; // Temporary
        }
    }
}
