import java.util.ArrayList;
import java.util.Random;
public class TileBag {
    private ArrayList<Tile> bag;
    public TileBag(){
        bag = new ArrayList<>();
        fillBag();
    }

    private void fillBag(){
        // Indexes: 0 (A) - 26 (Blank Space)             25 is (Z)
        String[] tileChars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", " "};
        int [] tileValues = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10, 0};
        int[] numTiles = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 2};

        int alphaIndex = 0;
        for(int i : numTiles){
            while(i > 0){
                bag.add(new Tile(tileChars[alphaIndex], tileValues[alphaIndex]));
                i--;
            }
            alphaIndex++;
        }
    }

    public Tile popTile(){
        Random rand = new Random();
        int randIndex = rand.nextInt(bag.size());
        Tile pickedTile = bag.get(randIndex);
        bag.remove(randIndex);
        return pickedTile;
    }

    public void returnTile(Tile tile){
        bag.add(tile);
    }

    public int returnSize(){
        return bag.size();
    }
}
