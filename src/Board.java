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
    private BoardSquare[][] board = new BoardSquare[15][15];
    private BoardSquare[][] prevState = new BoardSquare[15][15];
    private boolean empty;

    /**
     * This is the board constructor. It initializes every board square to
     */
    public Board(){
        empty = true;
        // Initialize all squares as empty
        for(int i = 0; i < 15; i+=1){
            for(int j = 0; j < 15; j+=1){
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
    public String getLetterAtIndex(int[] coordinates){
        return board[coordinates[0]][coordinates[1]].getLetter();
    }

    /**
     * Places the provided game tile on the specified coordinates.
     *
     * @param t The tile to place on the specified coordinate.
     * @param coordinates The coordinate of the board square to place the tile on.
     */
    public void placeTile(Tile t, int[] coordinates){
        empty = false;
        board[coordinates[0]][coordinates[1]].placeTile(t);
    }

    /**
     * Returns whether the board is empty.
     *
     * @return Board empty (True : False)
     */
    public boolean isEmpty(){
        return empty;
    }

    /**
     * This method returns a string representation of the board for the view to use and update with.
     *
     * @return A string representation of the board.
     */
    public String toString(){
        StringBuilder strBoard = new StringBuilder();
        for(int i = 0; i < 15; i++){
            strBoard.append("     -------------------------------------------------------------------------------------------------------------------------\n");
            StringBuilder squareSpecs = new StringBuilder("     ");
            StringBuilder squareLetters;
            if(i > 8) { squareLetters = new StringBuilder(" " + (i + 1) + "  "); }
            else { squareLetters = new StringBuilder("  " + (i + 1) + "  "); }
            StringBuilder squarePoints = new StringBuilder("     ");
            for(int j = 0; j < 15; j++){
                // Get squareSpecsLine
                if(board[i][j].getWordScore() != 0){ squareSpecs.append("|  ").append(board[i][j].getWordScore()).append(" W  "); }
                else if(board[i][j].getLetterScore() != 0){ squareSpecs.append("|  ").append(board[i][j].getLetterScore()).append(" L  "); }
                else { squareSpecs.append("|       "); }

                // Get tile character line
                if(board[i][j].getLetter() != null){ squareLetters.append("|   ").append(board[i][j].getLetter()).append("   "); }
                else { squareLetters.append("|       "); }

                // Get tile point line
                if(board[i][j].getLetterPoint() != -1){ squarePoints.append("|   ").append(board[i][j].getLetterPoint()).append("   "); }
                else { squarePoints.append("|       "); }

                if(j == 14){ squareSpecs.append("|\n"); squareLetters.append("|\n"); squarePoints.append("|\n"); }
            }
            strBoard.append(squareSpecs).append(squareLetters).append(squarePoints);
            if(i == 14){ strBoard.append("     ----A-------B-------C-------D-------E-------F-------G-------H-------I-------J-------K-------L-------M-------N-------O----\n"); }
        }
        return strBoard.toString();
    }
}
