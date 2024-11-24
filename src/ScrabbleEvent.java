import java.util.EventObject;

public class ScrabbleEvent{
    public static class TilePlacement extends EventObject{
        private final int r, c, selectedTile;
        public TilePlacement(ScrabbleModel model, int r, int c, int selectedTile){
            super(model);
            this.r = r;
            this.c = c;
            this.selectedTile = selectedTile;
        }

        public int getR(){ return r; }
        public int getC(){ return c; }
        public int getSelectedTile(){ return selectedTile; }
    }
}
