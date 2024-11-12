import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

/**
 * Tests the ScrabbleModel class.
 * Simultaneously tests word placement and word scoring as a score is produced depending on word placement.
 */
public class ScrabbleModelTest {

    private ScrabbleModel model;
    private Player player1, player2;

    /**
     * Initializes the Scrabble game before each test
     */
    @BeforeEach
    public void setUp() {
        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.add("Player1");
        playerNames.add("Player2");
        model = new ScrabbleModel(playerNames);
        player1 = model.getPlayers().get(0);
        player2 = model.getPlayers().get(1);
    }

    /**
     * Tests that if a valid word is played on the first turn, the player that played gets the corresponding points.
     */
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

    /**
     * Test two valid plays in a row. First player plays a valid word, then next player plays a valid word. The players scores
     * should change according to the words they placed.
     */
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

    /**
     * Tests that if a player plays an invalid word on the first turn that the player's score remains 0.
     */
    @Test
    public void testInvalidFirstWordPlacement() {
        Tile tileZ = new Tile("Z", 3);
        Tile tileA = new Tile("A", 1);
        Tile tileT = new Tile("T", 1);

        Player currentPlayer = model.getCurrentPlayer();

        currentPlayer.addTileToHolder(tileZ);
        currentPlayer.addTileToHolder(tileA);
        currentPlayer.addTileToHolder(tileT);

        // Place "BAT" horizontally at row 7
        model.getBoard().placeTile(tileZ, 7, 7);
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

        assertEquals(0, currentPlayer.getScore(), "The score calculation for multiple words formed in one move should be correct.");
    }

    /**
     * Verifies exception is thrown when tile is placed out of bounds.
     */
    @Test
    public void testEdgeCasePlacementOutOfBounds(){
        Tile tileA = new Tile("A",1);
        assertThrows(IndexOutOfBoundsException.class,()->model.getBoard().placeTile(tileA,15,15), "Placing a tile out of bounds should throw an exception.");
    }

    /**
     * Tests that a single letter randomly placed is counted as invalid and the current players score remains 0.
     */
    @Test
    public void testInvalidSingleLetterPlacement() {
        Tile tileZ = new Tile("Z", 3);

        Player currentPlayer = model.getCurrentPlayer();

        currentPlayer.addTileToHolder(tileZ);

        // Place "BAT" horizontally at row 7
        model.getBoard().placeTile(tileZ, 2, 3);

        // Must set tiles as used after playing them so we know the player played
        currentPlayer.setTileAsUsed(0);

        ArrayList<int[]> playCoordinates = new ArrayList<>();
        playCoordinates.add(new int[]{2, 3});

        model.validateAndScoreBoard(playCoordinates);

        assertEquals(0, currentPlayer.getScore(), "The score calculation for multiple words formed in one move should be correct.");
    }

    /**
     * Tests that a player playing a single letter in the middle as the first turn is invalid and their score remains 0.
     */
    @Test
    public void testInvalidSingleLetterCenterPlacement() {
        Tile tileZ = new Tile("E", 3);

        Player currentPlayer = model.getCurrentPlayer();

        currentPlayer.addTileToHolder(tileZ);

        // Place "BAT" horizontally at row 7
        model.getBoard().placeTile(tileZ, 7, 7);

        // Must set tiles as used after playing them so we know the player played
        currentPlayer.setTileAsUsed(0);

        ArrayList<int[]> playCoordinates = new ArrayList<>();
        playCoordinates.add(new int[]{7, 7});

        model.validateAndScoreBoard(playCoordinates);

        assertEquals(0, currentPlayer.getScore(), "The score calculation for multiple words formed in one move should be correct.");
    }

    /**
     * Tests that the first player (in player order determined by the model) can play a valid word and receive the appropriate
     * score, but when the second player to play plays an invalid word that they still have a score of 0.
     */
    @Test
    public void testValidFirstInvalidSecond() {
        Tile tileC = new Tile("C", 3);
        Tile tileH = new Tile("Z", 1);
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

        model.validateAndScoreBoard(playCoordinates);

        assertTrue((currentPlayer1.getScore() == expectedScore1) && (currentPlayer2.getScore() == 0), "The score calculation for multiple words formed in one move should be correct.");
    }

    /**
     * Tests that submitting an empty board is invalid and results in a score of 0.
     */
    @Test
    public void testSubmitEmptyBoard() {
        Player currentPlayer = model.getCurrentPlayer();

        ArrayList<int[]> playCoordinates = new ArrayList<>();

        model.validateAndScoreBoard(playCoordinates);

        assertEquals(0, currentPlayer.getScore(), "The score calculation for multiple words formed in one move should be correct.");
    }

    /**
     * Test two valid plays in a row, then third play is invalid. First player plays a valid word. Then, on their second turn,
     * they place an invalid word. This means that their score should remain the same after their first play.
     */
    @Test
    public void testTwoValidOneInvalid() {
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
        model.getBoard().placeTile(tileC, 7, 7);
        model.getBoard().placeTile(tileA, 7, 8);
        model.getBoard().placeTile(tileT, 7, 9);

        // Must set tiles as used after playing them so we know the player played
        currentPlayer1.setTileAsUsed(0);
        currentPlayer1.setTileAsUsed(1);
        currentPlayer1.setTileAsUsed(2);

        ArrayList<int[]> playCoordinates = new ArrayList<>();
        playCoordinates.add(new int[]{7, 7});
        playCoordinates.add(new int[]{7, 8});
        playCoordinates.add(new int[]{7, 9});

        model.validateAndScoreBoard(playCoordinates);
        int expectedScore1 = currentPlayer1.getScore(); // "CAT" = 5

        // SECOND PLAYER TO PLAY PLAYS "HAT"
        Player currentPlayer2 = model.getCurrentPlayer();

        currentPlayer2.addTileToHolder(tileH);
        currentPlayer2.addTileToHolder(tileT);

        // Place "HAT" vertically at column 7
        model.getBoard().placeTile(tileH, 6, 8);
        model.getBoard().placeTile(tileT, 8, 8);

        // Must set tiles as used after playing them so we know the player played
        currentPlayer2.setTileAsUsed(0);
        currentPlayer2.setTileAsUsed(1);

        playCoordinates.clear();
        playCoordinates.add(new int[]{6, 7});
        playCoordinates.add(new int[]{8, 7});

        int initialScore2 = currentPlayer2.getScore();
        model.validateAndScoreBoard(playCoordinates);
        int expectedScore2 = initialScore2 + 6; // "HAT" = 6

        // First player plays again, this time an invalid play
        currentPlayer1.addTileToHolder(tileC);
        currentPlayer1.addTileToHolder(tileA);
        currentPlayer1.addTileToHolder(tileT);

        // Place "BAT" horizontally at row 7
        model.getBoard().placeTile(tileT, 3, 6);
        model.getBoard().placeTile(tileC, 3, 7);
        model.getBoard().placeTile(tileA, 3, 8);

        // Must set tiles as used after playing them so we know the player played
        currentPlayer1.setTileAsUsed(3);
        currentPlayer1.setTileAsUsed(4);
        currentPlayer1.setTileAsUsed(5);

        playCoordinates.clear();
        playCoordinates.add(new int[]{3, 6});
        playCoordinates.add(new int[]{3, 7});
        playCoordinates.add(new int[]{3, 8});

        model.validateAndScoreBoard(playCoordinates);

        assertTrue((currentPlayer1.getScore() == expectedScore1) && (currentPlayer2.getScore() == expectedScore2), "The score calculation for multiple words formed in one move should be correct.");
    }

}