public class Player {
    private String name;
    private int score;

    /**
     * Cosntructor method for player
     * initializes name, score, and tiles
     * @param name
     */
    public Player(String name){
        this.name = name;
        score = 0;
    }


    /**
     * Getter method for player score
     * @return score
     */
    public int getScore(){
        return score;
    }
}
