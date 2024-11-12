import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class ScrabbleModelTest {

    private ScrabbleModel model;
    private Player player1, player2;

    @BeforeEach
    public void setUp() {
        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.add("Player1");
        playerNames.add("Player2");
        model = new ScrabbleModel(playerNames);
        player1 = model.getPlayers().get(0);
        player2 = model.getPlayers().get(1);
    }

    @Test
    public void testFirstWordPlacement() {
        Tile tileB = new Tile("B", 3);
        Tile tileA = new Tile("A", 1);
        Tile tileT = new Tile("T", 1);

        Player currentPlayer = model.getCurrentPlayer();

        currentPlayer.addTileToHolder(tileB);
        currentPlayer.addTileToHolder(tileA);
        currentPlayer.addTileToHolder(tileT);

        // Place "BAT" horizontally at row 7
        model.getBoard().placeTile(tileB, 7, 7);
        model.getBoard().placeTile(tileA, 7, 8);
        model.getBoard().placeTile(tileT, 7, 9);

        // Must set tiles as used after playing them so we know the player played
        currentPlayer.setTileAsUsed(0);
        currentPlayer.setTileAsUsed(1);
        currentPlayer.setTileAsUsed(2);

        ArrayList<int[]> playCoordinates = new ArrayList<>();
        playCoordinates.add(new int[]{7, 7});
        playCoordinates.add(new int[]{7, 8});
        playCoordinates.add(new int[]{7, 9});

        int initialScore = currentPlayer.getScore();
        model.validateAndScoreBoard(playCoordinates);
        int expectedScore = initialScore + 5; // "BAT"

        assertEquals(expectedScore, currentPlayer.getScore(), "The score calculation for multiple words formed in one move should be correct.");
    }

    @Test
    public void testTwoValidWordPlacements() {
        Tile tileC = new Tile("C", 3);
        Tile tileH = new Tile("H", 1);
        Tile tileA = new Tile("A", 1);
        Tile tileT = new Tile("T", 1);

        // FIRST PLAYER PLAYS CAT
        Player currentPlayer1 = model.getCurrentPlayer();

        currentPlayer1.addTileToHolder(tileC);
        currentPlayer1.addTileToHolder(tileA);
        currentPlayer1.addTileToHolder(tileT);

        // Place "BAT" horizontally at row 7
        model.getBoard().placeTile(tileC, 7, 6);
        model.getBoard().placeTile(tileA, 7, 7);
        model.getBoard().placeTile(tileT, 7, 8);

        // Must set tiles as used after playing them so we know the player played
        currentPlayer1.setTileAsUsed(0);
        currentPlayer1.setTileAsUsed(1);
        currentPlayer1.setTileAsUsed(2);

        ArrayList<int[]> playCoordinates = new ArrayList<>();
        playCoordinates.add(new int[]{7, 7});
        playCoordinates.add(new int[]{7, 8});
        playCoordinates.add(new int[]{7, 9});

        int initialScore1 = currentPlayer1.getScore();
        model.validateAndScoreBoard(playCoordinates);
        int expectedScore1 = initialScore1 + 5; // "CAT" = 5

        // SECOND PLAYER TO PLAY PLAYS "HAT"
        Player currentPlayer2 = model.getCurrentPlayer();

        currentPlayer2.addTileToHolder(tileH);
        currentPlayer2.addTileToHolder(tileT);

        // Place "BAT" horizontally at row 7
        model.getBoard().placeTile(tileH, 6, 7);
        model.getBoard().placeTile(tileT, 8, 7);

        // Must set tiles as used after playing them so we know the player played
        currentPlayer2.setTileAsUsed(0);
        currentPlayer2.setTileAsUsed(1);

        playCoordinates.clear();
        playCoordinates.add(new int[]{6, 7});
        playCoordinates.add(new int[]{8, 7});

        int initialScore2 = currentPlayer2.getScore();
        model.validateAndScoreBoard(playCoordinates);
        int expectedScore2 = initialScore2 + 6; // "HAT" = 6

        assertTrue((currentPlayer1.getScore() == expectedScore1) && (currentPlayer2.getScore() == expectedScore2), "The score calculation for multiple words formed in one move should be correct.");
    }

}
