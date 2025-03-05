package cellsociety.model.grid;

import cellsociety.model.grid.shape.CellShape;
import cellsociety.model.grid.shape.RectangularShape;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CellShapeFactory {

  private static final Logger LOGGER = Logger.getLogger(CellShapeFactory.class.getName());

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
