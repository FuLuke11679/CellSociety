package cellsociety.model.grid;

import cellsociety.model.ruleset.Ruleset;

/*
Updates Grid based on Cell logic
Does not display the grid or interact at all with javafx packages (i.e Scene, Groups, etc)
 */
public class PercolationGrid extends Grid {

  /**
   * Constructor for PercolationGrid.
   *
   * @param rows    Number of rows in the grid.
   * @param columns Number of columns in the grid.
   */
  public PercolationGrid(int rows, int columns, Ruleset ruleset, String[] cells) {
    super(rows, columns, ruleset, cells);
  }

}
