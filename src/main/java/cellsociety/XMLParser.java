package cellsociety;

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

public class XMLParser extends Parser{
  private int width;
  private int height;
  private String title;
  private int rows;
  private int columns;
  private String simType;
  private String description;
  private List<Cellt> initialStates;
  private Map<String, String> simVarsMap;

  public XMLParser(File file) {
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
      String[] states = stateList.split(",");
      initialStates = new ArrayList<>();
      int i=0;
      for(String state : states){

        if(state.equals("A")){
          initialStates.add(new CellUnit(i, Color.BLUE));
        }
        else if(state.equals("D")){
          initialStates.add(new CellUnit(i, Color.WHITE));
        }
        else if()
        i++;
      }

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

  public int getWidth() { return width; }

  public int getHeight() { return height; }

  public String getTitle() { return title; }

  public String getSimType() {return simType; }

  public int getRows() { return rows; }

  public int getColumns() { return columns; }

  public List<CellUnit> getInitialStates() { return initialStates; }

  public Map<String, String> getSimVarsMap() {return simVarsMap;}

}