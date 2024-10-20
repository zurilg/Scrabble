import java.util.ArrayList;

public class Player {
    private String name;
    private int score;
    private ArrayList<Tile> tileHolder;

    /**
     * Cosntructor method for player
     * initializes name, score, and tiles
     * @param name
     */
    public Player(String name){
        this.name = name;
        score = 0;
        tileHolder = new ArrayList<>();
    }

    /**
     * Getter method for player score
     * @return score
     */
    public String getName(){
        return this.name;
    }

    /**
     * Getter method for player score
     * @return score
     */
    public int getScore(){
        return this.score;
    }

    public void getTile(Tile tile){
        tileHolder.add(tile);
    }

    public Tile popLastTile(){
        Tile t = tileHolder.getLast();
        tileHolder.remove(t);
        return t;
    }

    public Tile showLastTile(){
        return tileHolder.getLast();
    }
}
