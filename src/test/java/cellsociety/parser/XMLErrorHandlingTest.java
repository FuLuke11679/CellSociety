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