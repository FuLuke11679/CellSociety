package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.List;

public class WatorGrid extends Grid {

  public WatorGrid(int rows, int columns, Ruleset ruleset, String[] cells) {
    super(rows, columns, ruleset, cells);
  }

  /**
   * Method to initialize the grid with the proper cell type
   */

  @Override
  public List<Cell> getNeighbors(int row, int col) {
    List<Cell> neighbors = new ArrayList<>();
    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    for (int i = 0; i < 4; i++) {
      int numRows = getRows();
      int numCols = getColumns();
      List<List<Cell>> grid = getGrid();
      int newRow = (row + dx[i] + numRows) % numRows;  // Wrap row
      int newCol = (col + dy[i] + numCols) % numCols;  // Wrap column

      neighbors.add(grid.get(newRow).get(newCol));
    }
    return neighbors;
  }


}
