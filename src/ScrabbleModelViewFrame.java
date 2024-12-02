import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import static java.lang.System.exit;
/**
 * ScrabbleModelViewFrame class and main class of the Scrabble game.
 * ScrabbleModelViewFrame displays a GUI-based Scrabble game.
 * It acts as the frame in Scrabbles model-view-controller architecture. It handles player actions
 * including playing and skipping a turn. Updates the display accordingly after each turn.
 * Uses a JFrame to display the board, players, and the tiles.
 * The class implements the ScrabbleModelView interface to handle events related to letter placement
 * and board updates.
 * Also includes functionality for initializing players, managing game controls, and updating the game
 * state on the UI.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-24-2024
 */
public class ScrabbleModelViewFrame extends JFrame implements ScrabbleModelView {
    private ScrabbleModel model;
    private final ScrabbleController sc;
    private int numPlayers;

    // Attributes of the board display (center panel)
    private JButton[][] boardButtons; // 15 x 15 grid of buttons for the board.
    private JLabel[][][] buttonLabels; // 15 x 15 x 3 (Board Row x Board Column x Board Button Labels)
    private JPanel boardPanel; // Panel with a grid layout to hold the board buttons.

    // Attributes of user panel (south panel)
    private JButton[] tileHolder;
    private JLabel[][] tileLabels; // 7 x 2 labels. Tile character and points
    private JPanel userPanel; // Panel with 1 x 10 grid layout to hold all user play option buttons.

    // Attributes of the players names, points, and current player display (east panel)
    private JLabel[][] playerInfo; // N Player x 2
    private JPanel playersPanel;

    // Attributes of the menu bars for loading/saving games and for iterating through turns
    private JMenuBar menuBar;
    private JMenu game, buddies;
    private JMenuItem save, load, add, remove;
    /**
     * Constructor method for ScrabbleModelViewFrame()
     * Initializes the games UI and an instance of ScrabbleModel.
     */
    public ScrabbleModelViewFrame(){
        // Initialize basic frame aspects
        super("Scrabble"); // Call super class (JFrame)
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setIconImage((new ImageIcon("./GameAssets/Pictures/S_Logo.png")).getImage()); // Add icon / game logo
        this.setResizable(false);
        this.setSize(new Dimension(925, 825));
        this.setLocationRelativeTo(null); // Center on screen

        // NEED TO FIGURE OUT IF NEW GAME OR LOADING OLD GAME
        String[] playerGameOptions = {"New Game", "Load Game"};
        int playerGameLoadChoice = JOptionPane.showOptionDialog(null, "Would you like to start a new game or load a previously saved one?", "Scrabble", 0, 2, new ImageIcon("./GameAssets/Pictures/S_Icon.png"), playerGameOptions, playerGameOptions[0]);
        System.out.println(playerGameLoadChoice); //TODO: remove

        // Initialize model
        model = new ScrabbleModel();
        model.addScrabbleView(this);
        switch(playerGameLoadChoice){
            // Player decides to start a new game or has to start new game. No old games to load.
            case 0:
                Object[] options = {"Traditional", "New Wave", "No Bonus"};
                String bonusSelection = JOptionPane.showInputDialog(null, "Choose", "Menu", JOptionPane.PLAIN_MESSAGE, null, options, options[0]).toString();
                if(!bonusSelection.equals(options[2])) model.getBoard().initBoard(bonusSelection.replace(" ", ""));
                // Initialize players
                initPlayers();
                break;
            case 1:

                break;

            default:
                System.exit(0);
                break;
        }

        // Initialize controller
        sc = new ScrabbleController(model);

        // Initialize board buttons and add them to their panel. Once that's finished, draw them.
        initBoardButtons();
        drawBoardButtons();

        // Initialize current player button panel
        drawButtonPanel();

        // Initialize players panel
        drawPlayerInfo();

        // Finish frame initialization. Add content to the frame. Set as visible
        this.add(boardPanel, BorderLayout.CENTER);
        this.add(userPanel, BorderLayout.SOUTH);
        this.add(playersPanel, BorderLayout.EAST);
        this.setVisible(true);

        while(model.checkAI()) sc.playAI();
    }

