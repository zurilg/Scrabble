/**
 * Scrabble's main class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 10-22-2024
 */

import javax.swing.*;
import java.awt.*;

public class ScrabbleViewFrame extends JFrame{
    public ScrabbleViewFrame(){
        super("Scrabble");
        this.setLayout(new BorderLayout());

        this.setVisible(true);
    }
    /**
     * Main method of the Scrabble class. Instantiates the controller and starts the game.
     *
     * @param args Configuration arguments.
     */
    public static void main(String[] args) {
        // Initialize controller then start a new game.
        new ScrabbleViewFrame();
        ScrabbleController cont = new ScrabbleController();
        cont.startGame();
    }
}