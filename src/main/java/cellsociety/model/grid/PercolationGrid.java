package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell;
import cellsociety.model.cell.PercolationCell;
import cellsociety.model.ruleset.ConwayRuleset;
import cellsociety.model.ruleset.PercolationRuleset;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.List;

/*
Updates Grid based on Cell logic
Does not display the grid or interact at all with javafx packages (i.e Scene, Groups, etc)
 */
public class PercolationGrid extends Grid{

  /**
   * Constructor for ConwayGrid.
   *
   * @param rows       Number of rows in the grid.
   * @param columns    Number of columns in the grid.
   */

  public PercolationGrid(int rows, int columns, Ruleset ruleset, String[] cells) {
    super(rows, columns, ruleset, cells);
  }

  /**
   * Initialize the grid with Cells
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
        Cell cell = new PercolationCell(count, null, initialState);
        row.add(cell);
        count++;
      }
      myGrid.add(row);
    }
  }

}
