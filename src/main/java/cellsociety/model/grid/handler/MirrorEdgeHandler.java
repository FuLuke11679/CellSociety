package cellsociety.model.grid.handler;

import cellsociety.model.cell.Cell;
import cellsociety.model.grid.Grid;
import java.util.ArrayList;
import java.util.List;

/**
 * Edge handler implementation that simulates mirror boundaries.
 * This handler reflects the cell positions at the grid edges, creating a mirrored effect.
 * @author Luke
 *
 * @see EdgeHandler
 */
public class MirrorEdgeHandler implements EdgeHandler {
  /**
   * Handles the retrieval of neighbors using mirror reflection at the grid edges.
   * <p>
   * For each neighbor offset, the new position is computed by reflecting around the boundaries.
   * </p>
   *
   * @param row     the row index of the target cell
   * @param col     the column index of the target cell
   * @param offsets a list of relative offsets representing potential neighbor positions
   * @param grid    the grid from which neighbors should be retrieved
   * @return a list of neighboring {@link Cell} objects computed using mirror reflection
   */
  public List<Cell> handleNeighbors(int row, int col, List<int[]> offsets, Grid grid) {
    List<Cell> neighbors = new ArrayList<>();
    int numRows = grid.getRows();
    int numCols = grid.getColumns();

    for(int[] offset : offsets) {
      int newRow = (row + offset[0] + numRows) % numRows;
      int newCol = (row + offset[1] + numCols) % numCols;
      neighbors.add(grid.getCell(newRow, newCol));
    }
    return neighbors;
  }

}