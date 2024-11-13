/**
 * ScrabbleEvent class
 *
 * This class represents an event in the Scrabble game.
 *
 * Holds details about the players actions during their turn, including the selected tile and its index
 * and the status of the game.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-12-2024
 */

import java.util.EventObject;
public class ScrabbleEvent extends EventObject{
    private int r;
    private int c;
    private int selectedTile;
    private Board board;
    private Player currentPlayer;
    private ScrabbleModel.Status status;

    /**
     * Constructor method for ScrabbleEvent.
     * Initializes the specified details of an action event.
     * @param model Instance of scrabble model.
     * @param r Row index on the board where action occurred.
     * @param c Column index on the board where action occurred.
     * @param t Tile that was selected by the player.
     * @param b An instance of the board to show the current state of the board.
     * @param p Player that caused the action.
     * @param status Current status of the game.
     */
    public ScrabbleEvent(ScrabbleModel model, int r, int c, int t, Board b, Player p, ScrabbleModel.Status status){
        super(model);
        this.r = r;
        this.c = c;
        this.selectedTile = t;
        this.board = b;
        this.currentPlayer = p;
        this.status = status;
    }

    /**
     * Getter method for the row where action occurred.
     * @return The row index.
     */
    public int getR(){
        return this.r;
    }
    /**
     * Getter method for the column where action occurred.
     * @return The column index.
     */
    public int getC(){
        return this.c;
    }

    /**
     * Getter method for the tile that was selected by the player.
     * @return The tile that was selected by the player.
     */
    public int getSelectedTile(){
        return this.selectedTile;
    }

    /**
     * Getter method for the current state of the Scrabble game board.
     *
     * @return the board.
     */
    public Board getBoard(){
        return board;
    }

    /**
     * Getter method for the player that caused the action.
     * @return The player object that caused the action.
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Getter method for the current status of the game.
     *
     * @return the game status
     */
    public ScrabbleModel.Status getStatus(){
        return status;
    }
}