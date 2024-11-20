public class BoardSquare {
    private Tile t;
    private int wordScore;
    private int letterScore;

    public BoardSquare(int letterScore, int wordScore){
        t = null;
        this.letterScore = letterScore;
        this.wordScore = wordScore;
    }

    // Accessors
    public Tile getTile() { return t; }

    public int getWordScore() { return wordScore; }

    public int getLetterScore() { return letterScore; }

    // Mutators
    public void placeTile(Tile t) { this.t = t; }

}
