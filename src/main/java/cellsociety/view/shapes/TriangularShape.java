package cellsociety.view.shapes;

import javafx.scene.shape.Polygon;

public class TriangularShape extends Polygon {

  private static final double SCALE_FACTOR = 1.5;

  /**
   * Constructs a Triangle with the given size and grid position.
   *
   * @param size the size of the hexagon
   * @param row  the row index in the grid
   * @param col  the column index in the grid
   */
  public TriangularShape(int size, int row, int col) {
    double width = size * SCALE_FACTOR;
    double height = Math.sqrt(3) / 2 * size;

    double xOffset = col * width * 0.5;
    double yOffset = row * height - (height * 0.25);  // Adjusted to move up

    if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
      //Upright
      getPoints().addAll(
          0.0, height,
          width / 2, 0.0,
          width, height
      );
      //Upside Down
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
}
