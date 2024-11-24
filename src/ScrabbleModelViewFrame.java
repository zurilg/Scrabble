import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import static java.lang.System.exit;

public class ScrabbleModelViewFrame extends JFrame implements ScrabbleModelView {
    private ScrabbleModel model;
    private ScrabbleController sc;
    private int numPlayers;

    // Attributes of the board display (center panel)
    private JButton[][] boardButtons; // 15 x 15 grid of buttons for the board.
    private JLabel[][][] buttonLabels; // 15 x 15 x 3 (Board Row x Board Column x Board Button Labels)
    private JPanel boardPanel; // Panel with a grid layout to hold the board buttons.

    // Attributes of user panel (south panel)
    private JButton[] tileHolder;
    private JLabel[][] tileLabels; // 7 x 2 labels. Tile character and points
    private JButton play, replace, skip;
    private JPanel userPanel; // Panel with 1 x 10 grid layout to hold all user play option buttons.

    // Attributes of the players names, points, and current player display (east panel)
    private JLabel[][] playerInfo; // N Player x 2
    private JPanel playersPanel;


    public ScrabbleModelViewFrame(){
        // Initialize basic frame aspects
        super("Scrabble"); // Call super class (JFrame)
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(new Dimension(925, 825));
        this.setLocationRelativeTo(null); // Center on screen

        // Initialize model
        model = new ScrabbleModel();
        model.addScrabbleView(this);

        // Initialize controller
        sc = new ScrabbleController(model);

        // Initialize players
        initPlayers();

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

    private void drawButtonPanel(){
        // Initialize play button
        play = new JButton("Play!");
        play.addActionListener(sc);
        play.setActionCommand("P");
        play.setBackground(new Color(0xB6D38D));
        play.setFocusable(false);
        // Initialize replace button
        replace = new JButton();
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
        skip = new JButton("Skip Turn");
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

    private void redrawPlayerInfo(){
        // Set current player's name to bright color
        playerInfo[model.getPlayerTurn()][0].setForeground(new Color(0xFF0000));
        // Find index of previous player
        int prevTurn = 0;
        if(model.getPlayerTurn() == 0) prevTurn = numPlayers - 1;
        else prevTurn = model.getPlayerTurn() - 1;
        // Update previous player's visuals
        playerInfo[prevTurn][0].setForeground(new Color(0x000000));
        playerInfo[prevTurn][1].setText(String.format("Score: %d", model.getPlayers().get(prevTurn).getScore()));
    }

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

    private void initPlayers(){
        ArrayList<String> playerNames = new ArrayList<>();
        int numHuman = 0;
        int numAI = 0;
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

    public static void fileReadError(String message){
        JOptionPane.showMessageDialog(null, message, "File Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    public static String getBlankInput(){
        String[] alpha = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        return JOptionPane.showInputDialog(null, "Choose letter to replace blank.", "Set Blank Tile", JOptionPane.PLAIN_MESSAGE, null, alpha, alpha[0]).toString();
    }

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

    public static void main(String[] args) {
        new ScrabbleModelViewFrame();
    }
}
