package cellsociety.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLParser extends Parser {
    private int width;
    private int height;
    private String title;
    private int rows;
    private int columns;
    private String simType;
    private String description;
    private String author;
    private String[] initialStates;
    private Map<String, String> simVarsMap;
    private Map<String, GridPattern> patterns;
    private boolean hasRandomStates;
    private Map<String, Double> stateProportions;

  private String edgeType;
  private String neighborhoodType;
  private String cellShape;


  /**
   * Inner class to store pattern information.
   * A GridPattern represents a subsection of the grid with its own state values,
   * starting at a specified row and column with given dimensions.
   */
    public class GridPattern {
        private int startRow;
        private int startCol;
        private String[] patternStates;
        private int patternRows;
        private int patternCols;

      /**
       * Constructs a GridPattern.
       *
       * @param startRow      the starting row index of the pattern in the grid
       * @param startCol      the starting column index of the pattern in the grid
       * @param patternStates an array of state symbols for the pattern
       * @param patternRows   the number of rows in the pattern
       * @param patternCols   the number of columns in the pattern
       */
        public GridPattern(int startRow, int startCol, String[] patternStates, int patternRows, int patternCols) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.patternStates = patternStates;
            this.patternRows = patternRows;
            this.patternCols = patternCols;
        }

        public int getStartRow() { return startRow; }
        public int getStartCol() { return startCol; }
        public String[] getPatternStates() { return patternStates; }
        public int getPatternRows() { return patternRows; }
        public int getPatternCols() { return patternCols; }
    }

  /**
   * Constructs an XMLParser and parses the provided XML file.
   *
   * @param file the XML file to be parsed
   * @throws InvalidXMLConfigurationException if the XML file is not valid or parsing fails
   */
    public XMLParser(File file) throws InvalidXMLConfigurationException {
        patterns = new HashMap<>();
        stateProportions = new HashMap<>();
        hasRandomStates = false;

        try {
            if (!isXMLFile(file)) {
                throw new IllegalArgumentException("File is not an XML file: " + file.getName());
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            parseDocument(document);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new InvalidXMLConfigurationException("XML Parsing Error: " + e.getMessage());
        }
    }


  /**
   * Checks whether the provided file has an XML file extension.
   *
   * @param file the file to check
   * @return true if the file name ends with ".xml", false otherwise
   */
    private boolean isXMLFile(File file) {
        String fileName = file.getName();
        return fileName.endsWith(".xml");
    }

  /**
   * Parses the entire XML document.
   * <p>
   * This method extracts display settings, simulation parameters, initialization data,
   * and optional pattern sections from the XML document. If any patterns are defined,
   * they are applied to the initial states.
   * </p>
   *
   * @param document the XML Document to parse
   * @throws InvalidXMLConfigurationException if any required elements or attributes are missing or invalid
   */
    private void parseDocument(Document document) throws InvalidXMLConfigurationException {
        parseDisplay(document);
        parseSimulation(document);

        // Parse init section (will handle both random and explicit state lists)
        parseInitSection(document);

        // Parse pattern sections if present
        parsePatterns(document);

        // If we have patterns, apply them to the initial states
        if (!patterns.isEmpty()) {
            applyPatterns();
        }
    }

  /**
   * Parses display-related information from the XML document.
   * <p>
   * Extracts attributes such as width, height, title, author, grid dimensions,
   * description, and optional grid attributes like edgeType, neighborhoodType, and cellShape.
   * </p>
   *
   * @param document the XML Document to parse
   * @throws InvalidXMLConfigurationException if any required display elements or attributes are missing or invalid
   */
    private void parseDisplay(Document document) throws InvalidXMLConfigurationException {
        try {
            // CELL-27: Input Missing Parameters
            Element display = getRequiredElement(document, "display");

            this.width = getRequiredIntAttribute(display, "width");
            if (this.width <= 0) {
                throw new InvalidXMLConfigurationException("Width must be a positive integer");
            }

            this.height = getRequiredIntAttribute(display, "height");
            if (this.height <= 0) {
                throw new InvalidXMLConfigurationException("Height must be a positive integer");
            }

            this.title = getRequiredAttribute(display, "title");
            if (this.title.trim().isEmpty()) {
                throw new InvalidXMLConfigurationException("Title cannot be empty");
            }

            this.author = getRequiredAttribute(display, "author");
            if (this.author.trim().isEmpty()) {
                throw new InvalidXMLConfigurationException("Author cannot be empty");
            }

            Element grid = getRequiredElement(display, "grid");
            this.rows = getRequiredIntAttribute(grid, "rows");
            if (this.rows <= 0) {
                throw new InvalidXMLConfigurationException("Rows must be a positive integer");
            }

            this.columns = getRequiredIntAttribute(grid, "columns");
            if (this.columns <= 0) {
                throw new InvalidXMLConfigurationException("Columns must be a positive integer");
            }

            Element descElement = getRequiredElement(display, "description");
            this.description = getRequiredAttribute(descElement, "text");
            if (this.description.trim().isEmpty()) {
                throw new InvalidXMLConfigurationException("Description cannot be empty");
            }
          this.edgeType = grid.getAttribute("edgeType");
          this.neighborhoodType = grid.getAttribute("neighborhoodType");
          this.cellShape = grid.getAttribute("cellShape");
        } catch (NullPointerException e) {
            throw new InvalidXMLConfigurationException("Missing required display elements");
        }
    }

  /**
   * Parses simulation-related information from the XML document.
   * <p>
   * Extracts simulation type and simulation variables (simvars). Validates the simulation type
   * and reads all attributes from the simvars element.
   * </p>
   *
   * @param document the XML Document to parse
   * @throws InvalidXMLConfigurationException if required simulation elements or attributes are missing or invalid
   */
    private void parseSimulation(Document document) throws InvalidXMLConfigurationException {
        Element sim = getRequiredElement(document, "sim");
        this.simType = getRequiredAttribute(sim, "type");

        // Validate simulation type
        if (!isValidSimulationType(simType)) {
            throw new IllegalArgumentException("Invalid simulation type: " + simType);
        }

        Element simvars = getRequiredElement(document, "simvars");
        simVarsMap = new HashMap<>();
        NamedNodeMap attributes = simvars.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attr = attributes.item(i);
            String value = attr.getNodeValue();
            if (attr.getNodeName().equals("probCatch") || attr.getNodeName().equals("probGrow")) {
                validateProbability(value);
            }
            simVarsMap.put(attr.getNodeName(), value);
        }
    }

  /**
   * Parses the initialization section from the XML document.
   * <p>
   * Determines whether cell states should be generated randomly or explicitly from a state list.
   * For random initialization, parses proportions (if provided) or random nodes; for explicit initialization,
   * verifies the provided state list.
   * </p>
   *
   * @param document the XML Document to parse
   * @throws InvalidXMLConfigurationException if required initialization attributes are missing or invalid
   */
    private void parseInitSection(Document document) throws InvalidXMLConfigurationException {
        Element initElement = getRequiredElement(document, "init");

        // Check if this is random states
        String randomStatesAttr = initElement.getAttribute("randomStates");
        hasRandomStates = "true".equalsIgnoreCase(randomStatesAttr);

        if (hasRandomStates) {
            // Parse proportions attribute
            String proportionsAttr = initElement.getAttribute("proportions");
            if (proportionsAttr != null && !proportionsAttr.isEmpty()) {
                parseProportions(proportionsAttr);
            } else {
                // Fall back to random nodes if no proportions attribute
                parseRandomStates(document);
            }

            // Generate states based on proportions
            generateRandomStates();
        } else {
            // Check for explicit state list
            String stateListAttr = initElement.getAttribute("stateList");
            if (stateListAttr != null && !stateListAttr.isEmpty()) {
                parseStateList(stateListAttr);
            } else {
                throw new InvalidXMLConfigurationException("Missing stateList in init element");
            }
        }
    }

  /**
   * Parses a proportions attribute to determine the percentage of each state.
   *
   * @param proportionsAttr the attribute value specifying state proportions (e.g., "A:0.5,B:0.3")
   */
    private void parseProportions(String proportionsAttr) {
        String[] proportionPairs = proportionsAttr.replaceAll("\\s+", "").split(",");

        for (String pair : proportionPairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String state = keyValue[0];
                double proportion = Double.parseDouble(keyValue[1]);

                // Validate state and proportion
                if (!isInSimulation(state, simType)) {
                    throw new IllegalArgumentException("Invalid state for simulation: " + state);
                }
                if (proportion < 0 || proportion > 1) {
                    throw new IllegalArgumentException("Proportion must be between 0 and 1: " + proportion);
                }

                stateProportions.put(state, proportion);
            }
        }
    }

  /**
   * Generates initial cell states based on the state proportions.
   * <p>
   * This method calculates how many cells should be assigned to each state based on the
   * total grid size and the specified proportions. Remaining cells are filled with the default state.
   * The resulting list is shuffled to randomize cell placement.
   * </p>
   */
    private void generateRandomStates() {
        int totalCells = rows * columns;
        initialStates = new String[totalCells];
        List<String> stateList = new ArrayList<>();

        // Add states based on proportions
        double remainingProportion = 1.0;
        String defaultState = getDefaultState(simType);

        for (Map.Entry<String, Double> entry : stateProportions.entrySet()) {
            int count = (int) Math.round(entry.getValue() * totalCells);
            for (int i = 0; i < count; i++) {
                stateList.add(entry.getKey());
            }
            remainingProportion -= entry.getValue();
        }

        // Fill remaining cells with default state
        int remaining = totalCells - stateList.size();
        if (remaining > 0) {
            for (int i = 0; i < remaining; i++) {
                stateList.add(defaultState);
            }
        }

        // Shuffle and assign to initialStates
        Collections.shuffle(stateList);
        initialStates = stateList.toArray(new String[0]);
    }

  /**
   * Parses an explicit state list for initialization.
   *
   * @param stateListStr a comma-separated string or contiguous string representing cell states
   * @throws IllegalArgumentException if the number of states does not match the grid size or if an invalid state is encountered
   */
    private void parseStateList(String stateListStr) {
        String cleanedList = stateListStr.replaceAll("\\s+", "");
        if (cleanedList.contains(",")) {
            initialStates = cleanedList.split(",");
        } else {
            // Handle case where commas might be missing
            initialStates = new String[cleanedList.length()];
            for (int i = 0; i < cleanedList.length(); i++) {
                initialStates[i] = String.valueOf(cleanedList.charAt(i));
            }
        }

        if (initialStates.length != rows * columns) {
            throw new IllegalArgumentException(
                "Number of cell states (" + initialStates.length +
                ") does not match grid size (" + (rows * columns) + ").");
        }

        for (String state : initialStates) {
            if (!isInSimulation(state, simType)) {
                throw new IllegalArgumentException("Invalid cell state: " + state);
            }
        }
    }

  /**
   * Parses random state definitions from a dedicated random section in the XML.
   *
   * @param document the XML Document containing random state definitions
   * @throws InvalidXMLConfigurationException if the total assigned state count exceeds the grid size or an invalid state is found
   */
    private void parseRandomStates(Document document) throws InvalidXMLConfigurationException {
        NodeList randomNodes = document.getElementsByTagName("random");
        if (randomNodes.getLength() > 0) {
            Element randomElement = (Element) randomNodes.item(0);
            NodeList stateNodes = randomElement.getElementsByTagName("state");

            // Initialize a map to store state counts
            Map<String, Integer> stateCounts = new HashMap<>();
            int totalCells = rows * columns;
            int assignedCells = 0;

            // Parse state counts
            for (int i = 0; i < stateNodes.getLength(); i++) {
                Element stateElement = (Element) stateNodes.item(i);
                String state = stateElement.getTextContent().trim(); // Trim to remove whitespace
                int count = getRequiredIntAttribute(stateElement, "count");

                // Validate state
                if (!isInSimulation(state, simType)) {
                    throw new IllegalArgumentException("Invalid state for simulation: " + state);
                }

                stateCounts.put(state, count);
                assignedCells += count;
            }

            // Validate total assigned cells
            if (assignedCells > totalCells) {
                throw new IllegalArgumentException("Total assigned cells exceed grid size.");
            }

            // Generate random states
            initialStates = new String[totalCells];
            List<String> stateList = new ArrayList<>();

            // Add states based on counts
            for (Map.Entry<String, Integer> entry : stateCounts.entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    stateList.add(entry.getKey());
                }
            }

            // Fill remaining cells with default state (e.g., DEAD or EMPTY)
            String defaultState = getDefaultState(simType);
            while (stateList.size() < totalCells) {
                stateList.add(defaultState);
            }

            // Shuffle and assign to initialStates
            Collections.shuffle(stateList);
            initialStates = stateList.toArray(new String[0]);
        }
    }

  /**
   * Parses pattern sections from the XML document.
   * <p>
   * For each "pattern" element, the method reads the starting position, pattern state list,
   * and computes the dimensions of the pattern. Patterns are then stored in a map.
   * </p>
   *
   * @param document the XML Document to parse
   * @throws InvalidXMLConfigurationException if a pattern exceeds grid bounds
   */
    private void parsePatterns(Document document) throws InvalidXMLConfigurationException {
        NodeList patternNodes = document.getElementsByTagName("pattern");
        for (int i = 0; i < patternNodes.getLength(); i++) {
            Element patternElement = (Element) patternNodes.item(i);
            String patternId = getRequiredAttribute(patternElement, "id");
            int startRow = getRequiredIntAttribute(patternElement, "startRow");
            int startCol = getRequiredIntAttribute(patternElement, "startCol");

            // Parse the state list from the pattern's text content
            String stateListStr = patternElement.getTextContent()
                .replaceAll("stateList\\s*=", "")
                .replaceAll("\"", "")
                .trim();

            String[] patternStateArray = parsePatternStateList(stateListStr);
            int patternRows = countRows(stateListStr);
            int patternCols = patternStateArray.length / patternRows;

            // Validate pattern dimensions
            if (startRow < 0 || startRow + patternRows > rows) {
                throw new InvalidXMLConfigurationException("Pattern '" + patternId + "' exceeds grid row bounds");
            }
            if (startCol < 0 || startCol + patternCols > columns) {
                throw new InvalidXMLConfigurationException("Pattern '" + patternId + "' exceeds grid column bounds");
            }

            // Add pattern to the map
            patterns.put(patternId, new GridPattern(startRow, startCol, patternStateArray, patternRows, patternCols));
        }
    }

  /**
   * Parses a pattern state list string into an array of state symbols.
   *
   * @param stateListStr the string containing the pattern state list
   * @return an array of state symbols
   */
    private String[] parsePatternStateList(String stateListStr) {
        // Remove all whitespace and split by commas
        String cleanedList = stateListStr.replaceAll("\\s+", "");
        return cleanedList.split(",");
    }

  /**
   * Counts the number of non-empty rows in the given pattern state list string.
   *
   * @param stateListStr the pattern state list string
   * @return the number of rows in the pattern
   */
    private int countRows(String stateListStr) {
        // Count the number of rows by counting newlines
        String[] lines = stateListStr.split("\\n");
        int rowCount = 0;
        for (String line : lines) {
            if (line.trim().length() > 0) {
                rowCount++;
            }
        }
        return rowCount;
    }

  /**
   * Applies all defined patterns to the initial states array.
   * <p>
   * For each pattern, overlays its state values onto the corresponding positions in the overall grid.
   * </p>
   */
    private void applyPatterns() {
        // Apply each pattern to the initial states
        for (GridPattern pattern : patterns.values()) {
            int startRow = pattern.getStartRow();
            int startCol = pattern.getStartCol();
            String[] patternStates = pattern.getPatternStates();
            int patternRows = pattern.getPatternRows();
            int patternCols = pattern.getPatternCols();

            // Overlay the pattern onto the initial states
            for (int r = 0; r < patternRows; r++) {
                for (int c = 0; c < patternCols; c++) {
                    int patternIndex = r * patternCols + c;
                    int gridIndex = (startRow + r) * columns + (startCol + c);

                    if (patternIndex < patternStates.length && gridIndex < initialStates.length) {
                        initialStates[gridIndex] = patternStates[patternIndex];
                    }
                }
            }
        }
    }


  /**
   * Validates whether the simulation type is valid.
   *
   * @param simType the simulation type to validate
   * @return true if the simulation type is valid, false otherwise
   */
    private boolean isValidSimulationType(String simType) {
        return validateSimulation(simType);
    }

  /**
   * Validates that a probability value is a number between 0 and 1.
   *
   * @param value the probability value as a string
   * @throws IllegalArgumentException if the value is not a valid probability
   */
    private void validateProbability(String value) {
        try {
            double prob = Double.parseDouble(value);
            if (prob < 0 || prob > 1) {
                throw new IllegalArgumentException("Probability must be between 0 and 1: " + value);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid probability value: " + value);
        }
    }

  /**
   * Returns the default state symbol for the given simulation type.
   *
   * @param simType the simulation type (e.g., "Conway", "Fire")
   * @return the default state symbol for the simulation
   * @throws IllegalArgumentException if the simulation type is unknown
   */
    private String getDefaultState(String simType) {
        return switch (simType) {
            case "Conway", "GameOfLife" -> "D"; // DEAD
            case "Fire" -> "E";   // EMPTY
            case "Percolation" -> "O"; // OPEN
            case "Segregation" -> "EM"; // EMPTY
            case "WatorWorld" -> "W"; // WATER
            case "GeneralConway" -> "D";
            default -> throw new IllegalArgumentException("Unknown simulation type: " + simType);
        };
    }

  /**
   * Retrieves a required element with the given tag name from the parent node.
   *
   * @param parent  the parent node (Document or Element)
   * @param tagName the tag name of the required element
   * @return the required Element
   * @throws IllegalArgumentException if the element is not found
   */
    private Element getRequiredElement(Node parent, String tagName) {
        NodeList elements = (parent instanceof Document ?
            ((Document)parent).getElementsByTagName(tagName) :
            ((Element)parent).getElementsByTagName(tagName));

        if (elements.getLength() == 0) {
            throw new IllegalArgumentException(
                String.format("Required element '%s' not found", tagName));
        }
        return (Element) elements.item(0);
    }

  /**
   * Retrieves a required attribute value from an element.
   *
   * @param element       the element from which to retrieve the attribute
   * @param attributeName the name of the attribute
   * @return the attribute value
   * @throws InvalidXMLConfigurationException if the attribute is missing or empty
   */
    private String getRequiredAttribute(Element element, String attributeName) throws InvalidXMLConfigurationException {
        String value = element.getAttribute(attributeName);
        if (value.isEmpty()) {
            throw new InvalidXMLConfigurationException(
                String.format("Error: Missing required attribute '%s' in element '%s'.",
                    attributeName, element.getTagName()));
        }
        return value;
    }

  /**
   * Retrieves a required integer attribute from an element.
   *
   * @param element       the element from which to retrieve the attribute
   * @param attributeName the name of the attribute
   * @return the integer value of the attribute
   * @throws InvalidXMLConfigurationException if the attribute is missing, empty, or not an integer
   */
    private int getRequiredIntAttribute(Element element, String attributeName)
        throws InvalidXMLConfigurationException {
        String value = getRequiredAttribute(element, attributeName);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                String.format("Attribute '%s' must be an integer, got '%s'",
                    attributeName, value));
        }
    }

  // Getter methods for various configuration properties
    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
      return title;
    }

    public String getSimType() {
      return simType;
    }

    public String getAuthor(){return author;}

    public int getRows() {
      return rows;
    }

    public int getColumns() {
      return columns;
    }

    public String[] getInitialStates() {
      return initialStates;
    }

    public Map<String, String> getSimVarsMap() {
      return simVarsMap;
    }

    public Map<String, GridPattern> getPatterns() {
        return patterns;
    }

    public boolean hasRandomStates() {
      return hasRandomStates;
    }

    public Map<String, Double> getStateProportions() {
        return stateProportions;
    }

  public String getEdgeType() {
    return edgeType;
  }

  public String getNeighborhoodType() {
    return neighborhoodType;
  }

  public String getCellShape(){
    return cellShape;
  }
}