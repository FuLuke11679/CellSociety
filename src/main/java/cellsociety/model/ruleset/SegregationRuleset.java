package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.SegregationGrid;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author: Daniel Rodriguez-Florido
 * The back-end ruleset logic to preform the Segregation Simulation
 */

public class SegregationRuleset extends Ruleset {

  private static final String THRESHOLD_PARAM_NAME = "thresh";

  private final double similarityThreshold;
  private SegregationGrid myGrid;

  /**
   * Constructor for the Segregation Ruleset
   * @param params Map of String, String for parameters relevant to simulation
   *               (thresh)
   */
  public SegregationRuleset(Map<String, String> params) {
    this.similarityThreshold = Double.parseDouble(params.getOrDefault(THRESHOLD_PARAM_NAME, "0.4"));
  }

  public void updateCellState(Cell cell, List<Cell> neighbors) {
  }

  /**
   * Updates the state of a grid in the case of a segregation simulation.
   *
   * This particular implementation amasses the empty cells and unsatisfied cells into two lists
   * and matches them accordingly to place the unsatisfied cells into a random empty spot.
   */
  @Override
  public void updateGridState() {
    List<Cell> emptyCells = getEmptyCells();
    List<Cell> unSatisfiedCells = getUnsatisfiedCells();

    Collections.shuffle(unSatisfiedCells);
    Collections.shuffle(emptyCells);

    for (Cell c : unSatisfiedCells) {
      if (emptyCells.isEmpty()) {
        break;
      }

      Cell randomEmptyCell = emptyCells.getLast();
      swapCell(c, randomEmptyCell);

      emptyCells.removeLast();
    }

    maintainRestOfGrid(myGrid);

  }

  /**
   * Function to check if the Segregation cell is satisfied with the "race" of its neighbors
   *
   * @param cell      The cell that is being analyzed
   * @param neighbors The neighbors of the cell that we need to analyze
   * @return If the cell is satisfied with the "race" of its neighbors
   */
  private boolean isCellSatisfied(Cell cell, List<Cell> neighbors) {
    if (cell.getCurrState() == SegregationState.EMPTY) {
      return true;
    }

    double simCount = 0;
    double totalCount = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == cell.getCurrState()) {
        simCount++;
      }
      if (neighbor.getCurrState() != SegregationState.EMPTY) {
        totalCount++;
      }
    }
    return totalCount == 0 || simCount / totalCount > similarityThreshold;
  }

  /**
   * Function to "swap" two cells in a grid
   *
   * @param cell1 The first cell we wish to swap
   * @param cell2 The cell we wish to swap the first cell with
   */
  private void swapCell(Cell cell1, Cell cell2) {
    cell1.setNextState(cell2.getCurrState());
    cell2.setNextState(cell1.getCurrState());
  }

  /**
   * Generates a list of the empty cells in the grid
   * @return A List of cells
   */
  private List<Cell> getEmptyCells() {
    List<Cell> emptyCellAux = new ArrayList<>();
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        if (myGrid.getCell(i, j).getCurrState() == SegregationState.EMPTY) {
          emptyCellAux.add(myGrid.getCell(i, j));
        }
      }
    }
    return emptyCellAux;
  }

  /**
   * Generates a List of the unsatisfied cells in the grid
   * @return A List of cells
   */
  private List<Cell> getUnsatisfiedCells() {
    List<Cell> unsatisfiedCells = new ArrayList<>();
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        Cell currCell = myGrid.getCell(i, j);
        if (!isCellSatisfied(currCell, myGrid.getNeighbors(i, j))) {
          unsatisfiedCells.add(currCell);
        }
      }
    }
    return unsatisfiedCells;
  }

  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    myGrid = new SegregationGrid(rows, columns, this, initialStates);
    return myGrid;
  }

  @Override
  public CellState getDefaultCellState() {
    return SegregationState.EMPTY;
  }


}
