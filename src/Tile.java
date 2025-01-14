import java.io.Serializable;

/**
 * Tile class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 12-06-2024
 */
public class Tile implements Serializable {
    private String character;
    private final int value;
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
     * Constructor for Tile that is used to create a copy of a Tile object.
     * Used for game saving purposes.
     * @param t Tile object that is to be copied.
     */
    public Tile(Tile t){
        this.character = t.getChar();
        this.value = t.getValue();
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
     * Mutator for the tile's letter. Used for blank tiles.
     *
     * @param character The character the player decides to set the blank tile to.
     */
    public void setChar(String character){
        this.character = character;
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