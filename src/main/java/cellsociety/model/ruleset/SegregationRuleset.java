package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SegregationCell;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.SegregationGrid;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SegregationRuleset extends Ruleset {

  private final double similarityThreshold;
  private SegregationGrid myGrid;
  Set<Cell> emptyCells;

  public SegregationRuleset(double similarityThreshold) {
    this.similarityThreshold = similarityThreshold;
    emptyCells = new HashSet<>();
  }

  /**
   * Function to update the state of the cell based on its neighbors
   *
   * @param cell      The cell of focus that we are looking to update
   * @param neighbors The neighbors around the cell that we need to analyze
   */
  public void updateState(Cell cell, List<Cell> neighbors) {

    System.out.println("Number of empty cells: " + emptyCells.size());

    if (cell.getCurrState() == SegregationState.EMPTY) {
      maintainCell(cell);
      return;
    }

    if (cell.getNextState() != null) {
      return;
    }

    if (isCellSatisfied(cell, neighbors)) {
      maintainCell(cell);
    } else {
      Cell cellToSwap = findSuitableEmptyCell();
      if (cellToSwap != null) {
        swapCell(cell, cellToSwap);
      }
    }

  }

  /**
   * Function to check if the Segregation cell is satisfied with the "race" of its neighbors
   *
   * @param cell      The cell that is being analyzed
   * @param neighbors The neighbors of the cell that we need to analyze
   * @return If the cell is satisfied with the "race" of its neighbors
   */
  private boolean isCellSatisfied(Cell cell, List<Cell> neighbors) {
    double simCount = 0;
    double totalCount = 0;
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == cell.getCurrState()) {
        simCount++;
        totalCount++;
      } else if (neighbor.getCurrState() != SegregationState.EMPTY) {
        totalCount++;
      }
    }
    return simCount / totalCount > similarityThreshold;
  }

  /**
   * Shuffles the KeySet of empty cells and finds the first cell that has had a life cycle longer
   * than one update cycle (to account for sequential updates)
   *
   * @return A suitable empty cell to swap with
   */
  private Cell findSuitableEmptyCell() {
    List<Cell> shuffledCells = new ArrayList<>(emptyCells);
    Collections.shuffle(shuffledCells);
    for (Cell c : shuffledCells) {
      if (c.getCurrState() != SegregationState.EMPTY || c.getNextState() == SegregationState.EMPTY) {
        return c;
      }
    }
    return null;
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

    emptyCells.remove(cell2);
    emptyCells.add(cell1);
  }

  /**
   * Maintains the state of a cell if it does not need updates
   *
   * @param cell The cell to maintain the state of
   */
  private void maintainCell(Cell cell) {
    cell.setNextState(cell.getCurrState());
  }

  /**
   * Creates a custom grid with the appropriate cell type
   *
   * @param rows          The amount of rows in the grid
   * @param columns       The amount of columns in a grid
   * @param initialStates The initial states to be configured in the grid of each cell
   * @return The Grid of the simulation
   */
  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    myGrid = new SegregationGrid(rows, columns, this, initialStates);
    emptyCells = getEmptyCells(myGrid);
    return myGrid;
  }


  private Set<Cell> getEmptyCells(Grid grid) {
    Set<Cell> emptyCellAux = new HashSet<>();
    for (int i = 0; i < grid.getRows(); i++) {
      for (int j = 0; j < grid.getColumns(); j++) {
        if (grid.getCell(i, j).getCurrState() == SegregationState.EMPTY) {
          emptyCellAux.add(grid.getCell(i, j));
        }
      }
    }
    return emptyCellAux;
  }

}
