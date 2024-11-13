/**
 * Board class.
 *
 * Model class that represents the Scrabble board
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-12-2024
 */

// Imports to be able to read xml file
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
public class Board {
    private BoardSquare[][] board; // Stores the current state of the board
    private BoardSquare[][] prevState; // Stores the previous state of the board

    /**
     * This is the board constructor. It initializes every board square to 0 score.
     */
    public Board(){
        board = new BoardSquare[ScrabbleModel.BOARD_SIZE][ScrabbleModel.BOARD_SIZE]; // Array of board squares (since static)
        prevState = new BoardSquare[ScrabbleModel.BOARD_SIZE][ScrabbleModel.BOARD_SIZE];
        // Initialize all squares as empty
        for(int i = 0; i < ScrabbleModel.BOARD_SIZE; i+=1){
            for(int j = 0; j < ScrabbleModel.BOARD_SIZE; j+=1){
                board[i][j] = new BoardSquare(0,0); // Empty board square
                prevState[i][j] = new BoardSquare(0,0);
            }
        }
        initBoard(); // Correct all word and letter scores using data from XML
    }
    /**
     * Reads premium squares from an XML file and assigns them to the board squares.
     */
    private void initBoard(){
        try {
            File xmlFile = new File("./BoardSquareInfo.xml"); // Open the XML that holds the board square bonus info
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder(); // Create a document builder
            Document doc = docBuilder.parse(xmlFile); // Parse through XML file content and save as a document object
            doc.getDocumentElement().normalize(); // Normalize so it can be structured as nodes
            NodeList nodeList = doc.getElementsByTagName("coordinate"); // Create node list using normalized doc content

            // Iterate through each element node and add its attributes to the board squares
            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i); // Get node at index i
                // Check object type before accessing attributes
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) node;
                    String[] coordinates = e.getAttribute("rc").split(","); // Get the coordinates of board square with bonus
                    int wordScore = Integer.parseInt(e.getElementsByTagName("wordScore").item(0).getTextContent()); // Get word score
                    int letterScore = Integer.parseInt(e.getElementsByTagName("letterScore").item(0).getTextContent()); // Get letter score
                    // Recreate each empty board square with the attributes obtained from XML file.
                    board[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] = new BoardSquare(letterScore, wordScore);
                }
            }
        }
        catch(Exception e){
            System.out.println("This error shouldn't occur. XML error"); // TODO: Temporary. Remove/replace
            e.printStackTrace();
        }
    }


    /**
     * Saves the current state of the board for future reset purposes.
     */
    public void setPrevState(){
        // Iterate through all saved board squares and set their states equal to the current board.
        for(int i = 0; i<=14; i+=1)
            for(int j = 0; j<=14; j+=1)
                prevState[i][j].placeTile(board[i][j].getTile());
    }

    /**
     * Resets board back to state previously saved.
     */
    public void reset(){
        // Iterate through all board squares and set their states equal to the previously stored states.
        for(int i = 0; i<=14; i+=1)
            for(int j = 0; j<=14; j+=1)
                board[i][j].placeTile(prevState[i][j].getTile());
    }

    /**
     * Returns the tile letter on the corresponding board square provided coordinates.
     *
     * @param r The row index
     * @param c The column index
     * @return The letter (as a string) at the specified board coordinate.
     */
    public String getLetterAtIndex(int r, int c){
        return board[r][c].getLetter();
    }

    /**
     * Returns the BoardSquare object at the specified index
     * @param r Row where the board square desired is
     * @param c Column where the board square desired is
     * @return the BoardSquare object specified by the indexes
     */
    public BoardSquare getSqAtIndex(int r, int c){ return board[r][c]; }

    /**
     * Places the provided game tile on the specified coordinates.
     *
     * @param t The tile to place on the specified coordinate.
     * @param r The row index
     * @param c The column index
     */
    public void placeTile(Tile t, int r, int c){
        board[r][c].placeTile(t);
    }
}
