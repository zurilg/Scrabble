public class Board {
    private BoardSquare[][] board = new BoardSquare[15][15];
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
            }
        }
        //Triple Words
        for(int i = 0; i<=14; i+=7){
            for(int j = 0; j<=14; j+=7){
                if(!(i == 7 && j == 7)){
                    board[i][j] = new BoardSquare(0, 3);
                }
            }
        }

    }

    public void placeTile(Tile t, int[] coordinates){
        empty = false;
        board[coordinates[0]][coordinates[1]].placeTile(t);
    }

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
            if(i > 8) squareLetters = " " + (i+1) + "  ";
            else squareLetters = "  " + (i+1) + "  ";
            String squarePoints = "     ";
            for(int j = 0; j < 15; j++){
                // Get squareSpecsLine
                if(board[i][j].getWordScore() != 0){ squareSpecs += "|  " + board[i][j].getWordScore() + " W  "; }
                else if(board[i][j].getLetterScore() != 0){ squareSpecs += "|  " + board[i][j].getLetterScore() + " L  "; }
                else{ squareSpecs += "|       "; }

                // Get tile character line
                if(board[i][j].getLetter() != null){ squareLetters += "|   " + board[i][j].getLetter() + "   "; }
                else{ squareLetters += "|       "; }

                // Get tile point line
                if(board[i][j].getLetterPoint() != -1){ squarePoints += "|   " + board[i][j].getLetterPoint() + "   "; }
                else{ squarePoints += "|       "; }

                if(j == 14){squareSpecs += "|\n"; squareLetters += "|\n"; squarePoints += "|\n";}
            }
            strBoard += squareSpecs + squareLetters + squarePoints;
            if(i == 14) { strBoard += ("     ----A-------B-------C-------D-------E-------F-------G-------H-------I-------J-------K-------L-------M-------N-------O----\n"); }
        }
        return strBoard;
    }
}
