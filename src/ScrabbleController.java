import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Scrabble Controller class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-10-2024
 */
public class ScrabbleController implements ActionListener{
    ScrabbleModel model;
    ArrayList<int []> playCoordinates;

    /**
     * Scrabble controller class constructor.
     *
     * @param model The model object created by the model view frame.
     */
    public ScrabbleController(ScrabbleModel model){
        this.model = model; // Share the model with the view frame
        playCoordinates = new ArrayList<>(); // Store the coordinates played to share with model
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
                playCoordinates.clear();
                break;
            default:
                break; // Temporary
        }
    }
}
