/**
 * Player class
 * Represents a player in the Scrabble game, including their name, score, and tile holder.
 *
 * @author Zuri Lane-Griffore
 * @author Mohammad Ahmadi
 * @author Abdul Aziz Al-Sibakhi
 * @author Redah Eliwa
 *
 * @version 11-11-2024
 */
import java.util.ArrayList;
public class Player {
    private String name;
    private int score;
    private ArrayList<Tile> tileHolder;
    private ArrayList<Tile> prevTiles;
    private boolean played;

    /**
     * Constructor method for player. Initializes name, score, and tiles.
     *
     * @param name The player's chosen name.
     */
    public Player(String name){
        this.name = name;
        score = 0;
        tileHolder = new ArrayList<Tile>(0);
        prevTiles = new ArrayList<Tile>(7);
        played = false;
    }

    /**
     * Constructor method for player.
     * Creates a new player that is the copy of another Player object,
     * with the same name, score, and tiles.
     *
     * @param p Player object that is to be copied.
     */
    public Player(Player p){
        this.name = p.getName();
        this.score = p.getScore();
        this.tileHolder = p.getTiles();
        prevTiles = p.getPrevTiles();
    }

    /**
     * Returns whether a player has player their turn or not.
     * @return True if a player has played their turn, false otherwise
     */
    public boolean getPlayed(){
        return played;
    }

    /**
     * Returns the tiles previously held by the player before their most recent action.
     * @return ArrayList of the players previously held Tile objects.
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
     * Sets a specific tile in the players tile holder as used.
     * @param index Index of the tile that is set as used.
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
     * Saves the current tiles held by the player for undo/reset purposes.
     */
    public void setPrevTiles(){
        prevTiles.clear();
        for(int i = 0; i < ScrabbleModel.NUM_PLAYER_TILES; i++)
            prevTiles.add(tileHolder.get(i));
    }

    /**
     * Resets the players tile holder to its previous saved state.
     */
    public void resetTiles(){
        tileHolder.clear();
        for(int i = 0; i < ScrabbleModel.NUM_PLAYER_TILES; i++)
            tileHolder.add(prevTiles.get(i));

        played = false;
    }

}


