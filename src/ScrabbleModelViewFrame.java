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
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ScrabbleModelViewFrame extends JFrame implements ScrabbleModelView {
    public ArrayList<ArrayList<JButton>> boardButtons;
    public JPanel boardPanel;
    public ArrayList<JButton> userButtons;
    public JPanel userButtonPanel;
    public JButton submitInput;
    public JPanel userPanel;

    ScrabbleModel model;

    public ScrabbleModelViewFrame(){
        super("Scrabble");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Get num players here
        int numPlayers = 0;
        while(numPlayers < 2 || numPlayers > 4){
            String input = JOptionPane.showInputDialog("Enter the number of players (2-4).");
            try{
                numPlayers = Integer.parseInt(input);
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, "Enter an integer value between 2 and 4.");
            }
        }

        // Initialize model
        this.model = new ScrabbleModel(numPlayers);
        this.model.addScrabbleView(this);

        // Continue frame component initialization
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(15,15 ));

        userButtonPanel = new JPanel();
        userButtonPanel.setLayout(new GridLayout(1,7 ));

        userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(1,2 ));

        submitInput = new JButton(" ");

        boardButtons = new ArrayList<ArrayList<JButton>>(ScrabbleModel.BOARD_SIZE);

        ScrabbleController sc = new ScrabbleController(model);

        for(int r = 0; r < 15; r++){
            ArrayList<JButton> row = new ArrayList<JButton>(ScrabbleModel.BOARD_SIZE);
            for(int c = 0; c<15; c++){
                JButton button = new JButton(" "); // Initialize to empty.
                button.setSize(new Dimension(50,50)); // Set the button size
                button.setForeground(Color.BLACK); // Sets text color to black
                button.setBackground(new Color(0xFFF1B0)); // Sets button color to a nice, light yellow
                button.setActionCommand(String.format("B,%d,%d", r, c)); // Action command says it's a board buttons at index (row, column)
                button.addActionListener(sc);
                row.add(c, button);
                boardPanel.add(button); // Add button to the panel it's associated with.
            }
            boardButtons.add(r, row);
        }
        this.add(boardPanel, BorderLayout.CENTER);

        //change to code
        userButtons = new ArrayList<JButton>(ScrabbleModel.NUM_PLAYER_TILES + 1);
        for(int r = 0; r < ScrabbleModel.NUM_PLAYER_TILES; r++){
            JButton button = new JButton(model.getCurrentPlayer().getTiles().get(r).getChar()); // Initialize to empty.
            button.setPreferredSize(new Dimension(100,83)); // Set the button size
            button.setForeground(Color.BLACK); // Sets text color to black
            button.setBackground(new Color(0xBBAF82)); // Sets button color to a nice, light yellow
            button.setActionCommand(String.format("U,%d", r)); // Action command says it's a user tile at index r in their tile holder
            button.addActionListener(sc);
            userButtons.add(r, button); // Adds the button to the 2D array of buttons called grid.
            userButtonPanel.add(button); // Add button to the panel it's associated with.
        }


        submitInput = new  JButton("Play!");
        submitInput.setPreferredSize(new Dimension(83,83)); // Set the button size
        submitInput.setForeground(Color.BLACK); // Sets text color to black
        submitInput.setBackground(new Color(0x58B087));
        submitInput.setActionCommand("P");
        submitInput.addActionListener(sc);
        userButtons.add(7, submitInput); // Adds the button to the 2D array of buttons called grid.
        userButtonPanel.add(submitInput); // Add button to the panel it's associated with.

        userPanel.add(userButtonPanel);

        this.add(userPanel, BorderLayout.SOUTH);

        this.setSize(750,750);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void handleLetterPlacement(ScrabbleEvent e){
        boardButtons.get(e.getR()).get(e.getC()).setText(userButtons.get(e.getSelectedTile()).getText());
        userButtons.get(e.getSelectedTile()).setText(" ");
        userButtons.get(e.getSelectedTile()).setBackground(new Color(0xB0B0B0));
    }

    @Override
    public void updateBoard(ScrabbleEvent e){
        // Redraw board
        for(int r = 0; r < 15; r++){
            ArrayList<JButton> row = boardButtons.get(r);
            for(int c = 0; c<15; c++)
                row.get(c).setText(e.getBoard().getLetterAtIndex(r, c));
        }

        // Redraw user buttons
        for(int r = 0; r < ScrabbleModel.NUM_PLAYER_TILES; r++){
            if(e.getCurrentPlayer().getTiles().get(r) != null){
                System.out.println("HERE!!!");
                userButtons.get(r).setText(e.getCurrentPlayer().getTiles().get(r).getChar());
                userButtons.get(r).setBackground(new Color(0xBBAF82));
            }
            else{
                System.out.println("Here?");
            }
        }


    }

    /**
     * Main method of the Scrabble class. Instantiates the controller and starts the game.
     *
     * @param args Configuration arguments.
     */
    public static void main(String[] args) {
        // Initialize controller then start a new game.
        new ScrabbleModelViewFrame();
    }
}