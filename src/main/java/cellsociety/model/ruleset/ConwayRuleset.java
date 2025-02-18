package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.grid.ConwayGrid;
import cellsociety.model.grid.Grid;
import java.util.List;

/**
 * Author: Daniel Rodriguez-Florido
 * The back-end ruleset logic to preform the Conway Game of Life Simulation
 */

public class ConwayRuleset extends Ruleset {

  public ConwayRuleset() {
  }

  public void updateCellState(Cell cell, List<Cell> neighbors) {
    int aliveCells = countAliveNeighbors(neighbors);

    if (cell.getCurrState() == ConwayState.ALIVE) {
      // Live cell survives with 2 or 3 neighbors, otherwise it dies
      if (aliveCells < 2 || aliveCells > 3) {
        killCell(cell);
      } else {
        maintainCell(cell);
      }
    } else {
      // Dead cell becomes alive if it has exactly 3 neighbors
      if (aliveCells == 3) {
        birthCell(cell);
      } else {
        maintainCell(cell);  // Explicitly keep it dead if it doesn't meet the condition
      }
    }
  }

  @Override
  public void updateGridState() {
  }

  /**
   * Function to count the amount of alive neighbors of a cell
   * @param neighbors The list of neighbor cells
   * @return An integer denoting the amount of alive cells
   */
  protected int countAliveNeighbors(List<Cell> neighbors) {
    int aliveCells = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == ConwayState.ALIVE) { //If the cell is alive
        aliveCells++;
      }
    }
    return aliveCells;
  }

  /**
   * Sets the next state of a cell to DEAD
   * @param cell The cell we wish to kill
   */
  protected void killCell(Cell cell) {
    cell.setNextState(ConwayState.DEAD);
  }

  /**
   * Sets the next state of a cell to ALIVE
   * @param cell The cell we wish to birth
   */
  protected void birthCell(Cell cell) {
    cell.setNextState(ConwayState.ALIVE);
  }

  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    return new ConwayGrid(rows, columns, this, initialStates);
  }

}
