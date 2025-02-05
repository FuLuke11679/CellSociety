package cellsociety;

import cellsociety.model.cell.ConwayCell;
import java.io.IOException;
import javafx.scene.paint.Color;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.SAXException;
import java.util.HashMap;
import java.util.Map;
import cellsociety.model.cell.Cell;

public class XMLParser extends Parser{
  private int width;
  private int height;
  private String title;
  private int rows;
  private int columns;
  private String simType;
  private String description;
  private String[] initialStates;
  private Map<String, String> simVarsMap;

  public XMLParser(File file) throws ParserConfigurationException, SAXException, IOException{
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(file);
      //document.getDocumentElement().normalize();

      Element root = document.getDocumentElement();
      System.out.println("Root Element: " + root.getNodeName());

      Element display = (Element) document.getElementsByTagName("display").item(0);
      this.width = Integer.parseInt(display.getAttribute("width"));
      this.height = Integer.parseInt(display.getAttribute("height"));
      this.title = display.getAttribute("title");

      System.out.println("Display - Width: " + width + ", Height: " + height + ", Title: " + title);


      Element grid = (Element) display.getElementsByTagName("grid").item(0);
      this.rows = Integer.parseInt(grid.getAttribute("rows"));
      this.columns = Integer.parseInt(grid.getAttribute("columns"));

      Element description = (Element) display.getElementsByTagName("description").item(0);
      this.description = description.getAttribute("text");
      System.out.println("Description: " + this.description);

      Element sim = (Element) document.getElementsByTagName("sim").item(0);
      this.simType = sim.getAttribute("type");

      Element simvars = (Element) document.getElementsByTagName("simvars").item(0);
      this.simType = sim.getAttribute("type");
      simVarsMap = new HashMap<>();

      NamedNodeMap attributes = simvars.getAttributes();
      for (int i = 0; i < attributes.getLength(); i++) {
        Node attr = attributes.item(i);
        simVarsMap.put(attr.getNodeName(), attr.getNodeValue());
      }
      for (Map.Entry<String, String> entry : simVarsMap.entrySet()) {
        System.out.println(entry.getKey() + " = " + entry.getValue());
      }

      Element initElement = (Element) document.getElementsByTagName("init").item(0);
      String stateList = initElement.getAttribute("stateList").replaceAll("\\s+", ""); // Remove newlines and spaces
      // Split values using comma
      initialStates = stateList.split(",");


    } catch (ParserConfigurationException e) {
      System.err.println("Parser Configuration Error: " + e.getMessage());
    } catch (SAXException e) {
      System.err.println("XML Parsing Error: The file may be malformed. " + e.getMessage());
    } catch (IOException e) {
      System.err.println("File I/O Error: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Unexpected Error: " + e.getMessage());
    }

  }

  private void parseDocument(Document document){
    Element root = document.getDocumentElement();
        
    parseDisplay(document);
    parseSimulation(document);
    parseInitialStates(document);
  }

  private void parseDisplay(Document document){
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
    String value = getRequiredAttribute(element, "width");
    try {
        return Integer.parseInt(value);
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException(
            String.format("Attribute '%s' must be an integer, got '%s'",
                attributeName, value));
    }
  }



  public int getWidth() { return width; }

  public int getHeight() { return height; }

  public String getTitle() { return title; }

  public String getSimType() {return simType; }

  public int getRows() { return rows; }

  public int getColumns() { return columns; }

  public String[] getInitialStates() { return initialStates; }

  public Map<String, String> getSimVarsMap() {return simVarsMap;}

}