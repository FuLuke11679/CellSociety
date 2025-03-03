package cellsociety.view.shapes;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class TriangularShape extends Polygon {
  private static final double SCALE_FACTOR = 1.5;


  public TriangularShape(int size, int row, int col) {
    double width = size * SCALE_FACTOR;
    double height = Math.sqrt(3) / 2 * size;

    double xOffset = col * width * 0.5;
    double yOffset = row * height - (height * 0.25);  // Adjusted to move up

    if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
      getPoints().addAll(
          0.0, height,
          width / 2, 0.0,
          width, height
      );
    } else {
      getPoints().addAll(
          0.0, 0.0,
          width / 2, height,
          width, 0.0
      );
    }

    setTranslateX(xOffset);
    setTranslateY(yOffset);
  }

  public static void setPosition(Shape shape, int row, int col, int size) {
    double width = size;
    double height = Math.sqrt(3) / 2 * size;

    double xOffset = col * width * 0.5;
    double yOffset = row * height - (height * 0.25);  // Adjusted

    shape.setTranslateX(xOffset);
    shape.setTranslateY(yOffset);
  }
}
