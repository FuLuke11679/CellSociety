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

  @Override
  public void update() {
    getRuleset().updateGridState();
    // Second pass: Apply new states and update cells to utilize the next state
    moveNextStateToCurrent();
  }

  /**
   * A function that determines the four adjacent cells (N,E,S,W)
   * @param row The row index of the target cell
   * @param col The col index of a target cell
   * @return The four neighbors of a cell
   */
  @Override
  public List<Cell> getNeighbors(int row, int col) {
    Grid grid = getGrid();
    List<Cell> neighbors = new ArrayList<>();

    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    for (int i = 0; i < 4; i++) {
      int numRows = getRows();
      int numCols = getColumns();
      int newRow = (row + dx[i] + numRows) % numRows;  // Wrap row
      int newCol = (col + dy[i] + numCols) % numCols;  // Wrap column

      neighbors.add(grid.getCell(newRow, newCol));
    }
    return neighbors;
  }


}
