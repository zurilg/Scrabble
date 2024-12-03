import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private JMenu game, turns;
    private JMenuItem save, end, undo, redo;

    // The gameFile name. Not empty if game was loaded.
    private String gameFile;
    /**
     * Constructor method for ScrabbleModelViewFrame()
     * Initializes the games UI and an instance of ScrabbleModel.
     */
    public ScrabbleModelViewFrame(){
        // Initialize basic frame aspects
        super("Scrabble"); // Call super class (JFrame)
        this.setLayout(new BorderLayout()); // Set frame layout
        // Prompts user to save their game before exiting the application
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int saveGame = JOptionPane.showConfirmDialog(null, "Would you like to save ongoing game before exiting?", "End Game: Save", JOptionPane.YES_NO_OPTION);
                if(saveGame == JOptionPane.YES_OPTION) saveGame();
                exit(0);
            }
        });
        this.setIconImage((new ImageIcon("./GameAssets/Pictures/S_Logo.png")).getImage()); // Add icon / game logo
        this.setResizable(false); // Don't allow user to resize the game.
        this.setSize(new Dimension(925, 825)); // Game window dimensions
        this.setLocationRelativeTo(null); // Center on screen

        // Initialize model
        model = new ScrabbleModel();

        // Must initialize the game. Old game / new game and game options.
        gameFile = "";
        initGame();
        // Initialize controller
        sc = new ScrabbleController(model);
        // Add view
        model.addScrabbleView(this);
        // Initialize board buttons and add them to their panel. Once that's finished, draw them.
        initBoardButtons();
        drawBoardButtons();
        // Initialize current player button panel
        drawButtonPanel();

        // Initialize players panel
        drawPlayerInfo();

        initMenuBar();

        // Finish frame initialization. Add content to the frame. Set as visible
        this.add(boardPanel, BorderLayout.CENTER);
        this.add(userPanel, BorderLayout.SOUTH);
        this.add(playersPanel, BorderLayout.EAST);
        this.setJMenuBar(menuBar);
        this.setVisible(true);

        while(model.checkAI()){
            System.out.println("AI TURN");
            sc.playAI();
        }
        model.setTempState();
    }
    /**
     * Loads a saved game from a file.
     */
    public void loadGame(){
        File path = new File("./GameAssets/SavedGames");
        String[] savedGames = path.list();
        for(int i = 0; i < savedGames.length; i++) savedGames[i] = savedGames[i].replace(".bin", "");
        gameFile = JOptionPane.showInputDialog(null, "Which game would you like to load?", "Saved Games", JOptionPane.PLAIN_MESSAGE, null, savedGames, savedGames[0]).toString();
        model = load(gameFile);
        numPlayers = model.getPlayers().size();
        //for(int r = 0; r < Board.BOARD_SIZE; r++) for(int c = 0; c < Board.BOARD_SIZE; c++) System.out.println(String.format("Squares: %d", model.getBoard().getSqAtIndex(r,c).getLetterScore()));
    }

    /**
     * Saves the game in its current state into a file
     */
    public void saveGame(){
        if(gameFile.isBlank()){
            String fileName = getValidFileName();
            if(fileName == null) return;
            gameFile = fileName;
        }
        save(gameFile, model);
    }

    /**
     * Prompts the user to enter a valid file name for saving the game.
     *
     * @return the file name entered by the user
     */
    private String getValidFileName(){
        // Get current file names. Don't want duplicates.
        File path = new File("./GameAssets/SavedGames");
        String[] savedGames = path.list();
        String fileName = "";
        boolean invalidName = true;
        StringBuilder message = new StringBuilder("Enter name to save game as: ");
        while(invalidName){
            fileName = JOptionPane.showInputDialog(message);
            if(fileName == null) return null;
            for(int i = 0; i < fileName.length(); i++){
                int c = (int) fileName.charAt(i);
                if(!((c >= 65 && c <= 90) || (c >= 97 && c <= 122) || (c >= 48 && c <= 57) || c == 45 || c == 46 || c == 95)){
                    invalidName = true;
                    break;
                }
                else invalidName = false;
            }

            if(!invalidName){
                if(savedGames != null){
                    for(String f : savedGames){
                        if(fileName.equals(f.replace(".bin", ""))){
                            int replaceGame = JOptionPane.showConfirmDialog(null, "A previously saved game has the same name.\nReplace previously saved game?", "Warning", JOptionPane.YES_NO_OPTION);
                            if(replaceGame != JOptionPane.YES_OPTION){
                                invalidName = true;
                                break;
                            }
                        }
                    }
                }
            }
            if(invalidName){
                message.setLength(0);
                message.append(String.format("Invalid name entered.\nValid name characters: \n%5s- Letters: a-z and A-Z\n%5s- Numbers: 0-9\n%5s- Characters: '_', '-', '.'\n\nEnter name to save game as: ", "", "", ""));
            }
        }
        return fileName;
    }

    /**
     * Loads a previously saved game from a file.
     *
     * @param fileName the name of the file to load the game from
     * @return the loaded Scrabble model
     */
    static ScrabbleModel load(String fileName){
        fileName = String.format("./GameAssets/SavedGames/%s.bin", fileName);
        try{
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object o = is.readObject();
            fis.close();
            is.close();
            return (ScrabbleModel) o;
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Saves the current Scrabble game state to a file.
     *
     * @param fileName The name of the file where the game will be saved.
     * @param m The current Scrabble game model object to be saved.
     * @throws RuntimeException If an error occurs while writing the game object to the file.
     */
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
     * Initializes the game by either starting a new game or loading an existing saved game file.
     * Displays a dialog for the user to choose whether to start a new game or load an old one.
     * If no saved games are available, a new game is started.
     * Display a dialog that prompts the user to select the board they would like to play the game with.
     */
    private void initGame(){
        int playerGameLoadChoice = -1;
        // Need to figure out if there are any saved games
        File path = new File("./GameAssets/SavedGames");
        path.mkdirs(); //Must be sure to create directory if not present. Otherwise, doesn't work correctly. We don't care about result...
        String[] savedGames = path.list();
        // There are no saved games, so have to start a new game.
        if(savedGames != null) if(savedGames.length == 0) playerGameLoadChoice = 0;

        // NEED TO FIGURE OUT IF NEW GAME OR LOADING OLD GAME
        if(playerGameLoadChoice != 0){
            String[] playerGameOptions = {"New Game", "Load Game"};
            playerGameLoadChoice = JOptionPane.showOptionDialog(null, "Welcome to Scrabble!\nWould you like to start a new game or load a previously saved one?", "Scrabble", 0, 2, new ImageIcon("./GameAssets/Pictures/S_Icon.png"), playerGameOptions, playerGameOptions[0]);
        }
        else{
            String[] playerGameOptions = {"New Game"};
            playerGameLoadChoice = JOptionPane.showOptionDialog(null, "Welcome to Scrabble!\nReady to play?", "Scrabble", 0, 2, new ImageIcon("./GameAssets/Pictures/S_Icon.png"), playerGameOptions, playerGameOptions[0]);
        }

        switch(playerGameLoadChoice){
            // Player decides to start a new game or has to start new game. No old games to load.
            case 0:
                Object[] options = {"Classic", "New Wave", "No Bonus"};
                String bonusSelection = JOptionPane.showInputDialog(null, "Select Premium Square Layout", "New Game: Board Style Selection", JOptionPane.PLAIN_MESSAGE, null, options, options[0]).toString();
                if(!bonusSelection.equals(options[2])) model.getBoard().initBoard(bonusSelection.replace(" ", ""));
                // Initialize players
                initPlayers();
                break;
            case 1:
                loadGame();
                break;
            default:
                exit(0);
                break;
        }
    }
    /**
     * Initializes the menu bar of game-related options for player as a GUI.
     * Sets option buttons for saving a game, and undoing and redoing a move.
     */
    private void initMenuBar(){
        menuBar = new JMenuBar();
        game = new JMenu("Game");
        turns = new JMenu("Turns");

        undo = new JMenuItem("Undo"); // TODO: Go back to previous human player state?
        redo = new JMenuItem("Redo");
        save = new JMenuItem("Save Game");
        end = new JMenuItem("End Game");

        save.addActionListener(e -> saveGame());
        end.addActionListener(e -> endGame(true));
        undo.addActionListener(sc);
        undo.setActionCommand("X");
        redo.addActionListener(sc);
        redo.setActionCommand("Y");

        game.add(save);
        game.add(end);

        turns.add(undo);
        turns.add(redo);

        menuBar.add(game);
        menuBar.add(turns);
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
            tileHolder[i].setFocusable(false);

            if(model.getCurrentPlayer().getTile(i) != null){
                tileHolder[i].setBackground(new Color(0xc69f77));
                tileLabels[i][0] = new JLabel(model.getCurrentPlayer().getTile(i).getChar());
                tileLabels[i][1] = new JLabel(String.format("             %d", model.getCurrentPlayer().getTile(i).getValue()));
            }
            else{
                tileHolder[i].setBackground(new Color(0x444343));
                tileLabels[i][0] = new JLabel("");
                tileLabels[i][1] = new JLabel("");
            }

            tileLabels[i][0].setHorizontalAlignment(JTextField.CENTER);
            tileLabels[i][1].setHorizontalAlignment(JTextField.CENTER);
            tileLabels[i][0].setPreferredSize(new Dimension(23,23));
            tileLabels[i][1].setPreferredSize(new Dimension(23,23));

            tileHolder[i].add(new JLabel(""));
            tileHolder[i].add(tileLabels[i][0]);
            tileHolder[i].add(tileLabels[i][1]);

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
        for(int i = 0; i < playerInfo.length; i++){
            playerInfo[i][0].setForeground(new Color(0x000000));
            playerInfo[i][1].setText(String.format("Score: %d", model.getPlayers().get(i).getScore()));
        }
        // Set current player's name to bright color
        playerInfo[model.getPlayerTurn()][0].setForeground(new Color(0xFF0000));
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
                if(input == null) exit(0); // User pressed cancel.
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
            if(input == null) exit(0); // User pressed cancel
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
        exit(0);
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
    private void endGame(boolean abruptEnding){
        boolean deleted = false;
        if(abruptEnding && !gameFile.isBlank()){
            int endingChoice = JOptionPane.showConfirmDialog(null, "Are you sure? Terminating the game will delete previously saved progress.", "End Game Confirmation", JOptionPane.YES_NO_OPTION);
            if(endingChoice != JOptionPane.YES_OPTION) return;
        }
        if(!gameFile.isBlank()){
            File fileToDelete = new File(String.format("./GameAssets/SavedGames/%s.bin", gameFile));
            deleted = fileToDelete.delete();
        }

        JOptionPane.showMessageDialog(this, model.gameResults(deleted));
        exit(0);
    }
    /**
     * Method used to update the board after a players turn.
     * Ends the game if game status is over. Initiates end of game process and displays game results.
     */
    @Override
    public void updateBoard(){
        if(model.getStatus() == ScrabbleModel.Status.OVER) {
            endGame(false);
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
