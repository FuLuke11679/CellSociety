package cellsociety.model.grid.handler;

import cellsociety.model.cell.Cell;
import cellsociety.model.grid.Grid;
import java.util.ArrayList;
import java.util.List;

/**
 * Edge handler implementation for toroidal (wrap-around) boundaries. This handler computes neighbor
 * positions by wrapping around the grid edges.
 *
 * @author Luke
 * @see EdgeHandler
 */
public class ToroidalEdgeHandler implements EdgeHandler {

  /**
   * Handles the retrieval of neighbors using toroidal (wrap-around) boundaries. For each neighbor
   * offset, the new row and column indices are computed using modulo arithmetic, ensuring that
   * positions wrap around the grid.
   *
   * @param row     the row index of the target cell
   * @param col     the column index of the target cell
   * @param offsets a list of relative offsets representing potential neighbor positions
   * @param grid    the grid from which neighbors should be retrieved
   * @return a list of neighboring {@link Cell} objects computed using toroidal boundaries
   */
  public List<Cell> handleNeighbors(int row, int col, List<int[]> offsets, Grid grid) {
    List<Cell> neighbors = new ArrayList<>();
    int numRows = grid.getRows();
    int numCols = grid.getColumns();

    for (int[] offset : offsets) {
      int newRow = (row + offset[0] + numRows) % numRows;
      int newCol = (col + offset[1] + numCols) % numCols;
      neighbors.add(grid.getCell(newRow, newCol));
    }
    return neighbors;
  }

}
