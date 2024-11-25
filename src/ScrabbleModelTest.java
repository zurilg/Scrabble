import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Tests the ScrabbleModel class.
 * Tests word placement, turn scoring (with premium squares), and blank tile usage.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-12-2024
 */
public class ScrabbleModelTest {

    private ScrabbleModel model;
    /**
     * Initializes the Scrabble game before each test
     */
    @BeforeEach
    public void setUp() {
        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.add("Player1");
        playerNames.add("Player2");
        model = new ScrabbleModel();
        model.initPlayers(playerNames, 0);
    }

    /**
     * Tests that if a valid word is played on the first turn, the player that played gets the corresponding points.
     */
    @Test
    public void testFirstWordPlacement() {
        // Create tiles to be used.
        Tile tileB = new Tile("B", 3);
        Tile tileA = new Tile("A", 1);
        Tile tileT = new Tile("T", 1);
        // Get the current player (can't just play with player1). Must follow player order.
        Player currentPlayer = model.getCurrentPlayer();
        // Add necessary tiles to current player's tile holder
        currentPlayer.addTile(0, tileB);
        currentPlayer.addTile(1, tileA);
        currentPlayer.addTile(2, tileT);
        // Place "BAT" horizontally at row 7
        model.getBoard().getSqAtIndex(7, 7).placeTile(currentPlayer.popTile(0)); // B
        model.getBoard().getSqAtIndex(7, 8).placeTile(currentPlayer.popTile(1)); // A
        model.getBoard().getSqAtIndex(7, 9).placeTile(currentPlayer.popTile(2)); // T
        // Initialize the coordinates played by the player. This info is provided by view usually.
        HashSet<int[]> playCoordinates = new HashSet<>();
        playCoordinates.add(new int[]{7, 7});
        playCoordinates.add(new int[]{7, 8});
        playCoordinates.add(new int[]{7, 9});
        // Get players initial score, validate word, get their expected score.
        int initialScore = currentPlayer.getScore();
        int score = model.validateAndScoreBoard(playCoordinates); // Validate and score word played
        model.validTurn(score);
        int expectedScore = (initialScore + 5) * 2; // "BAT" * 2 (double word)

        assertEquals(expectedScore, currentPlayer.getScore(), "The score calculation for multiple words formed in one move should be correct.");
    }

