import java.util.EventObject;
/**
 * ScrabbleEvent class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-24-2024
 */
public class ScrabbleEvent{
    /**
     * Tile placement event class.
     * Provides play information when a user places a tile on the board.
     */
    public static class TilePlacement extends EventObject{
        private final int r, c, selectedTile;
        /**
         * Constructor for a tile placement event.
         *
         * @param model The observed model.
         * @param r The row which the user placed their tile.
         * @param c The column which the user placed their tile.
         * @param selectedTile The index of the selected user tile.
         */
        public TilePlacement(ScrabbleModel model, int r, int c, int selectedTile){
            super(model);
            this.r = r;
            this.c = c;
            this.selectedTile = selectedTile;
        }
        /**
         * Getter method for the row where tile is placed.
         * @return The row coordinate.
         */
        public int getR(){ return r; }
        /**
         * Getter method for the column where tile is placed.
         * @return The column coordinate.
         */
        public int getC(){ return c; }
        /**
         * Getter method for the index of tile that was selected by the player.
         * @return The tile that was selected by the player.
         */
        public int getSelectedTile(){ return selectedTile; }
    }
    // Inner classes are used for easy implementation of different types of scrabble events (sub/inner classes).
}
