import java.util.EventObject;

public class ScrabbleEvent extends EventObject{
    private int r;
    private int c;
    private int selectedTile;
    private Board board;
    private Player currentPlayer;


    public ScrabbleEvent(ScrabbleModel model, int r, int c, int t, Board b, Player p){
        super(model);
        this.r = r;
        this.c = c;
        this.selectedTile = t;
        this.board = b;
        this.currentPlayer = p;
    }

    public int getR(){
        return this.r;
    }

    public int getC(){
        return this.c;
    }

    public int getSelectedTile(){
        return this.selectedTile;
    }

    public Board getBoard(){
        return board;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }
}
