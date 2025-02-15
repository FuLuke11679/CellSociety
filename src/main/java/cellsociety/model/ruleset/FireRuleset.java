package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.grid.FireGrid;
import cellsociety.model.grid.Grid;
import java.util.List;

public class FireRuleset extends Ruleset {

  private final double probGrow;
  private final double probCatch;

  public FireRuleset(double probGrow, double probCatch) {
    this.probGrow = probGrow;
    this.probCatch = probCatch;
  }

  @Override
  public void updateState(Cell cell, List<Cell> neighbors) {
    if (cell.getCurrState() == FireState.EMPTY) {
      if (Math.random() < probGrow) {
        growTree(cell);
      } else {
        maintainCell(cell);
      }
      return;
    }

    if (cell.getCurrState() == FireState.BURNING) {
      killCell(cell);
      return;
    }

    // The following code will only execute if tree
    if (isNeighborCellBurning(cell, neighbors)) {
      lightFire(cell);
    } else if (Math.random() < probCatch) {
      lightFire(cell);
    }
  }

  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    return new FireGrid(rows, columns, new FireRuleset(probGrow, probCatch), initialStates);
  }

  private boolean isNeighborCellBurning(Cell cell, List<Cell> neighbors) {
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == FireState.BURNING) {
        return true;
      }
    }
    return false;
  }

  private void killCell(Cell cell) {
    cell.setNextState(FireState.EMPTY);
  }

  private void lightFire(Cell cell) {
    cell.setNextState(FireState.BURNING);
  }

  private void growTree(Cell cell) {
    cell.setNextState(FireState.TREE);
  }

  private void maintainCell(Cell cell) {
    cell.setNextState(cell.getCurrState());
  }

}
