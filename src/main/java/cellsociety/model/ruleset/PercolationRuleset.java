package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.PercolationGrid;
import cellsociety.model.state.CellState;
import java.util.List;

/**
 * Author: Daniel Rodriguez-Florido
 * The back-end ruleset logic to preform the Percolation Simulation
 */

public class PercolationRuleset extends Ruleset {

  /**
   * Empty constructor since no parameters necessary
   */
  public PercolationRuleset() {
  }

  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {
    if (cell.getCurrState() == PercolationState.BLOCKED
        || cell.getCurrState() == PercolationState.PERCOLATED) {
      maintainCell(cell);
      return;
    }

    // The following code only applies to open cells
    if (hasPercolatedNeighbor(neighbors)) {
      percolateCell(cell);
    } else {
      maintainCell(cell);
    }

  }

  /**
   * Empty function that does not need implementation for this simulation (strategy pattern)
   */
  @Override
  public void updateGridState() {
  }

  /**
   * Checks whether a cell has any neighboring cells that have been percolated
   * @param neighbors The neighbors of the target cell
   * @return True if the cell has percolated neighbors
   */
  private boolean hasPercolatedNeighbor(List<Cell> neighbors) {
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == PercolationState.PERCOLATED) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the next state of the cell to PERCOLATED
   * @param cell The target cell
   */
  private void percolateCell(Cell cell) {
    cell.setNextState(PercolationState.PERCOLATED);
  }

  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    return new PercolationGrid(rows, columns, this, initialStates);
  }

  @Override
  public CellState getDefaultCellState() {
    return PercolationState.OPEN;
  }


}