    /**
     * Tests that if a valid word is played on the first turn, the player that played gets the corresponding points.
     */
    @Test
    public void testBlankTile() {
        // Create tiles to be used.
        Tile tileB = new Tile("B", 3);
        Tile tileBlank = new Tile(" ", 0);
        Tile tileT = new Tile("T", 1);
        // Get the current player (can't just play with player1). Must follow player order.
        Player currentPlayer = model.getCurrentPlayer();
        // Add necessary tiles to current player's tile holder
        currentPlayer.addTile(0, tileB);
        currentPlayer.addTile(1, tileBlank);
        currentPlayer.addTile(2, tileT);

        // Set blank tile character
        tileBlank.setChar("A");
        // Place "BAT" horizontally at row 7
        model.getBoard().getSqAtIndex(7, 7).placeTile(currentPlayer.popTile(0)); // B
        model.getBoard().getSqAtIndex(7, 8).placeTile(currentPlayer.popTile(1)); // A
        model.getBoard().getSqAtIndex(7, 9).placeTile(currentPlayer.popTile(2)); // T
        // Initialize the coordinates played by the player. This info is provided by view usually.
        HashSet<int[]> playCoordinates = new HashSet<>();
        playCoordinates.add(new int[]{7, 7});
        playCoordinates.add(new int[]{7, 8});
        playCoordinates.add(new int[]{7, 9});
        // Get players initial score, validate word, get their expected score.
        int initialScore = currentPlayer.getScore();
        int score = model.validateAndScoreBoard(playCoordinates); // Validate and score word played
        model.validTurn(score);
        int expectedScore = (initialScore + 4) * 2; // "BAT" * 2 (double word)

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

        currentPlayer1.addTile(0, tileC);
        currentPlayer1.addTile(1, tileA);
        currentPlayer1.addTile(2, tileT);

        // Place "CAT" horizontally at row 7
        model.getBoard().getSqAtIndex(7, 7).placeTile(currentPlayer1.popTile(0)); // C
        model.getBoard().getSqAtIndex(7, 8).placeTile(currentPlayer1.popTile(1)); // A
        model.getBoard().getSqAtIndex(7, 9).placeTile(currentPlayer1.popTile(2)); // T

        HashSet<int[]> playCoordinates = new HashSet<>();
        playCoordinates.add(new int[]{7, 7});
        playCoordinates.add(new int[]{7, 8});
        playCoordinates.add(new int[]{7, 9});

        int initialScore1 = currentPlayer1.getScore();
        int score = model.validateAndScoreBoard(playCoordinates);
        model.validTurn(score);
        int expectedScore1 = (initialScore1 + 5) * 2; // "CAT" = 5 * double word

        // SECOND PLAYER TO PLAY PLAYS "HAT" VERTICALLY
        Player currentPlayer2 = model.getCurrentPlayer();

        currentPlayer2.addTile(0, tileH);
        currentPlayer2.addTile(1, tileT);

        // Place "HAT" horizontally at row 7
        model.getBoard().getSqAtIndex(6, 8).placeTile(currentPlayer2.popTile(0)); // H
        model.getBoard().getSqAtIndex(8, 8).placeTile(currentPlayer2.popTile(1)); // A

        playCoordinates.clear();
        playCoordinates.add(new int[]{6, 8});
        playCoordinates.add(new int[]{8, 8});

        int initialScore2 = currentPlayer2.getScore();
        score = model.validateAndScoreBoard(playCoordinates);
        model.validTurn(score);
        int expectedScore2 = initialScore2 + 5; // "HAT" = 6 - 1 = 5 since 'A' already placed

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

        currentPlayer.addTile(0, tileZ);
        currentPlayer.addTile(1, tileA);
        currentPlayer.addTile(2, tileT);

        // Place "ZAT" horizontally at row 7
        model.getBoard().getSqAtIndex(7, 7).placeTile(currentPlayer.popTile(0)); // B
        model.getBoard().getSqAtIndex(7, 8).placeTile(currentPlayer.popTile(1)); // A
        model.getBoard().getSqAtIndex(7, 9).placeTile(currentPlayer.popTile(2)); // T

        HashSet<int[]> playCoordinates = new HashSet<>();
        playCoordinates.add(new int[]{7, 7});
        playCoordinates.add(new int[]{7, 8});
        playCoordinates.add(new int[]{7, 9});

        model.validateAndScoreBoard(playCoordinates);

        assertEquals(0, currentPlayer.getScore(), "The score calculation for multiple words formed in one move should be correct.");
    }

    /**
     * Verifies exception is thrown when tile is placed out of bounds.
     */
    @Test
    public void testEdgeCasePlacementOutOfBounds(){
        Tile tileA = new Tile("A",1);
        assertThrows(IndexOutOfBoundsException.class,()->model.getBoard().getSqAtIndex(15,15).placeTile(tileA), "Placing a tile out of bounds should throw an exception.");
    }

    /**
     * Tests that a single letter randomly placed is counted as invalid and the current players score remains 0.
     */
    @Test
    public void testInvalidSingleLetterPlacement() {
        Tile tileZ = new Tile("Z", 3);

        Player currentPlayer = model.getCurrentPlayer();

        currentPlayer.addTile(0, tileZ);

        // Place "BAT" horizontally at row 7
        model.getBoard().getSqAtIndex(2, 3).placeTile(currentPlayer.popTile(0)); // B

        HashSet<int[]> playCoordinates = new HashSet<>();
        playCoordinates.add(new int[]{2, 3});

        model.validateAndScoreBoard(playCoordinates);

        assertEquals(0, currentPlayer.getScore(), "The score calculation for multiple words formed in one move should be correct.");
    }

