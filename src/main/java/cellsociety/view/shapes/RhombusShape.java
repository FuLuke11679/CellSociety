package cellsociety.view.shapes;

import javafx.scene.shape.Polygon;

/**
 * Represents a Rhombus shape for use in a grid-based simulation. The Rhombus is sized and
 * positioned based on the given grid parameters.
 *
 * @author Luke
 */
public class RhombusShape extends Polygon {

  /**
   * Constructs a Rhombus with the given size and grid position.
   *
   * @param size the size of the hexagon
   * @param row  the row index in the grid
   * @param col  the column index in the grid
   */
  public RhombusShape(int size, int row, int col) {
    double s = size;
    double sqrt3 = Math.sqrt(3);

    // Define the rhombus vertices (lozenge) with side length s
    // The shape has an acute angle of 60° and an obtuse angle of 120°
    getPoints().addAll(
        0.0, 0.0,                   // Vertex 0
        s, 0.0,                     // Vertex 1
        1.5 * s, (s * sqrt3) / 2,     // Vertex 2
        0.5 * s, (s * sqrt3) / 2      // Vertex 3
    );

    // Calculate the translation offsets for tiling:
    // Use two basis vectors: one horizontal (s, 0) and one slanted (s/2, (sqrt3/2)*s)
    double xOffset = col * s + row * (s / 2);
    double yOffset = row * (s * sqrt3 / 2);

    setTranslateX(xOffset);
    setTranslateY(yOffset);
  }
}
