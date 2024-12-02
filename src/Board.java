import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;

/**
 * Board class.
 * Model class that represents the Scrabble board
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-24-2024
 */
public class Board implements Serializable {
    public static final int BOARD_SIZE = 15;
    private final BoardSquare[][] board;
    private final BoardSquare[][] prevState;
    /**
     * This is the board constructor. It initializes every board square to empty.
     */
    public Board(){
        board = new BoardSquare[BOARD_SIZE][BOARD_SIZE];
        prevState = new BoardSquare[BOARD_SIZE][BOARD_SIZE];
        // Initialize all squares as empty
        for(int r = 0; r < BOARD_SIZE; r+=1){
            for(int c = 0; c < BOARD_SIZE; c+=1){
                board[r][c] = new BoardSquare(1,1); // Empty board square
                prevState[r][c] = new BoardSquare(1,1);
            }
        }
    }
    /**
     * Accessor method to get the board square at a specified index.
     *
     * @param r Row coordinate.
     * @param c Column coordinate.
     * @return Board square at specified coordinates.
     */
    public BoardSquare getSqAtIndex(int r, int c){ return board[r][c]; }
    /**
     * Saves the current state of the board for future reset purposes.
     */
    public void saveState(){
        // Iterate through all saved board squares and set their states equal to the current board.
        for(int r = 0; r<=14; r+=1)
            for(int c = 0; c<=14; c+=1)
                prevState[r][c].placeTile(board[r][c].getTile());
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
     * Reads premium squares from an XML file and assigns them to the board squares.
     */
    public void initBoard(String bonusType){
        try {
            File xmlFile = new File(String.format("./GameAssets/BoardLayouts/%s.xml", bonusType)); // Open the XML that holds the board square bonus info
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
                    prevState[Integer.parseInt(coordinates[0])][Integer.parseInt(coordinates[1])] = new BoardSquare(letterScore, wordScore);
                }
            }
        }
        catch(Exception e){ ScrabbleModelViewFrame.fileReadError("Error occurred when trying to read from 'BoardSquareInfo.xml'."); }
    }
}
