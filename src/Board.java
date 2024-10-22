/**
 * Board class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 10-22-2024
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
        String strBoard = "";
        for(int i = 0; i < 15; i++){
            strBoard += ("     -------------------------------------------------------------------------------------------------------------------------\n");
            String squareSpecs = "     ";
            String squareLetters;
            if(i > 8) { squareLetters = " " + (i+1) + "  "; }
            else { squareLetters = "  " + (i+1) + "  "; }
            String squarePoints = "     ";
            for(int j = 0; j < 15; j++){
                // Get squareSpecsLine
                if(board[i][j].getWordScore() != 0){ squareSpecs += "|  " + board[i][j].getWordScore() + " W  "; }
                else if(board[i][j].getLetterScore() != 0){ squareSpecs += "|  " + board[i][j].getLetterScore() + " L  "; }
                else { squareSpecs += "|       "; }

                // Get tile character line
                if(board[i][j].getLetter() != null){ squareLetters += "|   " + board[i][j].getLetter() + "   "; }
                else { squareLetters += "|       "; }

                // Get tile point line
                if(board[i][j].getLetterPoint() != -1){ squarePoints += "|   " + board[i][j].getLetterPoint() + "   "; }
                else { squarePoints += "|       "; }

                if(j == 14){ squareSpecs += "|\n"; squareLetters += "|\n"; squarePoints += "|\n"; }
            }
            strBoard += squareSpecs + squareLetters + squarePoints;
            if(i == 14){ strBoard += ("     ----A-------B-------C-------D-------E-------F-------G-------H-------I-------J-------K-------L-------M-------N-------O----\n"); }
        }
        return strBoard;
    }
}
