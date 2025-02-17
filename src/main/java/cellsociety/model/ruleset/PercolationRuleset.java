package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.PercolationGrid;
import java.util.List;

public class PercolationRuleset extends Ruleset {

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

}