    public void loadGame(String fileName){ model = load(fileName); }
    public void saveGame(String fileName){
        save(fileName, model);
    }
    static ScrabbleModel load(String fileName){
        fileName = String.format("./GameAssets/SavedGames/%s.bin", fileName);
        try{
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            Object o = is.readObject();
            return (ScrabbleModel) o;
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void save(String fileName, ScrabbleModel m){
        fileName = String.format("./GameAssets/SavedGames/%s.bin", fileName);
        try{
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
            os.writeObject(m);
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes elements of the user Tile Holder button panel.
     */
    private void drawButtonPanel(){
        // Initialize play button
        JButton play = new JButton("Play!");
        play.addActionListener(sc);
        play.setActionCommand("P");
        play.setBackground(new Color(0xB6D38D));
        play.setFocusable(false);
        // Initialize replace button
        JButton replace = new JButton();
        replace.setLayout(new GridLayout(2, 1));
        JLabel r = new JLabel("Replace");
        JLabel t = new JLabel("Tiles");
        r.setBackground(new Color(0xFFC53B));
        t.setBackground(new Color(0xFFC53B));
        r.setHorizontalAlignment(JTextField.CENTER);
        t.setHorizontalAlignment(JTextField.CENTER);
        replace.add(r);
        replace.add(t);
        replace.addActionListener(sc);
        replace.setActionCommand("R");
        replace.setBackground(new Color(0xFFC53B));

        // Initialize skip button
        JButton skip = new JButton("Skip Turn");
        skip.setBackground(new Color(0xE16161));
        skip.addActionListener(sc);
        skip.setActionCommand("S");
        skip.setFocusable(false);
        // Initialize tiles
        tileHolder = new JButton[Player.TILE_HOLDER_SIZE];
        tileLabels = new JLabel[Player.TILE_HOLDER_SIZE][2];
        userPanel = new JPanel(new GridLayout(1, Player.TILE_HOLDER_SIZE + 3));
        for(int i = 0; i < Player.TILE_HOLDER_SIZE; i++){
            tileHolder[i] = new JButton();
            tileHolder[i].setLayout(new GridLayout(3, 1));
            tileHolder[i].addActionListener(sc);
            tileHolder[i].setActionCommand(String.format("U,%d", i));
            tileHolder[i].setBackground(new Color(0xc69f77));
            tileHolder[i].setFocusable(false);

            tileLabels[i][0] = new JLabel(model.getCurrentPlayer().getTile(i).getChar());
            tileLabels[i][1] = new JLabel(String.format("             %d", model.getCurrentPlayer().getTile(i).getValue()));
            tileLabels[i][0].setHorizontalAlignment(JTextField.CENTER);
            tileLabels[i][1].setHorizontalAlignment(JTextField.CENTER);
            tileLabels[i][0].setPreferredSize(new Dimension(23,23));
            tileLabels[i][1].setPreferredSize(new Dimension(23,23));

            tileHolder[i].add(new JLabel(""));
            tileHolder[i].add( tileLabels[i][0]);
            tileHolder[i].add( tileLabels[i][1]);

            userPanel.add(tileHolder[i]);
        }
        userPanel.add(play);
        userPanel.add(replace);
        userPanel.add(skip);
    }
    /**
     * Updates elements of the user Tile Holder button panel.
     */
    private void redrawTileHolder(){
        for(int i = 0; i < Player.TILE_HOLDER_SIZE; i++){
            if(model.getCurrentPlayer().getTile(i) != null){
                tileLabels[i][0].setText(model.getCurrentPlayer().getTile(i).getChar());
                tileLabels[i][1].setText(String.format("             %d", model.getCurrentPlayer().getTile(i).getValue()));
                tileHolder[i].setBackground(new Color(0xc69f77));
                tileHolder[i].setEnabled(true);
            }
            else{
                tileLabels[i][0].setText("");
                tileLabels[i][1].setText("");
                tileHolder[i].setBackground(new Color(0x444343));
                tileHolder[i].setEnabled(false);
            }
        }
    }
    /**
     * Initializes elements of the player info (name, scores, turn) panel.
     */
    private void drawPlayerInfo(){
        playerInfo = new JLabel[numPlayers][2];
        playersPanel = new JPanel();
        playersPanel.setSize(100, 825);
        playersPanel.setLayout(new GridLayout(numPlayers, 1));
        for(int i = 0; i < numPlayers; i++){
            // A JPanel to add the player info to. This JPanel is then added to the playersPanel.
            JPanel playerInfoDisplay = new JPanel();
            playerInfoDisplay.setLayout(new GridLayout(2, 1));
            // Get the name of the player and set the color of their name based on whether they're the current player or not.
            playerInfo[i][0] = new JLabel(model.getPlayers().get(i).getName());
            if(model.getPlayers().get(i) == model.getCurrentPlayer()) playerInfo[i][0].setForeground(new Color(0xFF0000));
            else playerInfo[i][0].setForeground(new Color(0x000000));
            // Get the score of the player as a JLabel
            playerInfo[i][1] = new JLabel(String.format("Score: %d", model.getPlayers().get(i).getScore()));
            // Make visually pleasing
            playerInfo[i][0].setPreferredSize(new Dimension(100, 100));
            playerInfo[i][1].setPreferredSize(new Dimension(100, 100));
            playerInfo[i][0].setHorizontalAlignment(JTextField.CENTER); // Center text
            playerInfo[i][1].setHorizontalAlignment(JTextField.CENTER);
            // Add each JLabel (Player name and points) to base panel
            playerInfoDisplay.add(playerInfo[i][0]);
            playerInfoDisplay.add(playerInfo[i][1]);
            // Alternate base panel colors to easily distinguish between players
            if(i % 2 == 0) playerInfoDisplay.setBackground(new Color(0xADBED5));
            else playerInfoDisplay.setBackground(new Color(0xD0E4FF));
            // Add base panel (a single player) to the playersPanel (the one to be displayed).
            playersPanel.add(playerInfoDisplay);
        }
    }
    /**
     * Updates elements of the player info (name, scores, turn) panel.
     */
    private void redrawPlayerInfo(){
        // Set current player's name to bright color
        playerInfo[model.getPlayerTurn()][0].setForeground(new Color(0xFF0000));
        // Find index of previous player
        int prevTurn;
        if(model.getPlayerTurn() == 0) prevTurn = numPlayers - 1;
        else prevTurn = model.getPlayerTurn() - 1;
        // Update previous player's visuals
        playerInfo[prevTurn][0].setForeground(new Color(0x000000));
        playerInfo[prevTurn][1].setText(String.format("Score: %d", model.getPlayers().get(prevTurn).getScore()));
    }
    /**
     * Initializes elements of the board square button panel.
     */
    private void initBoardButtons(){
        boardButtons = new JButton[Board.BOARD_SIZE][Board.BOARD_SIZE];
        buttonLabels = new JLabel[Board.BOARD_SIZE][Board.BOARD_SIZE][3];
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(Board.BOARD_SIZE, Board.BOARD_SIZE));
        for(int r = 0; r < Board.BOARD_SIZE; r++){
            for(int c = 0; c < Board.BOARD_SIZE; c++){
                boardButtons[r][c] = new JButton();
                boardButtons[r][c].setLayout(new GridLayout(3, 1));
                boardButtons[r][c].setActionCommand(String.format("B,%d,%d", r, c));
                boardButtons[r][c].addActionListener(sc);
                boardButtons[r][c].setFocusable(false);
                for(int i = 0; i < 3; i++){
                    buttonLabels[r][c][i] = new JLabel(" ");
                    buttonLabels[r][c][i].setHorizontalAlignment(JTextField.CENTER);
                    boardButtons[r][c].add(buttonLabels[r][c][i]);
                }
                boardPanel.add(boardButtons[r][c]);
            }
        }
    }
    /**
     * Updates elements of the board square button panel.
     */
    private void drawBoardButtons(){
        for(int r = 0; r < Board.BOARD_SIZE; r ++){
            for(int c = 0; c < Board.BOARD_SIZE; c ++){
                // Draw a tile placed on the board
                if(model.getBoard().getSqAtIndex(r, c).getTile() != null){
                    boardButtons[r][c].setBackground(new Color(0xc69f77));
                    buttonLabels[r][c][0].setText("");
                    buttonLabels[r][c][1].setText(model.getBoard().getSqAtIndex(r,c).getTile().getChar());
                    buttonLabels[r][c][2].setText(String.format("  %d", model.getBoard().getSqAtIndex(r,c).getTile().getValue()));
                }
                // Draw an untouched board square
                else{
                    int ws = model.getBoard().getSqAtIndex(r, c).getWordScore(); // Word score access variable
                    int ls = model.getBoard().getSqAtIndex(r, c).getLetterScore();
                    if(ws > 1){
                        buttonLabels[r][c][0].setText(String.format("%dW", model.getBoard().getSqAtIndex(r, c).getWordScore()));
                        if(ws == 2) boardButtons[r][c].setBackground(new Color(0xDE7E7E));
                        else if(ws == 3) boardButtons[r][c].setBackground(new Color(0xAB2828));
                    }
                    else if(ls > 1){
                        buttonLabels[r][c][0].setText(String.format("%dL", model.getBoard().getSqAtIndex(r, c).getLetterScore()));
                        if(ls == 2) boardButtons[r][c].setBackground(new Color(0x5E83B2));
                        else if(ls == 3) boardButtons[r][c].setBackground(new Color(0x1749BE));
                    }
                    else{
                        buttonLabels[r][c][0].setText(" ");
                        boardButtons[r][c].setBackground(new Color(0xBBAF92));
                    }
                    buttonLabels[r][c][1].setText(" ");
                    buttonLabels[r][c][2].setText(" ");
                }
            }
        }
    }
    /**
     * Gets input from user for number of players and human player names.
     * Provides player info gathered from user to the model.
     */
    private void initPlayers(){
        ArrayList<String> playerNames = new ArrayList<>();
        int numHuman;
        // Get number of players and number of human players
        numPlayers = getPlayerNumInput(ScrabbleModel.MIN_PLAYERS, ScrabbleModel.MAX_PLAYERS, String.format("Enter the number of players (%d-%d).", ScrabbleModel.MIN_PLAYERS, ScrabbleModel.MAX_PLAYERS));
        numHuman = getPlayerNumInput(1, numPlayers, String.format("Enter the number of human players (1-%d).", numPlayers));
        // Get each human player's name
        for(int i = 0; i < numHuman; i++){
            String input = "";
            while(input.isBlank()){
                input = JOptionPane.showInputDialog(String.format("Enter human player %d's name.", i+1));
                if(input == null) System.exit(0); // User pressed cancel.
                else input = input.trim();
                if(input.isBlank()) JOptionPane.showMessageDialog(this, "Can't enter a blank name!");
                for(String name : playerNames) if(name.equalsIgnoreCase(input)){
                    JOptionPane.showMessageDialog(this, "Please enter a unique name!");
                    input = "";
                }
            }
            playerNames.add(input);
        }
        // Initialize players in model using info provided. Number of AI players = numPlayers - numHuman
        model.initPlayers(playerNames, numPlayers - numHuman);
    }
    /**
     * Gets a user for an integer input within a specific range.
     *
     * @param min Minimum number that a user can input.
     * @param max Maximum number that a user can input.
     * @param message The message to display to the user.
     * @return The user's input.
     */
    private int getPlayerNumInput(int min, int max, String message){
        int num = 0;
        // Get number of players
        while(num < min || num > max){
            String input = JOptionPane.showInputDialog(message); // Get user input
            if(input == null) System.exit(0); // User pressed cancel
            try{ num = Integer.parseInt(input); } // User entered something that might be valid. Try to parse as int.
            catch (Exception e){ JOptionPane.showMessageDialog(this, String.format("Enter an integer value between %d and %d.", min, max)); }
        }
        return num;
    }
    /**
     * Invoked when a necessary program file is not present.
     *
     * @param message The error message to display to the user.
     */
    public static void fileReadError(String message){
        JOptionPane.showMessageDialog(null, message, "File Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    /**
     * Prompts and gets user input for a blank tile that has been placed.
     *
     * @return The letter selected by the user to replace the blank.
     */
    public static String getBlankInput(){
        String[] alpha = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        return JOptionPane.showInputDialog(null, "Choose letter to replace blank.", "Set Blank Tile", JOptionPane.PLAIN_MESSAGE, null, alpha, alpha[0]).toString();
    }
    /**
     * Handles the logic behind a player placing a tile on the board.
     *
     * @param e The tile placement event.
     */
    @Override
    public void handleLetterPlacement(ScrabbleEvent.TilePlacement e){
        buttonLabels[e.getR()][e.getC()][0].setText("");
        buttonLabels[e.getR()][e.getC()][1].setText(model.getBoard().getSqAtIndex(e.getR(), e.getC()).getTile().getChar());
        buttonLabels[e.getR()][e.getC()][2].setText(String.format("   %d", model.getBoard().getSqAtIndex(e.getR(), e.getC()).getTile().getValue()));
        boardButtons[e.getR()][e.getC()].setBackground(new Color(0xc69f77));
        tileHolder[e.getSelectedTile()].setText("");
        tileHolder[e.getSelectedTile()].setBackground(new Color(0x444343));
        tileHolder[e.getSelectedTile()].setEnabled(false);
    }
    /**
     * Method used to update the board after a players turn.
     * Ends the game if game status is over. Initiates end of game process and displays game results.
     */
    @Override
    public void updateBoard(){
        if(model.getStatus() == ScrabbleModel.Status.OVER) {
            JOptionPane.showMessageDialog(this, model.gameResults());
            exit(0);
        }
        drawBoardButtons();
        redrawTileHolder();
        redrawPlayerInfo();
        sc.clearPlayCoordinates();
    }
    /**
     * Scrabble's main method.
     * Instantiates a new instance of the model view frame.
     *
     * @param args NOT USED
     */
    public static void main(String[] args) {
        new ScrabbleModelViewFrame();
    }
}
