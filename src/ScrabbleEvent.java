import java.util.EventObject;

public class ScrabbleEvent extends EventObject{
    private int r;
    private int c;
    private int selectedTile;
    private Board board;
    private Player currentPlayer;
    private ScrabbleModel.Status status;


    public ScrabbleEvent(ScrabbleModel model, int r, int c, int t, Board b, Player p, ScrabbleModel.Status status){
        super(model);
        this.r = r;
        this.c = c;
        this.selectedTile = t;
        this.board = b;
        this.currentPlayer = p;
        this.status = status;
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

    public ScrabbleModel.Status getStatus(){
        return status;
    }
}
