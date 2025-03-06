package cellsociety.model.grid;

import cellsociety.model.grid.shape.CellShape;
import cellsociety.model.grid.shape.RectangularShape;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class for creating instances of {@link CellShape}.
 * This class dynamically loads a specific {@code CellShape} implementation based on the provided shape type.
 * If the specified shape type is null, empty, or if any error occurs during instantiation,
 * it defaults to returning a {@link RectangularShape}.
 *
 * @author Luke
 */
public class CellShapeFactory {

  private static final Logger LOGGER = Logger.getLogger(CellShapeFactory.class.getName());

  /**
   * Creates and returns an instance of {@link CellShape} based on the given shape type.
   * <p>
   * The method attempts to dynamically load a class named
   * "cellsociety.model.grid.shape.&lt;shapeType&gt;Shape" using reflection. If the shape type is invalid
   * or any error occurs, a {@link RectangularShape} is returned as a fallback.
   * </p>
   *
   * @param shapeType the type of cell shape to create (e.g., "Rectangular", "Hexagonal")
   * @return an instance of the requested {@code CellShape}, or a default {@code RectangularShape} if creation fails
   */
  public static CellShape createCellShape(String shapeType) {
    try {
      if (shapeType == null || shapeType.trim().isEmpty()) {
        throw new IllegalArgumentException("Cell shape type is null or empty.");
      }

      String className = "cellsociety.model.grid.shape." + shapeType + "Shape";
      LOGGER.info("Attempting to load CellShape class: " + className);

      Class<?> shapeClass = Class.forName(className);
      return (CellShape) shapeClass.getDeclaredConstructor().newInstance();
    } catch (ClassNotFoundException e) {
      LOGGER.log(Level.SEVERE, "Error: CellShape class not found for type -> " + shapeType, e);
    } catch (NoSuchMethodException e) {
      LOGGER.log(Level.SEVERE, "Error: No default constructor found in class -> " + shapeType, e);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unexpected error creating CellShape -> " + shapeType, e);
    }
    // Default fallback to RectangularShape in case of failure
    return new RectangularShape();
  }
}
