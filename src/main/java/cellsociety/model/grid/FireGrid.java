package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.FireCell;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.List;

/*
Updates Grid based on Cell logic
Does not display the grid or interact at all with javafx packages (i.e Scene, Groups, etc)
 */
public class FireGrid extends Grid {

  /**
   * Constructor for ConwayGrid.
   *
   * @param rows    Number of rows in the grid.
   * @param columns Number of columns in the grid.
   */

  public FireGrid(int rows, int columns, Ruleset ruleset, String[] cells) {
    super(rows, columns, ruleset, cells);
  }

}
