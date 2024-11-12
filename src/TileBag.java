/**
 * TileBag class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-10-2024
 */

import java.util.ArrayList;
import java.util.Random;
public class TileBag {
    private ArrayList<Tile> bag;

    public static final int[] TILE_VALUES = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
    public static final String[] TILE_CHARS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * TileBag constructor method. Fills the bag with the 98 game tiles.
     */
    public TileBag(){
        bag = new ArrayList<>();
        fillBag();
    }

    /**
     * Fill the bag with 98 game tiles.
     */
    private void fillBag(){
        // Indexes: 0 (A) - 25 (Z)         ---       ONLY HAVE 98 GAME TILES. ARE NOT USING BLANK TILES.

        int[] numTiles = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};

        int alphaIndex = 0;
        for(int i : numTiles){
            while(i > 0){
                bag.add(new Tile(TILE_CHARS[alphaIndex], TILE_VALUES[alphaIndex]));
                i--;
            }
            alphaIndex++;
        }
    }

    /**
     * Remove a random tile from the bag and return it.
     *
     * @return A random tile from the bag.
     */
    public Tile popTile(){
        Random rand = new Random();
        int randIndex = rand.nextInt(bag.size());
        Tile pickedTile = bag.get(randIndex);
        bag.remove(randIndex);
        return pickedTile;
    }


    /**
     * Return the size of the bag.
     *
     * @return Number of tiles remaining in the bag.
     */
    public int returnSize(){
        return bag.size();
    }
}
