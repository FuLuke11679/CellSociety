package cellsociety.view.shapes;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Represents a rectangular shape for use in a grid-based simulation.
 * The rectangle is positioned and sized based on the provided grid parameters.
 *
 * @author Luke
 */
public class RectangularShape extends Rectangle {

  /**
   * Constructs a RectangularShape with the specified size and grid coordinates.
   *
   * @param size the size of the rectangle (both width and height)
   * @param row  the row index in the grid
   * @param col  the column index in the grid
   */
  public RectangularShape(int size, int row, int col) {
    super(size, size);
    setTranslateX(col * size);
    setTranslateY(row * size - (size * 0.2));  // Adjusted to move up slightly
  }
}
