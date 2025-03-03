package cellsociety.view.shapes;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Arrays;
import javafx.scene.shape.Shape;

public class ShapeFactory {

  private static final List<String> AVAILABLE_SHAPES = Arrays.asList("Rectangular", "Hexagonal",
      "Triangular");

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

      // Get the fully qualified class name dynamically
      String className = getFullyQualifiedName(shapeType);
      if (className == null) {
        throw new IllegalArgumentException("Generated class name is null.");
      }

      // Load the class dynamically using reflection
      Class<?> shapeClass = Class.forName(className);
      Shape shape = (Shape) shapeClass.getDeclaredConstructor(int.class, int.class, int.class).newInstance(size, row, col);

      // Use reflection to find and call the setPosition() method if it exists
      try {
        Method setPositionMethod = shapeClass.getMethod("setPosition", Shape.class, int.class, int.class);
        setPositionMethod.invoke(null, shape, row, col);
      } catch (NoSuchMethodException ignored) {
        // If the shape does not have a custom positioning method, it will use default placement
      }

      return shape;
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

    // Default fallback to RectangularShape in case of failure
    return new RectangularShape(size, row, col);
  }

}