    /**
     * Tests that a player playing a single letter in the middle as the first turn is invalid and their score remains 0.
     */
    @Test
    public void testInvalidSingleLetterCenterPlacement() {
        Tile tileE = new Tile("E", 3);

        Player currentPlayer = model.getCurrentPlayer();

        currentPlayer.addTile(0, tileE);

        // Place "E" in the center
        model.getBoard().getSqAtIndex(7, 7).placeTile(currentPlayer.popTile(0)); // E

        HashSet<int[]> playCoordinates = new HashSet<>();
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

        currentPlayer1.addTile(0, tileC);
        currentPlayer1.addTile(1, tileA);
        currentPlayer1.addTile(2, tileT);

        // Place "CAT" horizontally at row 7
        model.getBoard().getSqAtIndex(7, 7).placeTile(currentPlayer1.popTile(0));
        model.getBoard().getSqAtIndex(7, 8).placeTile(currentPlayer1.popTile(1));
        model.getBoard().getSqAtIndex(7, 9).placeTile(currentPlayer1.popTile(2));

        HashSet<int[]> playCoordinates = new HashSet<>();
        playCoordinates.add(new int[]{7, 7});
        playCoordinates.add(new int[]{7, 8});
        playCoordinates.add(new int[]{7, 9});

        int initialScore1 = currentPlayer1.getScore();
        int score = model.validateAndScoreBoard(playCoordinates);
        model.validTurn(score);
        int expectedScore1 = (initialScore1 + 5) * 2; // "CAT" = 5 * 2 double word = 10

        // SECOND PLAYER TO PLAY PLAYS "HAT"
        Player currentPlayer2 = model.getCurrentPlayer();

        currentPlayer2.addTile(0, tileH);
        currentPlayer2.addTile(1, tileT);

        // Place "BAT" horizontally at row 7
        model.getBoard().getSqAtIndex(6, 7).placeTile(currentPlayer2.popTile(0));
        model.getBoard().getSqAtIndex(8, 7).placeTile(currentPlayer2.popTile(1));

        playCoordinates.clear();
        playCoordinates.add(new int[]{6, 7});
        playCoordinates.add(new int[]{8, 7});

        score = model.validateAndScoreBoard(playCoordinates);
        model.validTurn(score);

        assertTrue((currentPlayer1.getScore() == expectedScore1) && (currentPlayer2.getScore() == 0), "The score calculation for multiple words formed in one move should be correct.");
    }

    /**
     * Tests that submitting an empty board is invalid and results in a score of 0.
     */
    @Test
    public void testSubmitEmptyBoard() {
        Player currentPlayer = model.getCurrentPlayer();

        HashSet<int[]> playCoordinates = new HashSet<>();

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

        currentPlayer1.addTile(0, tileC);
        currentPlayer1.addTile(1, tileA);
        currentPlayer1.addTile(2, tileT);

        // Place "BAT" horizontally at row 7
        model.getBoard().getSqAtIndex(7, 7).placeTile(currentPlayer1.popTile(0));
        model.getBoard().getSqAtIndex(7, 8).placeTile(currentPlayer1.popTile(1));
        model.getBoard().getSqAtIndex(7, 9).placeTile(currentPlayer1.popTile(2));

        HashSet<int[]> playCoordinates = new HashSet<>();
        playCoordinates.add(new int[]{7, 7});
        playCoordinates.add(new int[]{7, 8});
        playCoordinates.add(new int[]{7, 9});

        int score = model.validateAndScoreBoard(playCoordinates);
        model.validTurn(score);
        int expectedScore1 = currentPlayer1.getScore(); // "CAT" = 5 * 2 double word

        // SECOND PLAYER TO PLAY PLAYS "HAT"
        Player currentPlayer2 = model.getCurrentPlayer();

        currentPlayer2.addTile(0, tileH);
        currentPlayer2.addTile(1, tileT);

        // Place "HAT" vertically at column 7
        model.getBoard().getSqAtIndex(6, 8).placeTile(currentPlayer2.popTile(0));
        model.getBoard().getSqAtIndex(8, 8).placeTile(currentPlayer2.popTile(1));

        playCoordinates.clear();
        playCoordinates.add(new int[]{6, 8});
        playCoordinates.add(new int[]{8, 8});

        int initialScore2 = currentPlayer2.getScore();
        score = model.validateAndScoreBoard(playCoordinates);
        model.validTurn(score);
        int expectedScore2 = initialScore2 + 5; // "HAT" = 6 - 1 =  since 'A' is already placed

        // First player plays again, this time an invalid play
        currentPlayer1.addTile(0, tileC);
        currentPlayer1.addTile(1, tileA);
        currentPlayer1.addTile(2, tileT);

        model.getBoard().getSqAtIndex(3, 6).placeTile(currentPlayer1.popTile(0));
        model.getBoard().getSqAtIndex(3, 7).placeTile(currentPlayer1.popTile(1));
        model.getBoard().getSqAtIndex(3, 8).placeTile(currentPlayer1.popTile(2));

        playCoordinates.clear();
        playCoordinates.add(new int[]{3, 6});
        playCoordinates.add(new int[]{3, 7});
        playCoordinates.add(new int[]{3, 8});

        score = model.validateAndScoreBoard(playCoordinates);
        model.validTurn(score);

        assertTrue((currentPlayer1.getScore() == expectedScore1) && (currentPlayer2.getScore() == expectedScore2), "The score calculation for multiple words formed in one move should be correct.");
    }

}