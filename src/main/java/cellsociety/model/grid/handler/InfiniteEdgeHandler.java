package cellsociety.model.grid.handler;

import cellsociety.model.cell.Cell;
import cellsociety.model.grid.Grid;
import java.util.List;

public class InfiniteEdgeHandler implements EdgeHandler {

  @Override
  public List<Cell> handleNeighbors(int row, int col, List<int[]> offsets, Grid grid) {
    if (!grid.isValidPosition(row, col)) {
      grid.expandGrid(row, col); // Dynamically expands grid size
    }
    return grid.getNeighbors(row, col); // Fetch neighbors from updated grid
  }

}