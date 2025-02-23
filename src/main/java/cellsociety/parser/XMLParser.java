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

  // Display/Simulation parameters
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
  private int[] initialValues;  // for explicit state values

  // New fields for random initialization and patterns
  private Map<String, GridPattern> patterns;
  private boolean hasRandomStates;
  private Map<String, Double> stateProportions;

  // Nested class to store grid pattern information
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

  // Constructor
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
    return file.getName().endsWith(".xml");
  }

  private void handleError(String message, Exception e) {
    System.err.println(message + ": " + e.getMessage());
  }

  // Main parsing method
  private void parseDocument(Document document) throws InvalidXMLConfigurationException {
    parseDisplay(document);
    parseSimulation(document);

    // Unified init section: handles both explicit state lists and random initialization.
    parseInitSection(document);

    // Parse any pattern sections and overlay them onto the initial states.
    parsePatterns(document);
    if (!patterns.isEmpty()) {
      applyPatterns();
    }

    if (initialValues == null) {
      throw new IllegalStateException("initialValues is NULL after parsing initial states.");
    }
  }

  // Parse <display> block
  private void parseDisplay(Document document) throws InvalidXMLConfigurationException {
    Element display = getRequiredElement(document, "display");
    this.width = getRequiredIntAttribute(display, "width");
    if (width <= 0) throw new InvalidXMLConfigurationException("Width must be positive");
    this.height = getRequiredIntAttribute(display, "height");
    if (height <= 0) throw new InvalidXMLConfigurationException("Height must be positive");
    this.title = getRequiredAttribute(display, "title");
    if (title.trim().isEmpty()) throw new InvalidXMLConfigurationException("Title cannot be empty");
    this.author = getRequiredAttribute(display, "author");
    if (author.trim().isEmpty()) throw new InvalidXMLConfigurationException("Author cannot be empty");

    Element grid = getRequiredElement(display, "grid");
    this.rows = getRequiredIntAttribute(grid, "rows");
    if (rows <= 0) throw new InvalidXMLConfigurationException("Rows must be positive");
    this.columns = getRequiredIntAttribute(grid, "columns");
    if (columns <= 0) throw new InvalidXMLConfigurationException("Columns must be positive");

    Element descElement = getRequiredElement(display, "description");
    this.description = getRequiredAttribute(descElement, "text");
    if (description.trim().isEmpty()) throw new InvalidXMLConfigurationException("Description cannot be empty");
  }

  // Parse <sim> block
  private void parseSimulation(Document document) throws InvalidXMLConfigurationException {
    Element sim = getRequiredElement(document, "sim");
    this.simType = getRequiredAttribute(sim, "type");
    if (!isValidSimulationType(simType)) {
      throw new IllegalArgumentException("Invalid simulation type: " + simType);
    }
    Element simvars = getRequiredElement(document, "simvars");
    simVarsMap = new HashMap<>();
    NamedNodeMap attrs = simvars.getAttributes();
    for (int i = 0; i < attrs.getLength(); i++) {
      Node attr = attrs.item(i);
      String value = attr.getNodeValue();
      if (attr.getNodeName().equals("probCatch") || attr.getNodeName().equals("probGrow")) {
        validateProbability(value);
      }
      simVarsMap.put(attr.getNodeName(), value);
    }
  }

  // Unified initialization method
  private void parseInitSection(Document document) throws InvalidXMLConfigurationException {
    Element initElement = getRequiredElement(document, "init");

    // Check for a "randomStates" attribute to decide which branch to use.
    String randomStatesAttr = initElement.getAttribute("randomStates");
    boolean useRandom = "true".equalsIgnoreCase(randomStatesAttr);

    if (useRandom) {
      hasRandomStates = true;
      // If a "proportions" attribute exists, use it
      String proportionsAttr = initElement.getAttribute("proportions");
      if (proportionsAttr != null && !proportionsAttr.trim().isEmpty()) {
        parseProportions(proportionsAttr);
      } else {
        // Fallback: if there is a <random> element present in the document, use that method.
        if (document.getElementsByTagName("random").getLength() > 0) {
          parseRandomStates(document);
        } else {
          throw new InvalidXMLConfigurationException("No proportions or <random> element provided for random states");
        }
      }
      generateRandomStates();
    } else {
      // Use explicit state list provided via the "stateList" attribute.
      String stateListAttr = initElement.getAttribute("stateList");
      if (stateListAttr == null || stateListAttr.trim().isEmpty()) {
        throw new InvalidXMLConfigurationException("Missing stateList in init element");
      }
      parseStateList(stateListAttr);
    }
  }

  // Parse proportions in the form "state:proportion,state:proportion,..."
  private void parseProportions(String proportionsAttr) {
    String[] pairs = proportionsAttr.replaceAll("\\s+", "").split(",");
    for (String pair : pairs) {
      String[] keyValue = pair.split(":");
      if (keyValue.length == 2) {
        String state = keyValue[0];
        double proportion = Double.parseDouble(keyValue[1]);
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

  // Generate initialStates array based on stateProportions
  private void generateRandomStates() {
    int totalCells = rows * columns;
    List<String> stateList = new ArrayList<>();
    for (Map.Entry<String, Double> entry : stateProportions.entrySet()) {
      int count = (int) Math.round(entry.getValue() * totalCells);
      for (int i = 0; i < count; i++) {
        stateList.add(entry.getKey());
      }
    }
    // Fill any remaining cells with the default state
    String defaultState = getDefaultState(simType);
    while (stateList.size() < totalCells) {
      stateList.add(defaultState);
    }
    Collections.shuffle(stateList);
    initialStates = stateList.toArray(new String[0]);
    // For random initialization, assign a default value (e.g., 25) to each cell
    initialValues = new int[totalCells];
    for (int i = 0; i < totalCells; i++) {
      initialValues[i] = 25;
    }
  }

  // Parse explicit state list (with optional values separated by a colon)
  private void parseStateList(String stateListStr) throws InvalidXMLConfigurationException {
    String cleanedList = stateListStr.replaceAll("\\s+", "");
    String[] tokens = cleanedList.split(",");
    int expectedCells = rows * columns;
    if (tokens.length != expectedCells) {
      throw new IllegalArgumentException("Number of cell states (" + tokens.length +
          ") does not match grid size (" + expectedCells + ").");
    }
    initialStates = new String[tokens.length];
    initialValues = new int[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i].trim();
      if (token.contains(":")) {
        String[] parts = token.split(":", 2);
        if (parts.length != 2) {
          throw new IllegalArgumentException("Invalid token format: " + token);
        }
        String state = parts[0];
        int value = parts[1].isEmpty() ? 25 : Integer.parseInt(parts[1]);
        if (!isInSimulation(state, simType)) {
          throw new IllegalArgumentException("Invalid state for simulation: " + state);
        }
        initialStates[i] = state;
        initialValues[i] = value;
      } else {
        if (!isInSimulation(token, simType)) {
          throw new IllegalArgumentException("Invalid cell state: " + token);
        }
        initialStates[i] = token;
        initialValues[i] = 25;
      }
    }
  }

  // Fallback random state parsing via a <random> element if no proportions attribute is provided
  private void parseRandomStates(Document document) throws InvalidXMLConfigurationException {
    NodeList randomNodes = document.getElementsByTagName("random");
    if (randomNodes.getLength() > 0) {
      Element randomElement = (Element) randomNodes.item(0);
      NodeList stateNodes = randomElement.getElementsByTagName("state");
      Map<String, Integer> stateCounts = new HashMap<>();
      int totalCells = rows * columns;
      int assignedCells = 0;
      for (int i = 0; i < stateNodes.getLength(); i++) {
        Element stateElement = (Element) stateNodes.item(i);
        String state = stateElement.getTextContent().trim();
        int count = getRequiredIntAttribute(stateElement, "count");
        if (!isInSimulation(state, simType)) {
          throw new IllegalArgumentException("Invalid state for simulation: " + state);
        }
        stateCounts.put(state, count);
        assignedCells += count;
      }
      if (assignedCells > totalCells) {
        throw new IllegalArgumentException("Total assigned cells exceed grid size.");
      }
      List<String> stateList = new ArrayList<>();
      for (Map.Entry<String, Integer> entry : stateCounts.entrySet()) {
        for (int i = 0; i < entry.getValue(); i++) {
          stateList.add(entry.getKey());
        }
      }
      String defaultState = getDefaultState(simType);
      while (stateList.size() < totalCells) {
        stateList.add(defaultState);
      }
      Collections.shuffle(stateList);
      initialStates = stateList.toArray(new String[0]);
      initialValues = new int[totalCells];
      for (int i = 0; i < totalCells; i++) {
        initialValues[i] = 25;
      }
    }
  }

  // Parse any <pattern> elements to overlay on the initial grid
  private void parsePatterns(Document document) throws InvalidXMLConfigurationException {
    NodeList patternNodes = document.getElementsByTagName("pattern");
    for (int i = 0; i < patternNodes.getLength(); i++) {
      Element patternElement = (Element) patternNodes.item(i);
      String patternId = getRequiredAttribute(patternElement, "id");
      int startRow = getRequiredIntAttribute(patternElement, "startRow");
      int startCol = getRequiredIntAttribute(patternElement, "startCol");
      // Get pattern state list from the element's text content.
      String stateListStr = patternElement.getTextContent().trim();
      // Remove any prefix such as "stateList=" and quotes.
      stateListStr = stateListStr.replaceAll("stateList\\s*=", "").replaceAll("\"", "").trim();
      String[] patternStates = parsePatternStateList(stateListStr);
      int patternRows = countRows(stateListStr);
      int patternCols = patternStates.length / patternRows;
      if (startRow < 0 || startRow + patternRows > rows) {
        throw new InvalidXMLConfigurationException("Pattern '" + patternId + "' exceeds grid row bounds");
      }
      if (startCol < 0 || startCol + patternCols > columns) {
        throw new InvalidXMLConfigurationException("Pattern '" + patternId + "' exceeds grid column bounds");
      }
      patterns.put(patternId, new GridPattern(startRow, startCol, patternStates, patternRows, patternCols));
    }
  }

  private String[] parsePatternStateList(String stateListStr) {
    String cleanedList = stateListStr.replaceAll("\\s+", "");
    return cleanedList.split(",");
  }

  private int countRows(String stateListStr) {
    String[] lines = stateListStr.split("\\n");
    int count = 0;
    for (String line : lines) {
      if (!line.trim().isEmpty()) {
        count++;
      }
    }
    return count;
  }

  // Overlay each pattern onto the initialStates array
  private void applyPatterns() {
    for (GridPattern pattern : patterns.values()) {
      int startRow = pattern.getStartRow();
      int startCol = pattern.getStartCol();
      String[] patternStates = pattern.getPatternStates();
      int patternRows = pattern.getPatternRows();
      int patternCols = pattern.getPatternCols();
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
      case "Wator" -> "W";  // WATER
      case "GeneralConway" -> "D";
      case "Sugarscape" -> "PATCH";  // Corrected key for Sugarscape
      default -> throw new IllegalArgumentException("Unknown simulation type: " + simType);
    };
  }

  private Element getRequiredElement(Node parent, String tagName) {
    NodeList elements = (parent instanceof Document ?
        ((Document) parent).getElementsByTagName(tagName) :
        ((Element) parent).getElementsByTagName(tagName));
    if (elements.getLength() == 0) {
      throw new IllegalArgumentException(String.format("Required element '%s' not found", tagName));
    }
    return (Element) elements.item(0);
  }

  private String getRequiredAttribute(Element element, String attributeName) throws InvalidXMLConfigurationException {
    String value = element.getAttribute(attributeName);
    if (value.isEmpty()) {
      throw new InvalidXMLConfigurationException(
          String.format("Error: Missing required attribute '%s' in element '%s'.", attributeName, element.getTagName()));
    }
    return value;
  }

  private int getRequiredIntAttribute(Element element, String attributeName) throws InvalidXMLConfigurationException {
    String value = getRequiredAttribute(element, attributeName);
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          String.format("Attribute '%s' must be an integer, got '%s'", attributeName, value));
    }
  }

  // Getters for external use
  public int getWidth() { return width; }
  public int getHeight() { return height; }
  public String getDescription() { return description; }
  public String getTitle() { return title; }
  public String getSimType() { return simType; }
  public String getAuthor() { return author; }
  public int getRows() { return rows; }
  public int getColumns() { return columns; }
  public String[] getInitialStates() { return initialStates; }
  public Map<String, String> getSimVarsMap() { return simVarsMap; }
  public int[] getValues() {
    if (initialValues == null) {
      throw new IllegalStateException("initialValues is NULL in getValues(). Ensure XML file is parsed first.");
    }
    return initialValues;
  }
  public Map<String, GridPattern> getPatterns() { return patterns; }
  public boolean hasRandomStates() { return hasRandomStates; }
  public Map<String, Double> getStateProportions() { return stateProportions; }
}
