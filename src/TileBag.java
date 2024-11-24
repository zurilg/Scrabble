/**
 * TileBag class.
 *
 * @author Zuri Lane-Griffore (101241678)
 * @author Mohammad Ahmadi (101267874)
 * @author Abdul Aziz Al-Sibakhi (101246056)
 * @author Redah Eliwa (101273466)
 *
 * @version 11-12-2024
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
public class TileBag {
    private final ArrayList<Tile> bag;
    /**
     * TileBag constructor method. Fills the bag with the 100 game tiles.
     */
    public TileBag(){
        bag = new ArrayList<>();
        fillBag();
    }

    /**
     * Remove a random tile from the bag and return it.
     *
     * @return A random tile from the bag.
     */
    public Tile popTile(){
        Random rand = new Random();
        int randIndex = rand.nextInt(bag.size());
        Tile pickedTile = bag.get(randIndex);
        bag.remove(randIndex);
        return pickedTile;
    }

    /**
     * @param t The tile to return to the bag
     */
    public void returnTile(Tile t){
        bag.add(t);
    }

    /**
     * Return the size of the bag.
     *
     * @return Number of tiles remaining in the bag.
     */
    public int size(){
        return bag.size();
    }

    /**
     * Fill the bag with 100 game tiles using info from xml file.
     */
    private void fillBag(){
        try {
            File xmlFile = new File("./TileBagInfo.xml"); // Open the XML that holds the info for all 100 tiles
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder(); // Create a document builder
            Document doc = docBuilder.parse(xmlFile); // Parse through XML file content and save as a document object
            doc.getDocumentElement().normalize(); // Normalize so it can be structured as nodes
            NodeList nodeList = doc.getElementsByTagName("tile"); // Create node list using normalized doc content

            // Iterate through each element node and add its attributes to the board squares
            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i); // Get node at index i
                // Check object type before accessing attributes
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) node;
                    String character = e.getAttribute("t"); // Get the tile character
                    int points = Integer.parseInt(e.getElementsByTagName("points").item(0).getTextContent()); // Get word score
                    int frequency = Integer.parseInt(e.getElementsByTagName("frequency").item(0).getTextContent()); // Get letter score
                    // Add specified number of tiles to the bag
                    for(int j = 0; j < frequency; j++) bag.add(new Tile(character, points));
                }
            }
        }
        catch(Exception e){ ScrabbleModelViewFrame.fileReadError("Error occurred when trying to read from 'TileBagInfo.xml'."); }
    }

}