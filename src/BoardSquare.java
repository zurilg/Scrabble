public class BoardSquare {
    private Tile t;
    private int letterScore;
    private int wordScore;

    public BoardSquare(int letterScore, int wordScore){
        t = null;
        this.letterScore = letterScore;
        this.wordScore = wordScore;
    }
    public Tile getTile(){
        return t;
    }
    public void placeTile(Tile t){
        this.t = t;
    }

    public Tile popTile(){
        Tile tile = this.t;
        this.t = null;
        return tile;
    }

    public int getLetterScore() {
        return this.letterScore;
    }

    public int getWordScore(){
        return this.wordScore;
    }

    public String getLetter(){
        if(t == null) return null;
        return t.getChar();
    }

    public int getLetterPoint(){
        if(t == null) return -1;
        return t.getValue();
    }
}
