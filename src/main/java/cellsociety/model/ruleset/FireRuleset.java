package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.grid.FireGrid;
import cellsociety.model.grid.Grid;
import cellsociety.model.state.CellState;
import java.util.List;
import java.util.Map;

/**
 * Author: Daniel Rodriguez-Florido
 * The back-end ruleset logic to preform the Forest Fire Simulation
 */

public class FireRuleset extends Ruleset {

  private static final String PROB_GROW_PARAM_NAME = "probGrow";
  private static final String PROB_CATCH_PARAM_NAME = "probCatch";

  private final double probGrow;
  private final double probCatch;

  /**
   * Constructor for the FireRuleset
   * @param params The map of relevant parameters
   */
  public FireRuleset(Map<String, String> params) {
    this.probGrow = Double.parseDouble(params.get(PROB_GROW_PARAM_NAME));
    this.probCatch = Double.parseDouble(params.get(PROB_CATCH_PARAM_NAME));
  }

  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {
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
    if (isNeighborCellBurning(neighbors)) {
      lightFire(cell);
    } else if (Math.random() < probCatch) {
      lightFire(cell);
    } else {
      maintainCell(cell);
    }

  }

  /**
   * Dummy function that is not used in this implementation
   */
  @Override
  public void updateGridState() {

  }

  /**
   * Function to check if a neighboring cell is burning
   * @param neighbors The neighbors of the target cell
   * @return A boolean denoting true for a burning neighbor, false otherwise
   */
  private boolean isNeighborCellBurning(List<Cell> neighbors) {
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == FireState.BURNING) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets a cell's next state to EMPTY
   * @param cell The target cell
   */
  private void killCell(Cell cell) {
    cell.setNextState(FireState.EMPTY);
  }

  /**
   * Sets a cell's next state to BURNING
   * @param cell The target cell
   */
  private void lightFire(Cell cell) {
    cell.setNextState(FireState.BURNING);
  }

  /**
   * Sets a cell's next state to TREE
   * @param cell The target cell
   */
  private void growTree(Cell cell) {
    cell.setNextState(FireState.TREE);
  }

  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    return new FireGrid(rows, columns, this, initialStates);
  }

  @Override
  public CellState getDefaultCellState() {
    return FireState.EMPTY;
  }


}
