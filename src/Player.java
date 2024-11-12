import java.util.ArrayList;
/**
 * Player class.
 * Represents a player in the Scrabble game, including their name, score, and tile holder.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-12-2024
 */
public class Player {
    private final String name; // The player's name
    private int score; // The player's overall score
    private final ArrayList<Tile> tileHolder; // The tiles the player is currently holding
    private final ArrayList<Tile> prevTiles; // Stores the state of tiles before player turn
    private boolean played; // Whether a player has finished their turn

    /**
     * Constructor method for player. Initializes name, score, and tiles.
     *
     * @param name The player's chosen name.
     */
    public Player(String name){
        this.name = name;
        score = 0;
        tileHolder = new ArrayList<>(0);
        prevTiles = new ArrayList<>(7);
        played = false;
    }

    /**
     * Overridden constructor. Accepts a player object opposed to just a name.
     *
     * @param p The Player object to be copied.
     */
    public Player(Player p){
        this.name = p.getName();
        this.score = p.getScore();
        this.tileHolder = p.getTiles();
        prevTiles = p.getPrevTiles();
        this.played = p.getPlayed();
    }


    /**
     * Accessor method for player turn status.
     *
     * @return true: player finished their turn, false: player hasn't finished their turn
     */
    public boolean getPlayed(){
        return played;
    }

    /**
     * Accessor method for a player's stored tile holder state.
     *
     * @return The state of the player's tile holder before their turn.
     */
    public ArrayList<Tile> getPrevTiles(){
        return prevTiles;
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

    /**
     * Adds turn score to player's total score.
     *
     * @param turn The score obtained during the player's turn.
     */
    public void addToScore(int turn) { score += turn; }

    /**
     * Return a representation of the entire tile holder.
     *
     * @return ArrayList of tiles representing the tile holder.
     */
    public ArrayList<Tile> getTiles(){ return tileHolder; }

    /**
     * Adds the provided tile to the tile holder.
     *
     * @param tile Tile to add to tile holder.
     */
    public void addTileToHolder(Tile tile){
        tileHolder.add(tile);
    }

    /**
     * @param index The index of the tile to set as used (null)
     */
    public void setTileAsUsed(int index) {
        tileHolder.set(index, null);
        played = true;
    }

    /**
     * Returns the number of tiles that are currently in a player's tile holder.
     *
     * @return Number of tiles tile holders.
     */
    public int numTiles(){
        int size = 0;
        for(int i = 0; i < 7; i++){
            if(tileHolder.get(i) != null) size += 1;
        }
        return size;
    }

    /**
     * Stores the current state of a player's tile holder.
     */
    public void setPrevTiles(){
        prevTiles.clear();
        for(int i = 0; i < ScrabbleModel.NUM_PLAYER_TILES; i++)
            prevTiles.add(tileHolder.get(i));
    }

    /**
     * Resets the current state of a player's tile holder to its past state.
     */
    public void resetTiles(){
        tileHolder.clear();
        for(int i = 0; i < ScrabbleModel.NUM_PLAYER_TILES; i++)
            tileHolder.add(prevTiles.get(i));

        played = false;
    }
}