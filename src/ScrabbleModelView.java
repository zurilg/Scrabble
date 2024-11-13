/**
 * The ScrabbleModelView interface
 * Designed for the view components of the Scrabble game.
 * Defines methods that allow the view to respond to changes in the model and update
 * the GUI representation of the game board and the placement of tiles.
 *
 * The interface includes methods for handling tile placements and updating the game board
 * based on the actions in the model.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-12-2024
 */
public interface ScrabbleModelView {
    /**
     * Method to handle the players tile placement
     * @param e The scrabble event that requires action
     */
    public void handleLetterPlacement(ScrabbleEvent e);
    /**
     * Method to update the board after a players turn.
     * @param e The scrabble event that requires action
     */
    public void updateBoard(ScrabbleEvent e);
}