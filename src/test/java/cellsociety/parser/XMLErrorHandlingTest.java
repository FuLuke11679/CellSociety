package cellsociety.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class XMLErrorHandlingTest {

    @Test
    public void testMissingParameters() {
        // CELL-27: Input Missing Parameters
        File testFile = new File("test-resources/missing-parameters.xml");
        assertThrows(InvalidXMLConfigurationException.class, () -> {
            new XMLParser(testFile);
        });
    }

    @Test
    public void testInvalidValue() {
        // CELL-28: Invalid Value Check
        File testFile = new File("test-resources/invalid-probability.xml");
        InvalidXMLConfigurationException exception = assertThrows(InvalidXMLConfigurationException.class, () -> {
            new XMLParser(testFile);
        });
        assertTrue(exception.getMessage().contains("probability"));
    }

    @Test
    public void testInvalidCellState() {
        // CELL-29: Invalid Cell State Check
        File testFile = new File("test-resources/invalid-cell-state.xml");
        InvalidXMLConfigurationException exception = assertThrows(InvalidXMLConfigurationException.class, () -> {
            new XMLParser(testFile);
        });
        assertTrue(exception.getMessage().contains("Invalid cell state"));
    }

    @Test
    public void testGridBounds() {
        // CELL-30: Grid Bounds Check
        File testFile = new File("test-resources/grid-bounds.xml");
        InvalidXMLConfigurationException exception = assertThrows(InvalidXMLConfigurationException.class, () -> {
            new XMLParser(testFile);
        });
        assertTrue(exception.getMessage().contains("cell states") && exception.getMessage().contains("grid size"));
    }

    @Test
    public void testFileFormat() {
        // CELL-31: File Format Validation
        File testFile = new File("test-resources/not-xml.xml");
        assertThrows(InvalidXMLConfigurationException.class, () -> {
            new XMLParser(testFile);
        });
    }

    @Test
    public void testValidFile() {
        // Test that a valid file is parsed correctly
        File testFile = new File("test-resources/valid-conway.xml");
        try {
            XMLParser parser = new XMLParser(testFile);
            assertEquals("Conway", parser.getSimType());
            assertEquals(10, parser.getRows());
            assertEquals(10, parser.getColumns());
        } catch (InvalidXMLConfigurationException e) {
            fail("Valid file should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testDefaultValues() {
        // Test that default values are provided
        File testFile = new File("test-resources/missing-simvars.xml");
        try {
            XMLParser parser = new XMLParser(testFile);
            assertEquals("Fire", parser.getSimType());
            Map<String, String> simVars = parser.getSimVarsMap();
            assertNotNull(simVars.get("probCatch"));
        } catch (InvalidXMLConfigurationException e) {
            fail("Parser should provide default values: " + e.getMessage());
        }
    }
}