package cellsociety.parser;

import java.io.File;
import java.io.IOException;
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


    // TODO: modify this so that it has methods to return relevant info in the xml file
    public XMLParser(File file) throws InvalidXMLConfigurationException {
        try {
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



    private void handleError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
    }
    
    private void parseDocument(Document document) throws InvalidXMLConfigurationException {
        Element root = document.getDocumentElement();
        
        parseDisplay(document);
        parseSimulation(document);
        parseInitialStates(document);
    }
    
    private void parseDisplay(Document document) throws InvalidXMLConfigurationException {
        Element display = getRequiredElement(document, "display");
        
        this.width = getRequiredIntAttribute(display, "width");
        this.height = getRequiredIntAttribute(display, "height");
        this.title = getRequiredAttribute(display, "title");
        this.author = getRequiredAttribute(display, "author");
        
        Element grid = getRequiredElement(display, "grid");
        this.rows = getRequiredIntAttribute(grid, "rows");
        this.columns = getRequiredIntAttribute(grid, "columns");
        
        Element descElement = getRequiredElement(display, "description");
        this.description = getRequiredAttribute(descElement, "text");
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

    private boolean isValidSimulationType(String simType) {
        return List.of("Conway", "Percolation", "Fire", "Segregation", "WatorWorld").contains(simType);
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

    private void parseInitialStates(Document document) throws InvalidXMLConfigurationException {
        Element initElement = getRequiredElement(document, "init");
        String stateList = getRequiredAttribute(initElement, "stateList")
            .replaceAll("\\s+", "");
        initialStates = stateList.split(",");

        if (initialStates.length != rows * columns) {
            throw new IllegalArgumentException("Number of cell states does not match grid size.");
        }

        for (String state : initialStates) {
            if (!isInSimulation(state, simType)) {
                throw new IllegalArgumentException("Invalid cell state: " + state);
            }
        }
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
}