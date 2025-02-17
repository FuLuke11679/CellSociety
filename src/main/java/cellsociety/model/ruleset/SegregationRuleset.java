package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.SegregationGrid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SegregationRuleset extends Ruleset {

  private final double similarityThreshold;
  private SegregationGrid myGrid;
  List<Cell> emptyCells;

  public SegregationRuleset(double similarityThreshold) {
    this.similarityThreshold = similarityThreshold;
    emptyCells = new ArrayList<>();
  }

  public void updateCellState(Cell cell, List<Cell> neighbors) {
  }

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

    maintainRestOfGrid();

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

  /**
   * Function that moves all of the current states into next states for unaffected cells
   */
  protected void maintainRestOfGrid() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        if (myGrid.getCell(i, j).getNextState() == null) {
          maintainCell(myGrid.getCell(i, j));
        }
      }
    }
  }

  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    myGrid = new SegregationGrid(rows, columns, this, initialStates);
    return myGrid;
  }

}
