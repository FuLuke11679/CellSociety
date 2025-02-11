package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.grid.FireGrid;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.SegregationGrid;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SegregationRuleset extends Ruleset {

  private final double similarityThreshold;
  private SegregationGrid grid;
  Map<Cell, Integer> emptyCells;

  public SegregationRuleset(double similarityThreshold) {
    this.similarityThreshold = similarityThreshold;
    emptyCells = new HashMap<>();
  }

  /**
   * @param cell The cell of focus that we are looking to update
   * @param neighbors
   */
  public void updateState(Cell cell, List<Cell> neighbors) {
    if (cell.getCurrState() == SegregationState.EMPTY) {
      if (emptyCells.containsKey(cell)) {
        emptyCells.put(cell, emptyCells.get(cell) + 1);
      } else {
        emptyCells.put(cell, 1);
      }
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

  private boolean isCellSatisfied(Cell cell, List<Cell> neighbors) {
    double simCount = 0;
    double totalCount = 0;
    for (Cell neighbor : neighbors) {
      if (getState(cell, neighbor) == cell.getCurrState()) {
        simCount++;
        totalCount++;
      } else if (getState(cell, neighbor) != SegregationState.EMPTY) {
        totalCount++;
      }
    }
    return simCount/totalCount > similarityThreshold;
  }

  private Cell findSuitableEmptyCell() {
    List<Cell> candidateCells = new ArrayList<>(emptyCells.keySet());
    Collections.shuffle(candidateCells);
    for (Cell c : candidateCells) {
      if (emptyCells.get(c) >= 1) {
        return c;
      }
    }
    return null;
  }

  @Override
  public CellState getState(Cell cell, Cell neighbor) {
    if (neighbor.getId() < cell.getId() || (emptyCells.containsKey(neighbor) && emptyCells.get(neighbor) < 1)) {
      return neighbor.getPrevState();
    }
    return neighbor.getCurrState();
  }

  private void swapCell(Cell cell1, Cell cell2) {
    cell1.setPrevState(cell1.getCurrState());
    cell2.setPrevState(cell2.getCurrState());

    cell1.setCurrState(cell2.getCurrState());
    cell2.setCurrState(cell1.getPrevState());

    emptyCells.remove(cell2);
    emptyCells.put(cell1, 0);
  }

  private void maintainCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
  }

  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    grid = new SegregationGrid(rows, columns, new SegregationRuleset(similarityThreshold), initialStates);
    emptyCells = grid.getEmptyCells();
    return grid;
  }

}
