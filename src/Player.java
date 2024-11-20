import java.util.ArrayList;

public class Player {
    public static final int TILE_HOLDER_SIZE = 7;

    private String name;
    private int score;
    private Tile[] tileHolder;
    private Tile[] prevTiles;

    public Player(String name){
        this.name = name;
        score = 0;
        tileHolder = new Tile[TILE_HOLDER_SIZE];
        prevTiles = new Tile[TILE_HOLDER_SIZE];
        for(int i = 0; i < TILE_HOLDER_SIZE; i++) {
            tileHolder[i] = null; // Initialize tile holders as empty initially.
            prevTiles[i] = null;
        }

    }

    public Tile getTile(int index){
        if(index >=0 && index < TILE_HOLDER_SIZE) return tileHolder[index]; // If index range is correct, return tile at that index in tile holder.
        return null; // Out of bounds.
    }

    public Tile popTile(int index){
        Tile t = null;
        if(index >=0 && index <= TILE_HOLDER_SIZE){
            t = tileHolder[index];
            tileHolder[index] = null;
        }
        return t; // Return null if that tile holder slot is empty or range is invalid. Otherwise, return tile at that index.
    }

    public void addTile(int index, Tile t){ tileHolder[index] = t; }

    public void setPrevTiles(){ System.arraycopy(tileHolder, 0, prevTiles, 0, Player.TILE_HOLDER_SIZE); }

    public void resetTileHolder(){ System.arraycopy(prevTiles, 0, tileHolder, 0, Player.TILE_HOLDER_SIZE); }

    public void addToScore(int s){ score += s; }

    public String getName(){ return name; }

    public int getScore(){ return score; }

    public int numTiles(){
        int num = 0;
        for(Tile t : tileHolder) if(t != null) num++;
        return num;
    }
}
