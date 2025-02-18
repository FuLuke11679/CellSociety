package cellsociety.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class XMLParserTest {
  XMLParser xmlParser;

  @BeforeEach
  void setUp() throws InvalidXMLConfigurationException {
    File file = new File("data/GameOfLife_1.xml");
    xmlParser = new XMLParser(file);
  }
  @Test
  void getWidth_returnCorrectWidth() {
    assertEquals(800, xmlParser.getWidth());
  }

  @Test
  void getHeight_returnsCorrectHeight() {
    assertEquals(800, xmlParser.getHeight());
  }

  @Test
  void getTitle_returnsCorrectTitle() {
    assertEquals("Conway Simulation", xmlParser.getTitle());
  }

  @Test
  void getSimType_returnsCorrectSimType() {
    assertEquals("Conway", xmlParser.getSimType());
  }

  @Test
  void getRows_returnsCorrectRows() {
    assertEquals(5, xmlParser.getRows());
  }

  @Test
  void getColumns_returnsCorrectColumns() {
    assertEquals(5, xmlParser.getColumns());
  }

  @Test
  void getInitialStates_returnsCorrectInitialStates() {
    String[] expected = {"A","A","D","D","D",
        "A","A","D","D","D",
        "A","A","D","D","D",
        "A","A","D","D","D",
        "A","A","D","D","D",};
    assertArrayEquals(expected, xmlParser.getInitialStates());
  }

  @Test
  void getSimVarsMap_returnsCorrectSimVarsMap() {
    Map<String, String> expected = new HashMap<>();
    assertEquals(expected, xmlParser.getSimVarsMap());
  }
}