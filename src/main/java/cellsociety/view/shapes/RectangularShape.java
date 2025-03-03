package cellsociety.view.shapes;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class RectangularShape extends Rectangle {

  public RectangularShape(int size, int row, int col) {
    super(size, size);
    setTranslateX(col * size);
    setTranslateY(row * size - (size * 0.2));  // Adjusted to move up slightly
  }

  public static void setPosition(Shape shape, int row, int col, int size) {
    shape.setTranslateX(col * size);
    shape.setTranslateY(row * size - (size * 0.2));  // Adjusted
  }
}
