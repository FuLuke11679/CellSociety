package cellsociety.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Map;

public class XMLErrorHandlingTest {

    @Test
    public void testMissingParameters() {
        // CELL-27: Input Missing Parameters
        File testFile = new File("data/invalid/missing-parameters.xml");
        assertThrows(InvalidXMLConfigurationException.class, () -> {
            new XMLParser(testFile);
        });
    }

    @Test
    public void testInvalidValue() {
        // CELL-28: Invalid Value Check
        File testFile = new File("data/invalid/invalid-probability-xml.xml");
        InvalidXMLConfigurationException exception = assertThrows(InvalidXMLConfigurationException.class, () -> {
            new XMLParser(testFile);
        });
        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("Probability"));
    }

    @Test
    public void testInvalidCellState() {
        // CELL-29: Invalid Cell State Check
        File testFile = new File("data/invalid/invalid-cell-state-xml.xml");
        InvalidXMLConfigurationException exception = assertThrows(InvalidXMLConfigurationException.class, () -> {
            new XMLParser(testFile);
        });
        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid cell state"));
    }

    @Test
    public void testGridBounds() {
        // CELL-30: Grid Bounds Check
        File testFile = new File("data/invalid/grid-bounds-xml.xml");
        InvalidXMLConfigurationException exception = assertThrows(InvalidXMLConfigurationException.class, () -> {
            new XMLParser(testFile);
        });
        assertTrue(exception.getMessage().contains("cell states") && exception.getMessage().contains("grid size"));
    }

    @Test
    public void testFileFormat() {
        // CELL-31: File Format Validation
        File testFile = new File("invalid/not-xml-file.xml");
        assertThrows(InvalidXMLConfigurationException.class, () -> {
            new XMLParser(testFile);
        });
    }

    @Test
    public void testValidFile() {
        // Test that a valid file is parsed correctly
        File testFile = new File("data/fire/Fire1.xml");
        try {
            XMLParser parser = new XMLParser(testFile);
            assertEquals("Fire", parser.getSimType());
            assertEquals(5, parser.getRows());
            assertEquals(5, parser.getColumns());
        } catch (InvalidXMLConfigurationException e) {
            fail("Valid file should not throw exception: " + e.getMessage());
        }
    }

}