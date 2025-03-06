package cellsociety.view.shapes;

import javafx.scene.shape.Polygon;

/**
 * Represents a Pentagonal shape for use in a grid-based simulation.
 * The Pentagon is sized, flipped, and positioned based on the given grid parameters.
 *
 * @author Luke
 */
public class PentagonalTilingShape extends Polygon {

  /**
   * Constructs a PentagonTile with the given size and grid position.
   *
   * @param size the size of the hexagon
   * @param row  the row index in the grid
   * @param col  the column index in the grid
   */
  public PentagonalTilingShape(int size, int row, int col) {
    double s = size;
    double xOffset = col * s * 0.75;
    double yOffset = row * s + s / 2;
    // Define a house-shaped pentagon centered at (0,0).
    // The vertices (non-rotated) are:
    //   Bottom left, Bottom right, Right base of roof, Roof apex, Left base of roof.
    getPoints().addAll(
        -s / 2, -s / 2,   // Bottom left
        s / 2, -s / 2,    // Bottom right
        s / 2, 0.0,    // Right base (start of roof)
        0.0, s / 2,     // Roof apex
        -s / 2, 0.0    // Left base (end of roof)
    );

    // Set the base rotation to 90° so the house is horizontal.
    double rotation = 90;
    // Alternate orientation for (row + col) odd: add 180° (i.e. 90 or 270°)
    //Right Facing
    if ((col) % 2 != 0) {
      rotation += 180;
      yOffset += size / 2;
      xOffset += size * 0.25;
    }
    setRotate(rotation);

    // Translate the shape so that its center is at the center of a square cell.
    setTranslateX(xOffset);
    setTranslateY(yOffset);
  }
}
