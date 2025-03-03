package cellsociety.view.shapes;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class HexagonalShape extends Polygon {

  public HexagonalShape(int size, int row, int col) {
    double width = size * 0.9;
    double height = Math.sqrt(3) * width / 2;

    double xOffset = col * width * 0.75;
    double yOffset = row * height - (height / 2);  // Adjusted to move up slightly

    if (col % 2 == 1) {
      yOffset += height / 2;
    }

    getPoints().addAll(
        0.0, height / 2,
        width / 2, 0.0,
        width, height / 2,
        width, 1.5 * height,
        width / 2, 2 * height,
        0.0, 1.5 * height
    );

    setTranslateX(xOffset);
    setTranslateY(yOffset);
  }

  public static void setPosition(Shape shape, int row, int col, int size) {
    double width = size * 0.9;
    double height = Math.sqrt(3) * width / 2;

    double xOffset = col * width * 0.75;
    double yOffset = row * height - (height / 2);  // Adjusted

    if (col % 2 == 1) {
      yOffset += height / 2;
    }

    shape.setTranslateX(xOffset);
    shape.setTranslateY(yOffset);
  }
}
