import java.io.Serializable;

/**
 * Player class.
 * Represents a player in the Scrabble game, including their name, score, and tile holder.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 12-06-2024
 */
public class Player implements Serializable {
    public static final int TILE_HOLDER_SIZE = 7;
    private String name;
    private int score;
    private Tile[] tileHolder;
    private Tile[] prevTiles;
    /**
     * Constructor method for player. Initializes name, score, and tiles.
     *
     * @param name The player's chosen name.
     */
    public Player(String name){
        this.name = name;
        score = 0;
        tileHolder = new Tile[TILE_HOLDER_SIZE];
        prevTiles = new Tile[TILE_HOLDER_SIZE];
        for(int i = 0; i < TILE_HOLDER_SIZE; i++) {
            tileHolder[i] = null; // Initialize tile holders as empty initially.
            prevTiles[i] = null;
        }
    }
    /**
     * Player constructor used to create a copy of a Player object.
     * Used for game saving purposes.
     * @param p Player object that is to be copied
     */
    public Player(Player p){
        tileHolder = new Tile[TILE_HOLDER_SIZE];
        prevTiles = new Tile[TILE_HOLDER_SIZE];
        this.name = p.getName();
        this.score = p.getScore();
        for(int i = 0; i < TILE_HOLDER_SIZE; i++) this.tileHolder[i] = new Tile(p.getTile(i));
        for(int i = 0; i < TILE_HOLDER_SIZE; i++) this.prevTiles[i] = new Tile(p.prevTiles[i]);
    }
    /**
     * Player constructor used to create a copy of a Player object.
     * Used for game saving purposes.
     *
     * @param name The name of the player.
     * @param score The current score of the player.
     * @param th The array of Tile objects representing the player's current tile holder.
     * @param pt The array of Tile objects representing the player's previous tiles.
     */
    public Player(String name, int score, Tile[] th, Tile[] pt){
        tileHolder = new Tile[TILE_HOLDER_SIZE];
        prevTiles = new Tile[TILE_HOLDER_SIZE];
        this.name = String.format("%s",name);
        this.score = score;
        for(int i = 0; i < TILE_HOLDER_SIZE; i++) {
            if(th[i] != null) this.tileHolder[i] = new Tile(th[i]);
            else this.tileHolder[i] = null;
        }
        for(int i = 0; i < TILE_HOLDER_SIZE; i++) {
            if(th[i] != null) this.prevTiles[i] = new Tile(pt[i]);
            else this.tileHolder[i] = null;
        }
    }

    /**
     * Getter method that returns an array of tile objects that represents the tile holder
     * @return array of Tile objects that represents the tile holder
     */
    public Tile[] getTileHolder(){ return tileHolder; }

    /**
     * Getter method that returns an array represting the player's previous tiles.
     *
     * @return array of Tile objects that represents the player's previous tiles.
     */
    public Tile[] getPrevTiles(){ return prevTiles; }

    /**
     * Returns tile in tile holder at specified index.
     *
     * @param index Index of tile in tile holder.
     * @return The tile at specified index.
     */
    public Tile getTile(int index){
        if(index >=0 && index < TILE_HOLDER_SIZE) return tileHolder[index]; // If index range is correct, return tile at that index in tile holder.
        return null; // Out of bounds.
    }

    /**
     * Removes and returns the tile at the specified index in the tile holder.
     *
     * @param index The index of the desired tile in the tile holder.
     * @return The removed tile.
     */
    public Tile popTile(int index){
        Tile t = null;
        if(index >=0 && index <= TILE_HOLDER_SIZE){
            t = tileHolder[index];
            tileHolder[index] = null;
        }
        return t; // Return null if that tile holder slot is empty or range is invalid. Otherwise, return tile at that index.
    }
    /**
     * Adds the provided tile to the tile holder at the specified index.
     *
     * @param index The index at which to add the tile.
     * @param t The tile to add.
     */
    public void addTile(int index, Tile t){ tileHolder[index] = t; }
    /**
     * Stores the current state of a player's tile holder.
     */
    public void setPrevTiles(){ System.arraycopy(tileHolder, 0, prevTiles, 0, Player.TILE_HOLDER_SIZE); }
    /**
     * Resets the current state of a player's tile holder to its past state.
     */
    public void resetTileHolder(){ System.arraycopy(prevTiles, 0, tileHolder, 0, Player.TILE_HOLDER_SIZE); }
    /**
     * Adds turn score to player's total score.
     *
     * @param s The score obtained during the player's turn.
     */
    public void addToScore(int s){ score += s; }
    /**
     * Accessor method for player name.
     *
     * @return name
     */
    public String getName(){ return name; }
    /**
     * Accessor method for player score.
     *
     * @return score
     */
    public int getScore(){ return score; }
    /**
     * Returns the number of tiles that are currently in a player's tile holder.
     *
     * @return Number of tiles tile holders.
     */
    public int numTiles(){
        int num = 0;
        for(Tile t : tileHolder) if(t != null) num++;
        return num;
    }
}
