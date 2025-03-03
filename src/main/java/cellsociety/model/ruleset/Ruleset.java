package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.grid.Grid;
import cellsociety.model.state.CellState;
import java.util.List;

/**
 * Author: Daniel Rodriguez-Florido
 * The superclass for all the Rulesets
 */

public abstract class Ruleset {

  public Ruleset() {
  }

  /**
   * Function to update a cell state locally based on its neighbors
   * @param cell
   * @param neighbors
   */
  public abstract void updateCellState(Cell cell, List<Cell> neighbors);

  /**
   * Function to update a grid all at once for more simulations that need more context
   */
  public abstract void updateGridState();

  /**
   * Function to create the Grid for the given ruleset
   *
   * @param rows          The number of rows in the grid
   * @param columns       The number of columns in the grid
   * @param initialStates A String[] denoting the initial states of the cells in the grid
   * @return The Grid object we wish to act upon and display
   */
  public abstract Grid createGrid(int rows, int columns, String[] initialStates);

  /**
   * Maintains the state of a cell if it does not need updates
   *
   * @param cell The cell to maintain the state of
   */
  protected void maintainCell(Cell cell) {
    cell.setNextState(cell.getCurrState());
  }

  /**
   * Function that moves all the current states into next states for unaffected cells
   */
  protected void maintainRestOfGrid(Grid myGrid) {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        if (myGrid.getCell(i, j).getNextState() == null) {
          maintainCell(myGrid.getCell(i, j));
        }
      }
    }
  }

  public abstract CellState getDefaultCellState();
}
