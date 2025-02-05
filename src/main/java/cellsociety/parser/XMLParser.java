package cellsociety.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
    private String[] initialStates;
    private Map<String, String> simVarsMap;

    public XMLParser(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Enable secure processing to prevent XXE attacks
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();
            
            parseDocument(document);
        } catch (ParserConfigurationException e) {
            handleError("XML Parser Configuration Error", e);
        } catch (SAXException e) {
            handleError("XML Parsing Error: The file may be malformed", e);
        } catch (IOException e) {
            handleError("File I/O Error", e);
        } catch (Exception e) {
            handleError("Unexpected Error", e);
        }
    }
    
    private void handleError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        // You might want to throw a custom exception here instead
        // throw new XMLParsingException(message, e);
    }
    
    private void parseDocument(Document document) {
        Element root = document.getDocumentElement();
        
        parseDisplay(document);
        parseSimulation(document);
        parseInitialStates(document);
    }
    
    private void parseDisplay(Document document) {
        Element display = getRequiredElement(document, "display");
        
        this.width = getRequiredIntAttribute(display, "width");
        this.height = getRequiredIntAttribute(display, "height");
        this.title = getRequiredAttribute(display, "title");
        
        Element grid = getRequiredElement(display, "grid");
        this.rows = getRequiredIntAttribute(grid, "rows");
        this.columns = getRequiredIntAttribute(grid, "columns");
        
        Element descElement = getRequiredElement(display, "description");
        this.description = getRequiredAttribute(descElement, "text");
    }
    
    private void parseSimulation(Document document) {
        Element sim = getRequiredElement(document, "sim");
        this.simType = getRequiredAttribute(sim, "type");
        
        Element simvars = getRequiredElement(document, "simvars");
        simVarsMap = new HashMap<>();
        NamedNodeMap attributes = simvars.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attr = attributes.item(i);
            simVarsMap.put(attr.getNodeName(), attr.getNodeValue());
        }
    }
    
    private void parseInitialStates(Document document) {
        Element initElement = getRequiredElement(document, "init");
        String stateList = getRequiredAttribute(initElement, "stateList")
            .replaceAll("\\s+", "");
        initialStates = stateList.split(",");
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
    
    private String getRequiredAttribute(Element element, String attributeName) {
        String value = element.getAttribute(attributeName);
        if (value.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("Required attribute '%s' missing from element '%s'",
                    attributeName, element.getTagName()));
        }
        return value;
    }
    
    private int getRequiredIntAttribute(Element element, String attributeName) {
        String value = getRequiredAttribute(element, attributeName);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                String.format("Attribute '%s' must be an integer, got '%s'",
                    attributeName, value));
        }
    }

    // Getters remain unchanged
    public int getWidth() { 
      return width; 
    }

    public int getHeight() { 
      return height; 
    }

    public String getTitle() { 
      return title; 
    }

    public String getSimType() { 
      return simType; 
    }

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