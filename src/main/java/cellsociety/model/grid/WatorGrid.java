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
  public void initializeGrid() {
    myGrid = new ArrayList<>();
    int count = 0;
    for (int x = 0; x < rows; x++) {
      List<Cell> row = new ArrayList<>();
      for (int y = 0; y < columns; y++) {
        CellState initialState = getInitialState(myCells[count]);
        // Use the appropriate cell type based on the state
        Cell cell = new WatorCell(count, initialState, null);
        row.add(cell);
        count++;
      }
      myGrid.add(row);
    }
  }

  @Override
  public List<Cell> getNeighbors(int row, int col) {
    List<Cell> neighbors = new ArrayList<>();
    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    for (int i = 0; i < 8; i++) {
      int newRow = (row + dx[i] + rows) % rows;  // Wrap row
      int newCol = (col + dy[i] + columns) % columns;  // Wrap column

      neighbors.add(myGrid.get(newRow).get(newCol));
    }
    return neighbors;
  }


}
