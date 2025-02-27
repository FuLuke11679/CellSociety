package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import java.util.ArrayList;
import java.util.List;

public class ToroidalEdgeHandler implements EdgeHandler {
  public List<Cell> getNeighbors(int row, int col, Grid grid, List<int[]> offsets) {
  List<Cell> neighbors = new ArrayList<>();
    int[] dx = {-1, -1, -1, 1, 1, 1, 0, 0};
    int[] dy = {-1, 0, 1, 0, -1, 1, 1, -1};

    for (int i = 0; i < dx.length; i++) {
      int numRows = grid.getRows();
      int numCols = grid.getColumns();
      int newRow = (row + dx[i] + numRows) % numRows;  // Wrap row
      int newCol = (col + dy[i] + numCols) % numCols;  // Wrap column

      neighbors.add(grid.getCell(newRow, newCol));
    }
    return neighbors;
  }

}
