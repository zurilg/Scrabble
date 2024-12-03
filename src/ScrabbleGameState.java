import java.io.Serializable;
import java.util.ArrayList;

public class ScrabbleGameState implements Serializable {
    private Board board;  // Save the board state
    private ArrayList<Player> players;  // Save player states
    private TileBag tileBag;  // Save the tile bag
    private int playerTurnIndex;  // Track whose turn it is

    public ScrabbleGameState(Board board, ArrayList<Player> players, TileBag tileBag, int index){
        this.board = new Board(board);
        this.players = new ArrayList<>();
        for (Player player : players) {
            System.out.println(player.getName());
            if (player instanceof PlayerAI) this.players.add(new PlayerAI((PlayerAI) player));
            else {
                this.players.add(new Player(player));
                System.out.println("SAVING HUMAN PLAYER SCORE AND STUFF");
            }
        }
        this.tileBag = new TileBag(tileBag);
        this.playerTurnIndex = index;
    }

    public Board getBoard(){ return board; }
    public ArrayList<Player> getPlayers(){ return players; }
    public TileBag getTileBag() { return tileBag; }
    public int getPlayerTurn() { return playerTurnIndex; }
}
