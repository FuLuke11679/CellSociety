package cellsociety.model.grid.neighborhood;

import java.util.List;
import cellsociety.model.grid.shape.CellShape;

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