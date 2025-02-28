package cellsociety.view.shapes;

import javafx.scene.shape.Polygon;

public class TriangularShape extends Polygon {
  public TriangularShape(int sideLength, int row, int col) {
    double width = sideLength;  // Base width of equilateral triangle
    double height = Math.sqrt(3) / 2 * sideLength; // Correct height of an equilateral triangle

    // Adjust column and row offsets
    double xOffset = col * width * 0.5; // Shift every other column
    double yOffset = row * height;

    // If row is even, alternate direction
    if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
      getPoints().addAll(
          xOffset, yOffset + height,
          xOffset + width / 2, yOffset,
          xOffset + width, yOffset + height
      );
    } else {
      getPoints().addAll(
          xOffset, yOffset,
          xOffset + width / 2, yOffset + height,
          xOffset + width, yOffset
      );
    }
  }
}
