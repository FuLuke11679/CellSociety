package cellsociety.view.shapes;

import javafx.scene.shape.Polygon;

/**
 * Represents a hexagonal shape for use in a grid-based simulation.
 * The hexagon is sized and positioned based on the given grid parameters.
 *
 * @author Luke
 */
public class HexagonalShape extends Polygon {

  /**
   * Constructs a HexagonalShape with the given size and grid position.
   *
   * @param size the size of the hexagon
   * @param row  the row index in the grid
   * @param col  the column index in the grid
   */
  public HexagonalShape(int size, int row, int col) {
    double width = size;  // Full width of a hexagon
    double height = Math.sqrt(3) * size / 2;  // Height calculated for flat-top hex

    // Correct x and y offsets for proper alignment
    double xOffset = .75 * col * width;  // Space horizontally by 1.5 times the width
    double yOffset = row * height;   // Evenly spaced rows

    if (col % 2 == 1) {  // Offset every other column downward
      yOffset += height / 2;
    }

    // Define the hexagon with flat sides at the top and bottom
    getPoints().addAll(
        width * 0.25, 0.0,   // Top left
        width * 0.75, 0.0,   // Top right
        width, height / 2,   // Middle right
        width * 0.75, height, // Bottom right
        width * 0.25, height, // Bottom left
        0.0, height / 2       // Middle left
    );

    setTranslateX(xOffset);
    setTranslateY(yOffset);
  }
}
