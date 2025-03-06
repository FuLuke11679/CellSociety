package cellsociety.model.grid.handler;

import cellsociety.model.cell.Cell;
import cellsociety.model.grid.Grid;
import java.util.ArrayList;
import java.util.List;

/**
 * Edge handler implementation that simulates mirror boundaries. In a mirrored grid, if a neighbor’s
 * position is out-of-bounds, it is reflected back into the grid. This reflection is computed such
 * that the edge cells are "mirrored" to create a full neighborhood.
 *
 * @see EdgeHandler
 */
public class MirrorEdgeHandler implements EdgeHandler {

  /**
   * Computes a mirrored index given an index and the maximum valid index.
   *
   * @param index the original index (which might be out-of-bounds)
   * @param max   the size of the dimension (number of rows or columns)
   * @return a valid index obtained by reflecting the out-of-bound index
   */
  private int mirrorIndex(int index, int max) {
    if (index < 0) {
      return -index; // Reflect negative index (e.g. -1 becomes 1)
    } else if (index >= max) {
      return 2 * max - index - 2; // Reflect indices beyond the edge
    }
    return index;
  }

  /**
   * Handles the retrieval of neighbors using mirror reflection at the grid edges.
   * <p>
   * For each neighbor offset, the new row and column indices are computed using the mirrorIndex
   * method. This reflects the neighbor positions instead of wrapping them around.
   * </p>
   *
   * @param row     the row index of the target cell
   * @param col     the column index of the target cell
   * @param offsets a list of relative offsets representing potential neighbor positions
   * @param grid    the grid from which neighbors should be retrieved
   * @return a list of neighboring {@link Cell} objects computed using mirror reflection
   */
  @Override
  public List<Cell> handleNeighbors(int row, int col, List<int[]> offsets, Grid grid) {
    List<Cell> neighbors = new ArrayList<>();
    int numRows = grid.getRows();
    int numCols = grid.getColumns();

    for (int[] offset : offsets) {
      int candidateRow = row + offset[0];
      int candidateCol = col + offset[1];
      int newRow = mirrorIndex(candidateRow, numRows);
      int newCol = mirrorIndex(candidateCol, numCols);
      neighbors.add(grid.getCell(newRow, newCol));
    }
    return neighbors;
  }
}
