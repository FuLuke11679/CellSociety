package cellsociety.model.grid.neighborhood;

import java.util.List;
import cellsociety.model.grid.shape.CellShape;

/**
 * Neighborhood strategy that returns only the direct (orthogonal) neighbor offsets.
 */
public class VonNeumannNeighborhood implements NeighborhoodStrategy {
  @Override
  public List<int[]> getFinalOffsets(CellShape shape, int row, int col) {
    // Use only the inner offsets that represent directly touching neighbors.
    return shape.getInnerNeighborOffsets(row, col);
  }
}