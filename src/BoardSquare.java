public class BoardSquare {
    private Tile t;
    private int letterScore;
    private int wordScore;

    public BoardSquare(int letterScore, int wordScore){
        t = null;
        this.letterScore = letterScore;
        this.wordScore = wordScore;
    }

    public void placeTile(Tile t){
        this.t = t;
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