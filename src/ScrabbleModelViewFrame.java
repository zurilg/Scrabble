/**
 * TODO: Write description. Scrabble's main class...
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-10-2024
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ScrabbleModelViewFrame extends JFrame implements ScrabbleModelView {
    private ArrayList<ArrayList<JButton>> boardButtons;
    private JPanel boardPanel, userButtonPanel, userPanel, players, words;
    private ArrayList<JButton> userButtons;
    private JButton submitInput, skipTurn;
    ArrayList<JLabel> playerInfo;

    private int numPlayers;

    private ScrabbleModel model;

    public ScrabbleModelViewFrame(){
        super("Scrabble"); // Call superclass (JFrame)
        this.setLayout(new BorderLayout()); // Set layout as a border layout.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set default close operation to exit.

        // Get the number of players
        this.numPlayers = 0;
        while(numPlayers < 2 || numPlayers > 4){
            String input = JOptionPane.showInputDialog("Enter the number of players (2-4).");
            try{
                numPlayers = Integer.parseInt(input);
            } catch (Exception e){
                JOptionPane.showMessageDialog(this, "Enter an integer value between 2 and 4.");
            }
        }

        playerInfo = new ArrayList<>(); // Name, points, ...

        // Initialize model. Add view to model.
        this.model = new ScrabbleModel(numPlayers);
        this.model.addScrabbleView(this);


        boardPanel = new JPanel(); // Initialize the board panel. This will store all board buttons.
        boardPanel.setLayout(new GridLayout(15,15 )); // Board is a 15x15 grid layout of buttons

        userButtonPanel = new JPanel();
        userButtonPanel.setLayout(new GridLayout(1,9 ));


        boardButtons = new ArrayList<ArrayList<JButton>>(ScrabbleModel.BOARD_SIZE);

        ScrabbleController sc = new ScrabbleController(model);

        // Create all board buttons
        for(int r = 0; r < 15; r++){
            ArrayList<JButton> row = new ArrayList<JButton>(ScrabbleModel.BOARD_SIZE);
            for(int c = 0; c<15; c++){
                JButton button = new JButton(" "); // Initialize to empty.
                button.setSize(new Dimension(55,55)); // Set the button size
                button.setForeground(Color.BLACK); // Sets text color to black
                button.setBackground(new Color(0xFFF1B0)); // Sets button color to a nice, light yellow
                button.setActionCommand(String.format("B,%d,%d", r, c)); // Action command says it's a board buttons at index (row, column)
                button.addActionListener(sc);
                button.setFocusable(false); // Get rid of annoying box
                row.add(c, button);
                boardPanel.add(button); // Add button to the panel it's associated with.
            }
            boardButtons.add(r, row);
        }
        this.add(boardPanel, BorderLayout.CENTER);

        userButtons = new ArrayList<JButton>(ScrabbleModel.NUM_PLAYER_TILES + 2);
        for(int r = 0; r < ScrabbleModel.NUM_PLAYER_TILES; r++){
            JButton button = new JButton(model.getCurrentPlayer().getTiles().get(r).getChar()); // Initialize to empty.
            button.setPreferredSize(new Dimension(100,83)); // Set the button size
            button.setForeground(Color.BLACK); // Sets text color to black
            button.setBackground(new Color(0xDEC38A)); // Sets button color to a nice, light yellow
            button.setActionCommand(String.format("U,%d", r)); // Action command says it's a user tile at index r in their tile holder
            button.addActionListener(sc);
            button.setFocusable(false); // Get rid of annoying box
            userButtons.add(r, button); // Adds the button to the 2D array of buttons called grid.
            userButtonPanel.add(button); // Add button to the panel it's associated with.
        }

        submitInput = new  JButton("Play!");
        submitInput.setPreferredSize(new Dimension(83,83)); // Set the button size
        submitInput.setBackground(new Color(0x58B087));
        submitInput.setActionCommand("P");
        submitInput.addActionListener(sc);
        submitInput.setFocusable(false);

        skipTurn = new  JButton("Skip Turn");
        skipTurn.setPreferredSize(new Dimension(83,83)); // Set the button size
        skipTurn.setBackground(new Color(0xE16161));
        skipTurn.setActionCommand("S");
        skipTurn.addActionListener(sc);
        skipTurn.setFocusable(false);

        userButtons.add(7, submitInput); // Adds the button to the 2D array of buttons called grid.
        userButtonPanel.add(submitInput); // Add button to the panel it's associated with.

        userButtons.add(8, skipTurn); // Adds the button to the 2D array of buttons called grid.
        userButtonPanel.add(skipTurn); // Add button to the panel it's associated with.


        this.add(userButtonPanel, BorderLayout.SOUTH);

        // Initialize text fields to display player info on the right-hand side
        for(Player p : model.getPlayers()){
            JLabel lb = new JLabel(p.getName());
            lb.setPreferredSize(new Dimension(100, 100));
            lb.setHorizontalAlignment(JTextField.CENTER); // Center text
            lb.setFont(new Font("Arial", Font.BOLD, 15)); // Make player name bold
            playerInfo.add(lb);

            lb = new JLabel(String.format("Points: %d", p.getScore()));
            lb.setPreferredSize(new Dimension(100, 100));
            lb.setHorizontalAlignment(JTextField.CENTER); // center text
            playerInfo.add(lb);
        }
        // Add player info text fields to their corresponding panel
        players = new JPanel(new GridLayout(playerInfo.size(), 1));
        players.setBackground(new Color(0xFAEEDD));
        players.setSize(100, 825);
        for(JLabel j : playerInfo){
            players.add(j);
        }

        this.add(players, BorderLayout.EAST);

        // Finish frame initialization
        this.setSize(925,825); // Define JFrame dimensions according to it's components
        this.setLocationRelativeTo(null); // Center JFrame
        this.setResizable(false); // Don't allow user to resize
        this.setVisible(true); // Show JFrame
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
            if(e.getCurrentPlayer().getTiles().get(r) != null){ // TODO: Temporary???? Useless????
                userButtons.get(r).setText(e.getCurrentPlayer().getTiles().get(r).getChar());
                userButtons.get(r).setBackground(new Color(0xDEC38A));
            }
            else{
                System.out.println("Here?"); // TODO: Temporary.. Should hopefully never print? Remove
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