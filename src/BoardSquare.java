/**
 * BoardSquare class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-24-2024
 */
public class BoardSquare {
    private Tile t;
    private int wordScore;
    private int letterScore;
    /**
     * Constructor for BoardSquare.
     *
     * @param letterScore The letter score multiplier for the board square.
     * @param wordScore The word score multiplier for the board square.
     */
    public BoardSquare(int letterScore, int wordScore){
        t = null;
        this.letterScore = letterScore;
        this.wordScore = wordScore;
    }
    /**
     * Show what tile is on the board square.
     *
     * @return Tile on board square.
     */
    public Tile getTile() { return t; }
    /**
     * Returns the word score multiplier corresponding to the square.
     *
     * @return The word score bonus of the board square.
     */
    public int getWordScore() { return wordScore; }
    /**
     * Returns the letter score multiplier corresponding to the square.
     *
     * @return The letter score bonus of the board square.
     */
    public int getLetterScore() { return letterScore; }
    /**
     * Places tile on board square.
     *
     * @param t Tile to place.
     */
    public void placeTile(Tile t) { this.t = t; }
}
