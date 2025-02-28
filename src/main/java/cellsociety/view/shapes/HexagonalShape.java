package cellsociety.view.shapes;

import javafx.scene.shape.Polygon;

public class HexagonalShape extends Polygon {
  public HexagonalShape(int size, int row, int col) {
    double width = size * 0.9;  // Reduce size slightly to prevent overlap
    double height = Math.sqrt(3) * width / 2;

    // Offset odd rows
    double xOffset = col * width * 0.75;
    double yOffset = row * height;
    if (col % 2 == 1) {
      yOffset += height / 2;
    }

    getPoints().addAll(
        xOffset, yOffset + height / 2,
        xOffset + width / 2, yOffset,
        xOffset + width, yOffset + height / 2,
        xOffset + width, yOffset + 1.5 * height,
        xOffset + width / 2, yOffset + 2 * height,
        xOffset, yOffset + 1.5 * height
    );
  }
}

