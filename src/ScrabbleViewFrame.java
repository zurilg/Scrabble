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
    public JButton[][] boardButtons;
    public JPanel boardPanel;
    public JButton [] userButtons;
    public JPanel userButtonPanel;
    public JButton submitInput;
    public JPanel userPanel;

    public ScrabbleViewFrame(){
        super("Scrabble");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(15,15 ));

        userButtonPanel = new JPanel();
        userButtonPanel.setLayout(new GridLayout(1,7 ));

        userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(1,2 ));

        submitInput = new JButton(" ");

        boardButtons = new JButton[15][15];


        for(int r = 0; r < 15; r++){
            for(int c = 0; c<15; c++){
                JButton button = new JButton(" "); // Initialize to empty.
                button.setSize(new Dimension(50,50)); // Set the button size
                button.setForeground(Color.BLACK); // Sets text color to black
                button.setBackground(new Color(0xFFF1B0)); // Sets button color to a nice, light yellow
                this.boardButtons[r][c] = button; // Adds the button to the 2D array of buttons called grid.
                boardPanel.add(button); // Add button to the panel it's associated with.
            }
        }
        this.add(boardPanel, BorderLayout.CENTER);


        userButtons = new JButton [8];
        for(int r = 0; r < 7; r++){
            JButton button = new JButton(" "); // Initialize to empty.
            button.setPreferredSize(new Dimension(100,83)); // Set the button size
            button.setForeground(Color.BLACK); // Sets text color to black
            button.setBackground(new Color(0xBBAF82)); // Sets button color to a nice, light yellow
            this.userButtons[r] = button; // Adds the button to the 2D array of buttons called grid.
            userButtonPanel.add(button); // Add button to the panel it's associated with.
        }

        submitInput = new  JButton("Place!");
        submitInput.setPreferredSize(new Dimension(83,83)); // Set the button size
        submitInput.setForeground(Color.BLACK); // Sets text color to black
        submitInput.setBackground(new Color(0x58B087));
        this.userButtons[7] = submitInput; // Adds the button to the 2D array of buttons called grid.
        userButtonPanel.add(submitInput); // Add button to the panel it's associated with.


        userPanel.add(userButtonPanel);



        this.add(userPanel, BorderLayout.SOUTH);



        this.setSize(750,750);
        this.setResizable(false);
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