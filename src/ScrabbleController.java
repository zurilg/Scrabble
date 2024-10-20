import java.util.ArrayList;
public class ScrabbleController {
    private ScrabbleView view;
    private TileBag tiles;
    private ArrayList<Player> players;

    public ScrabbleController(){
        // Initialize view
        view = new ScrabbleView();
        // Initialize Models
        tiles = new TileBag();
        players = new ArrayList<>();
    }

    public void addPlayers(){
        int numPlayers = view.getNumPlayers(); // Get number of players to initialize players
        for(int i = 1; i<= numPlayers; i++){
            players.add(new Player(view.getPlayerName(i)));
        }
    }

    public void determinePlayerOrder(){
        // Have each player draw a tile
        for(Player p : players){
            p.getTile(tiles.popTile());
            //System.out.println(p.getName());
            //System.out.println(p.showLastTile().getChar());
        }

        //
        boolean redraw = false;
        for(int i = 0; i < players.size()-1; i++){
            for(int j = 0; j < players.size() - i-1; j++){
                if(players.get(j).showLastTile().getChar().charAt(0) > players.get(j + 1).showLastTile().getChar().charAt(0)){
                    Player temp = players.get(j);
                    players.set(j, players.get(j+1));
                    players.set(j+1, temp);
                }
                else if(players.get(j).showLastTile().getChar().charAt(0) == players.get(j + 1).showLastTile().getChar().charAt(0)){
                    redraw = true;
                }
            }
        }
        for(Player p : players){
            Tile t = p.popLastTile();
            while(t!=null){
                tiles.returnTile(t);
                t = p.popLastTile();

            }
        }
    }

    public void distributeTiles(){
        for(Player p : players){
            for(int i = 7 - p.numTiles();i>0;i--){
                p.getTile(tiles.popTile());
            }
        }
    }
}
