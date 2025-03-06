package cellsociety.model.grid.handler;

import cellsociety.model.cell.Cell;
import cellsociety.model.grid.Grid;
import java.util.ArrayList;
import java.util.List;

public class ToroidalEdgeHandler implements EdgeHandler {

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
