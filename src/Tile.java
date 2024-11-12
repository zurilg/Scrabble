/**
 * Tile class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-12-2024
 */

public class Tile {
    private String character;
    private int value;

    /**
     * Tile constructor method. Creates a new tile with the provided attributes.
     *
     * @param character The letter associated with the tile.
     * @param value The point value associated with the tile.
     */
    public Tile(String character, int value){
        this.character = character;
        this.value = value;
    }

    /**
     * Accessor for the tile's letter.
     *
     * @return The letter associated with the tile (as a string).
     */
    public String getChar(){
        return this.character;
    }

    /**
     * Accessor for the tile's point value.
     *
     * @return The point value associated with the tile.
     */
    public int getValue(){
        return this.value;
    }
}