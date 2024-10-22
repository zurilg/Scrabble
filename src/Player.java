/**
 * Player class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 10-22-2024
 */

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
     * Removes the specified tile from the player's tile holder.
     *
     * @param t Tile to remove.
     */
    public void removeTile(Tile t){
        tileHolder.remove(t);
    }

    /**
     * Removes and returns the tile at the end of the tile holder.
     *
     * @return The tile at the end of the tile holder.
     */
    public Tile popLastTile(){
        Tile t;
        if(!tileHolder.isEmpty()){
            t = tileHolder.getLast();
            tileHolder.remove(t);
            return t;
        }
       return null;
    }

    /**
     * Returns the tile that is currently at the end of the tile holder.
     *
     * @return The tile at the end of the tile holder.
     */
    public Tile showLastTile(){
        return tileHolder.getLast();
    }

    /**
     * Returns the number of tiles that are currently in a player's tile holder.
     *
     * @return Number of tiles tile holders.
     */
    public int numTiles(){
        return tileHolder.size();
    }

    /**
     * Provides a string representation of the player assets (their name, tiles, and score).
     *
     * @return String representation of the player assets.
     */
    public String toString(){
        StringBuilder tileCharacters = new StringBuilder();
        for(Tile t : tileHolder){
            tileCharacters.append("\"").append(t.getChar()).append("\"  ");
        }
        return "Player: " + this.name + "\n   - Tiles: " + tileCharacters + "\n   - Score: " + score + " pts";
    }

}


