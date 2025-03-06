package cellsociety.view.shapes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Shape;

/**
 * This class provides factory methods to create various shape instances used in the grid-based
 * simulation. It dynamically loads shape classes using reflection and ensures fallback to a default
 * RectangularShape in case of errors.
 *
 * @author Luke
 */
public class ShapeFactory {

  private static final Logger LOGGER = Logger.getLogger(ShapeFactory.class.getName());

  private static final List<String> AVAILABLE_SHAPES = Arrays.asList("Rectangular", "Hexagonal",
      "Triangular", "Rhombus", "PentagonalTiling");

  /**
   * Returns the fully qualified name of a shape class based on its simple name.
   *
   * @return the all supported shapes
   * @throws IllegalArgumentException if the shape name is null or empty
   */
  public static List<String> getAvailableShapes() {
    return AVAILABLE_SHAPES;
  }

  /**
   * Returns the fully qualified name of a shape class based on its simple name.
   *
   * @param shapeName the simple name of the shape
   * @return the fully qualified class name of the shape
   * @throws IllegalArgumentException if the shape name is null or empty
   */
  public static String getFullyQualifiedName(String shapeName) {
    if (shapeName == null || shapeName.trim().isEmpty()) {
      throw new IllegalArgumentException("Shape name is null or empty.");
    }
    // If the name already contains a dot, assume it's fully qualified
    if (shapeName.contains(".")) {
      return shapeName;
    }
    return "cellsociety.view.shapes." + shapeName + "Shape";
  }

  /**
   * Creates a Shape instance based on the given shape type and grid parameters. If the specified
   * shape type is invalid or an error occurs, a RectangularShape is returned as fallback.
   *
   * @param shapeType the type of shape to create
   * @param size      the size of the shape
   * @param row       the row position in the grid
   * @param col       the column position in the grid
   * @return a Shape instance of the specified type or a RectangularShape on failure
   */
  public static Shape createShape(String shapeType, int size, int row, int col) {
    try {
      if (shapeType == null || shapeType.trim().isEmpty()) {
        throw new IllegalArgumentException("Shape type is null or empty.");
      }

      // Get the fully qualified class name dynamically
      String className = getFullyQualifiedName(shapeType);
      if (className == null || className.trim().isEmpty()) {
        throw new IllegalArgumentException("Generated class name is null or empty.");
      }

      // Load the class dynamically using reflection
      Class<?> shapeClass = Class.forName(className);
      Shape shape = (Shape) shapeClass
          .getDeclaredConstructor(int.class, int.class, int.class)
          .newInstance(size, row, col);

      // Use reflection to find and call the setPosition() method if it exists
      try {
        Method setPositionMethod = shapeClass.getMethod("setPosition", Shape.class, int.class,
            int.class);
        setPositionMethod.invoke(null, shape, row, col);
      } catch (NoSuchMethodException e) {
        // If there is no custom positioning method, that is acceptable.
        LOGGER.log(Level.FINE,
            "No custom setPosition method found for {0}; using default placement.", className);
      }

      return shape;
    } catch (ClassNotFoundException e) {
      LOGGER.log(Level.SEVERE,
          "Shape class not found for type: " + shapeType + ". Falling back to RectangularShape.",
          e);
    } catch (NoSuchMethodException e) {
      LOGGER.log(Level.SEVERE, "Constructor (int, int, int) not found for shape: " + shapeType
          + ". Falling back to RectangularShape.", e);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unexpected error while creating shape for type: " + shapeType
          + ". Falling back to RectangularShape.", e);
    }

    // Default fallback to RectangularShape in case of failure
    return new RectangularShape(size, row, col);
  }
}
