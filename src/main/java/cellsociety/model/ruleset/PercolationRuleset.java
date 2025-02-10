package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.PercolationGrid;
import java.util.List;

public class PercolationRuleset extends Ruleset {

  public PercolationRuleset() {}

  @Override
  public void updateState(Cell cell, List<Cell> neighbors) {
    if (cell.getCurrState() == PercolationState.BLOCKED || cell.getCurrState() == PercolationState.PERCOLATED) {
      maintainCell(cell);
      return;
    }

    // The following code only applies to open cells
    if (hasPercolatedNeighbor(cell, neighbors)) {
      percolateCell(cell);
    } else {
      maintainCell(cell);
    }

  }

  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    return new PercolationGrid(rows, columns, new PercolationRuleset(), initialStates);
  }

  private boolean hasPercolatedNeighbor(Cell cell, List<Cell> neighbors) {
    for (Cell neighbor : neighbors) {
      if (getState(cell, neighbor) == PercolationState.PERCOLATED) {
        return true;
      }
    }
    return false;
  }

  private void maintainCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
  }

  private void percolateCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(PercolationState.PERCOLATED);
  }

}
