package cellsociety.view.shapes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.Test;

class ShapeFactoryTest {

  @Test
  void testGetAvailableShapes() {
    List<String> expected = Arrays.asList("Rectangular", "Hexagonal", "Triangular", "Rhombus",
        "PentagonalTiling");
    List<String> actual = ShapeFactory.getAvailableShapes();
    assertEquals(expected, actual, "The available shapes list should match the expected list.");
  }

  @Test
  void testGetFullyQualifiedNameWithSimpleName() {
    String shapeName = "Rectangular";
    String expected = "cellsociety.view.shapes.RectangularShape";
    String actual = ShapeFactory.getFullyQualifiedName(shapeName);
    assertEquals(expected, actual,
        "A simple shape name should be converted to a fully qualified class name.");
  }

  @Test
  void testGetFullyQualifiedNameWithFullyQualifiedName() {
    String shapeName = "cellsociety.view.shapes.CustomShape";
    String actual = ShapeFactory.getFullyQualifiedName(shapeName);
    assertEquals(shapeName, actual, "A fully qualified shape name should be returned unchanged.");
  }

  @Test
  void testGetFullyQualifiedNameWithInvalidName() {
    assertThrows(IllegalArgumentException.class, () -> ShapeFactory.getFullyQualifiedName(""),
        "An empty shape name should throw an IllegalArgumentException.");
    assertThrows(IllegalArgumentException.class, () -> ShapeFactory.getFullyQualifiedName("   "),
        "A blank shape name should throw an IllegalArgumentException.");
    assertThrows(IllegalArgumentException.class, () -> ShapeFactory.getFullyQualifiedName(null),
        "A null shape name should throw an IllegalArgumentException.");
  }

  @Test
  void testCreateShapeValid() {
    // When creating a valid shape type, e.g., "Rectangular", the factory should return a RectangularShape instance.
    Shape shape = ShapeFactory.createShape("Rectangular", 50, 1, 2);
    assertNotNull(shape, "The shape instance should not be null.");
    assertTrue(shape instanceof RectangularShape,
        "The shape should be an instance of RectangularShape.");
  }

  @Test
  void testCreateShapeInvalidFallsBack() {
    // When providing an invalid shape type, the factory falls back to RectangularShape.
    Shape shape = ShapeFactory.createShape("NonExistent", 50, 1, 2);
    assertNotNull(shape, "The fallback shape instance should not be null.");
    assertTrue(shape instanceof RectangularShape,
        "The fallback shape should be an instance of RectangularShape.");
  }
}
