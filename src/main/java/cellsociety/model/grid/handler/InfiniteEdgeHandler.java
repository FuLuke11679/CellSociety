package cellsociety.model.grid.handler;

import cellsociety.model.cell.Cell;
import cellsociety.model.grid.Grid;
import java.util.List;

/**
 * Edge handler implementation for an infinite grid.
 * <p>
 * If the cell position is outside the current grid boundaries, this handler expands the grid dynamically,
 * then retrieves the neighbors.
 * </p>
 *
 * @see EdgeHandler
 */
public class InfiniteEdgeHandler implements EdgeHandler {
  /**
   * Handles the retrieval of neighbors by expanding the grid if necessary.
   * <p>
   * If the target cell position is invalid, the grid is expanded to include the position,
   * and then neighbors are retrieved from the updated grid.
   * </p>
   *
   * @param row     the row index of the cell
   * @param col     the column index of the cell
   * @param offsets a list of relative neighbor offsets
   * @param grid    the grid from which neighbors should be retrieved
   * @return a list of neighboring {@link Cell} objects from the updated grid
   */
  @Override
  public List<Cell> handleNeighbors(int row, int col, List<int[]> offsets, Grid grid) {
    if (!grid.isValidPosition(row, col)) {
      grid.expandGrid(row, col); // Dynamically expands grid size
    }
    return grid.getNeighbors(row, col); // Fetch neighbors from updated grid
  }

}