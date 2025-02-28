package cellsociety.view.shapes;

import java.util.List;
import java.util.Arrays;
import javafx.scene.shape.Shape;

public class ShapeFactory {
  private static final List<String> AVAILABLE_SHAPES = Arrays.asList("Rectangular", "Hexagonal", "Triangular");

  public static List<String> getAvailableShapes() {
    return AVAILABLE_SHAPES;
  }

  public static String getFullyQualifiedName(String shapeName) {
    String className;
    if (shapeName.contains(".")) { // Already fully qualified
      className = shapeName;
    } else {
      className = "cellsociety.view.shapes." + shapeName + "Shape";
    }
    return className;
  }

  public static Shape createShape(String shapeType, int size, int row, int col) {
    try {
      if (shapeType == null || shapeType.trim().isEmpty()) {
        throw new IllegalArgumentException("Shape type is null or empty.");
      }

      String className = getFullyQualifiedName(shapeType);
      if (className == null) {
        throw new IllegalArgumentException("Generated class name is null.");
      }

      Class<?> shapeClass = Class.forName(className);
      return (Shape) shapeClass.getDeclaredConstructor(int.class, int.class, int.class).newInstance(size, row, col);
    } catch (ClassNotFoundException e) {
      System.err.println("Error: Shape class not found -> " + shapeType);
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      System.err.println("Error: Constructor (int, int, int) not found in class -> " + shapeType);
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("Unexpected error creating shape -> " + shapeType);
      e.printStackTrace();
    }
    return new RectangularShape(size, row, col); // Default shape fallback
  }
}
