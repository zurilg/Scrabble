/**
 * Board class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-10-2024
 */
public class Board {
    private BoardSquare[][] board;
    private BoardSquare[][] prevState;
    private boolean empty;

    /**
     * This is the board constructor. It initializes every board square to
     */
    public Board(){
        board = new BoardSquare[ScrabbleModel.BOARD_SIZE][ScrabbleModel.BOARD_SIZE];
        prevState = new BoardSquare[ScrabbleModel.BOARD_SIZE][ScrabbleModel.BOARD_SIZE];

        empty = true;

        // Initialize all squares as empty
        for(int i = 0; i < ScrabbleModel.BOARD_SIZE; i+=1){
            for(int j = 0; j < ScrabbleModel.BOARD_SIZE; j+=1){
                board[i][j] = new BoardSquare(0,0);
                prevState[i][j] = new BoardSquare(0,0);
            }
        }
        //Triple Words
        for(int i = 0; i<=14; i+=7){
            for(int j = 0; j<=14; j+=7)
                if(!(i == 7 && j == 7))
                    board[i][j] = new BoardSquare(0, 3);
        }

    }

    /**
     * Saves the current state of the board for future reset purposes.
     */
    public void setPrevState(){
        for(int i = 0; i<=14; i+=1)
            for(int j = 0; j<=14; j+=1)
                prevState[i][j].placeTile(board[i][j].getTile());
    }

    /**
     * Resets board back to state previously saved.
     */
    public void reset(){
        for(int i = 0; i<=14; i+=1)
            for(int j = 0; j<=14; j+=1)
                board[i][j].placeTile(prevState[i][j].getTile());
    }

    /**
     * Returns the tile letter on the corresponding board square provided coordinates.
     *
     * @param coordinates The coordinates of the board square to retrieve the letter from.
     * @return The letter (as a string) at the specified board coordinate.
     */
    public String getLetterAtIndex(int r, int c){
        return board[r][c].getLetter();
    }

    public BoardSquare getSqAtIndex(int r, int c){ return board[r][c]; }

    /**
     * Places the provided game tile on the specified coordinates.
     *
     * @param t The tile to place on the specified coordinate.
     * @param coordinates The coordinate of the board square to place the tile on.
     */
    public void placeTile(Tile t, int r, int c){
        empty = false;
        board[r][c].placeTile(t);
    }

    /**
     * Returns whether the board is empty.
     *
     * @return Board empty (True : False)
     */
    public boolean isEmpty(){
        return empty;
    }

}
