import java.util.ArrayList;

public class Player {
    private String name;
    private int score;
    private ArrayList<Tile> tileHolder;

    /**
     * Constructor method for player. Initializes name, score, and tiles.
     *
     * @param name The player's chosen name.
     */
    public Player(String name){
        this.name = name;
        score = 0;
        tileHolder = new ArrayList<>();
    }

    /**
     * Getter method for player name.
     *
     * @return name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Getter method for player score.
     *
     * @return score
     */
    public int getScore(){
        return this.score;
    }

    public void addToScore(int turn) { score += turn; }

    public ArrayList<Tile> getTiles(){ return tileHolder; }

    public void addTileToHolder(Tile tile){
        tileHolder.add(tile);
    }

    public void removeTileFromHolder(Tile tile){
        tileHolder.remove(tile);
    }

    public Tile popLastTile(){
        Tile t;
        if(!tileHolder.isEmpty()){
            t = tileHolder.getLast();
            tileHolder.remove(t);
            return t;
        }
       return null;
    }

    public Tile showLastTile(){
        return tileHolder.getLast();
    }

    public int numTiles(){
        return tileHolder.size();
    }

    public void removeTile(Tile t){
        tileHolder.remove(t);
    }

    public String toString(){
        String tileCharacters = "";
        for(Tile t : tileHolder){
            tileCharacters += "\"" + t.getChar() + "\"  ";
        }
        return "Player: " + this.name + "\n   - Tiles: " + tileCharacters + "\n   - Score: " + score + " pts";
    }

}


