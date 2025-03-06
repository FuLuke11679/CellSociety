package cellsociety.model.grid.neighborhood;

import cellsociety.model.grid.shape.CellShape;
import java.util.List;

/**
 * Neighborhood strategy that returns all neighbor offsets (including diagonals).
 */
public class ExtendedMooreNeighborhood implements NeighborhoodStrategy {

  @Override
  public List<int[]> getFinalOffsets(CellShape shape, int row, int col) {
    // Use the complete set of offsets defined in the shape.
    return shape.getNeighborOffsets(row, col);
  }
}