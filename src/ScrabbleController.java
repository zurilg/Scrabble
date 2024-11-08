import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrabbleController implements ActionListener{
    ScrabbleModel model;
    public ScrabbleController(ScrabbleModel model){
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String[] commandParam = e.getActionCommand().split(",");
        switch(commandParam[0]){
            case "B":
                model.handleBoardSquare(Integer.parseInt(commandParam[1]), Integer.parseInt(commandParam[2]));
                break;
            case "U":
                model.handleUserTile(Integer.parseInt(commandParam[1]));
                break;
            case "P":
                model.validateBoard();
                break;
            default:
                System.out.println("ERROR!"); // TODO: Remove. Temporary.
        }
    }
}
