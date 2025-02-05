package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.PercolationCell;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.state.CellState;
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

  private boolean hasPercolatedNeighbor(Cell cell, List<Cell> neighbors) {
    for (Cell neighbor : neighbors) {
      if (getState(cell, neighbor) == PercolationState.PERCOLATED) {
        System.out.println("yo whatup");
        return true;
      }
    }
    System.out.println("updog");
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
