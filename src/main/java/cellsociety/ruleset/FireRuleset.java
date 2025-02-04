package cellsociety.ruleset;

import cellsociety.cell.Cell;
import cellsociety.cell.FireCell.FireState;
import java.util.List;
import cellsociety.state.CellState;
import javafx.scene.paint.Color;

public class FireRuleset extends Ruleset {

  private static final Color TREE_COLOR = Color.GREEN;
  private static final Color BURNING_COLOR = Color.BROWN;
  private static final Color EMPTY_COLOR = Color.YELLOW;

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

  private boolean isNeighborCellBurning(Cell cell, List<Cell> neighbors) {
    for (Cell neighbor : neighbors) {
      if (getState(cell, neighbor) == FireState.BURNING) {
        return true;
      }
    }
    return false;
  }

  private CellState getState(Cell cell, Cell neighbor) {
    if (neighbor.getId() < cell.getId()) {
      return neighbor.getPrevState();
    }
    return neighbor.getCurrState();
  }

  private void killCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(FireState.EMPTY);
    cell.setColor(EMPTY_COLOR);
  }

  private void lightFire(Cell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(FireState.BURNING);
    cell.setColor(BURNING_COLOR);
  }

  private void growTree(Cell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(FireState.TREE);
    cell.setColor(TREE_COLOR);
  }

}
