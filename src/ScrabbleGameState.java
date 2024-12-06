import java.io.Serializable;
import java.util.ArrayList;
/**
 * This class is a model representing the current state of the Scrabble game
 *
 * It contains the current state of the board, players, tile bag, and whose turn it is
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 12-06-2024
 */
public class ScrabbleGameState implements Serializable {
    private Board board;  // Save the board state
    private ArrayList<Player> players;  // Save player states
    private TileBag tileBag;  // Save the tile bag
    private int playerTurnIndex;  // Track whose turn it is

    /**
     * Constructor method. Initializes the board, players, tile bag, and current turn
     * according to the current game state.
     * @param board   the current state of the board
     * @param players the ArrayList of Player objects participating in the game
     * @param tileBag the tile bag containing remaining tiles
     * @param index   the index of the player whose turn it is
     */
    public ScrabbleGameState(Board board, ArrayList<Player> players, TileBag tileBag, int index){
        this.board = new Board(board);
        this.players = new ArrayList<>();
        for (Player player : players) {
            if (player instanceof PlayerAI) this.players.add(new PlayerAI((PlayerAI) player));
            else this.players.add(new Player(player));
        }
        this.tileBag = new TileBag(tileBag);
        this.playerTurnIndex = index;
    }
    /**
     * Returns the current state of the board.
     *
     * @return the Board object representing the current state of the board
     */
    public Board getBoard(){ return board; }
    /**
     * Returns the list of players participating in the game.
     *
     * @return ArrayList of Player objects representing the players in the game with their current status
     */
    public ArrayList<Player> getPlayers(){ return players; }
    /**
     * Returns the current state of the tile bag.
     *
     * @return TileBag object representing the remaining tiles
     */
    public TileBag getTileBag() { return tileBag; }

    /**
     * Returns the index of the player whose turn it is.
     *
     * @return the index of the current player
     */
    public int getPlayerTurn() { return playerTurnIndex; }
}
