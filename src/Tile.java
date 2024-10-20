public class Tile {
    private String character;
    private int value;

    public Tile(String character, int value){
        this.character = character;
        this.value = value;
    }

    public String getChar(){
        return this.character;
    }
    public int getValue(){
        return this.value;
    }
}
