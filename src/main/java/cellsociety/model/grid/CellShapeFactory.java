package cellsociety.model.grid;

import cellsociety.model.grid.shape.CellShape;
import cellsociety.model.grid.shape.RectangularShape;

public class CellShapeFactory {
  public static CellShape createCellShape(String shapeType) {
    try {
      if (shapeType == null || shapeType.isEmpty()) {
        throw new IllegalArgumentException("Cell shape type is null or empty.");
      }

      String className = "cellsociety.model.grid.shape." + shapeType + "Shape";
      System.out.println("Attempting to load CellShape class: " + className);

      Class<?> shapeClass = Class.forName(className);
      return (CellShape) shapeClass.getDeclaredConstructor().newInstance();
    } catch (ClassNotFoundException e) {
      System.err.println("Error: CellShape class not found -> " + shapeType);
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      System.err.println("Error: Constructor not found in class -> " + shapeType);
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("Unexpected error creating CellShape -> " + shapeType);
      e.printStackTrace();
    }
    return new RectangularShape(); // Default shape fallback
  }
}

