package cellsociety.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    // Class to store pattern information
    public class GridPattern {
        private int startRow;
        private int startCol;
        private String[] patternStates;
        private int patternRows;
        private int patternCols;

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

    private boolean isXMLFile(File file) {
        String fileName = file.getName();
        return fileName.endsWith(".xml");
    }

    private void handleError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
    }

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
            
        } catch (NullPointerException e) {
            throw new InvalidXMLConfigurationException("Missing required display elements");
        }
    }

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
    
    private String[] parsePatternStateList(String stateListStr) {
        // Remove all whitespace and split by commas
        String cleanedList = stateListStr.replaceAll("\\s+", "");
        return cleanedList.split(",");
    }
    
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

    private boolean isValidSimulationType(String simType) {
        return validateSimulation(simType);
    }

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

    private String getDefaultState(String simType) {
        return switch (simType) {
            case "Conway", "GameOfLife" -> "D"; // DEAD
            case "Fire" -> "E";   // EMPTY
            case "Percolation" -> "O"; // OPEN
            case "Segregation" -> "EM"; // EMPTY
            case "Wator" -> "W"; // WATER
            case "GeneralConway" -> "D";
            default -> throw new IllegalArgumentException("Unknown simulation type: " + simType);
        };
    }

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

    private String getRequiredAttribute(Element element, String attributeName) throws InvalidXMLConfigurationException {
        String value = element.getAttribute(attributeName);
        if (value.isEmpty()) {
            throw new InvalidXMLConfigurationException(
                String.format("Error: Missing required attribute '%s' in element '%s'.",
                    attributeName, element.getTagName()));
        }
        return value;
    }

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
}