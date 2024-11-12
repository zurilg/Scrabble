/**
 * Scrabble Controller class
 *
 * This class acts as the controller in Scrabbles model-view-controller architecture. It is responsible
 * for handling the interactions between frame and the model.
 *
 * It listens to user actions and processes them as commands for the model to execute.
 *
 * @author Zuri Lane-Griffore
 * @author Mohammad Ahmadi
 * @author Abdul Aziz Al-Sibakhi
 * @author Redah Eliwa
 *
 * @version 11-11-2024
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class ScrabbleController implements ActionListener{
    ScrabbleModel model;

    //coordinates of tiles played during current turn.
    ArrayList<int []> playCoordinates;

    /**
     * Constructor method for ScrabbleController.
     * Initializes an instance of ScrabbleModel and an ArrayList of the coordinates of the tiles played
     * during the current turn.
     * @param model An instance of the ScrabbleModel to handle game logic
     */
    public ScrabbleController(ScrabbleModel model){
        this.model = model;
        playCoordinates = new ArrayList<>();
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
                playCoordinates.clear();
                break;
            case "S":
                model.skipTurn();
                playCoordinates.clear();
                break;
            default:
                System.out.println("ERROR!"); // TODO: Remove. Temporary.
        }
    }
}