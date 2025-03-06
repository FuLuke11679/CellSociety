package cellsociety.model.grid.neighborhood;

import cellsociety.model.grid.shape.CellShape;
import java.util.List;

public interface NeighborhoodStrategy {

  /**
   * Computes the final neighbor offsets for a cell at (row, col) using the provided cell shape.
   *
   * @param shape the {@link CellShape} that provides the raw offsets
   * @param row   the row index of the cell
   * @param col   the column index of the cell
   * @return a list of integer arrays representing the final neighbor offsets
   */
  List<int[]> getFinalOffsets(CellShape shape, int row, int col);
}
