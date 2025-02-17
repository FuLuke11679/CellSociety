package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.WatorGrid;
import cellsociety.model.state.CellState;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatorRuleset extends Ruleset {

  private static final int MAX_SHARK_LIFE = 10;
  private static final int MAX_FISH_LIFE = 10;
  private static final int SHARK_REPRODUCTION_TIME = 5;
  private static final int FISH_REPRODUCTION_TIME = 5;

  private Grid myGrid;
  private Map<Cell, Integer> energyMap;

  public WatorRuleset() {
    super();
  }

  /**
   * @param cell      The cell whose state me must check
   * @param neighbors The neighbors who determine what the cell should do
   */
  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {

    if (cell.getNextState() == WatorState.WATER) {
      maintainCell(cell);
      return;
    }

    if (cell.getNextState() == WatorState.FISH) {
      if (energyMap.containsKey(cell)) {
        Cell toSwap = getRandomEmptySpot(cell, neighbors);
        if (toSwap != null && energyMap.get(cell) % FISH_REPRODUCTION_TIME == 0) {
            reproduceCell(cell, toSwap);
        } else if (toSwap != null) {
          swapCell(cell, toSwap);
          energyMap.put(cell, energyMap.get(cell) - 1); // Take away energy from fish
        }
      }
    }

  }

  /**
   *
   */
  @Override
  public void updateGridState() {

  }

  private Cell getRandomEmptySpot(Cell cell, List<Cell> neighbors) {
    Collections.shuffle(neighbors);
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == WatorState.WATER) {
        return cell;
      }
    }
    return null;
  }

  private Cell getRandomFish(Cell cell, List<Cell> neighbors) {
    Collections.shuffle(neighbors);
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == WatorState.FISH) {
        return cell;
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
    cell1.setCurrState(cell1.getNextState());
    cell2.setCurrState(cell2.getNextState());

    cell1.setNextState(cell2.getNextState());
    cell2.setNextState(cell1.getCurrState());

    energyMap.put(cell1, energyMap.get(cell1));
    energyMap.remove(cell2);
  }

  private void reproduceCell(Cell cell, Cell child) {
    child.setCurrState(child.getNextState());
    child.setNextState(cell.getNextState());
    energyMap.put(child, MAX_SHARK_LIFE);
  }

  @Override
  protected CellState getState(Cell cell, Cell neighbor) {
    if (neighbor.getId() < cell.getId() || isCellWrapped(cell, neighbor)) {
      return neighbor.getCurrState();
    }
    return neighbor.getNextState();
  }

  private boolean isCellWrapped(Cell cell, Cell neighbor) {
    if (cell.getId() >= 0 && cell.getId() < myGrid.getColumns()
        && neighbor.getId() >= myGrid.getRows() * myGrid.getColumns()) {
      return true;
    }
    return cell.getId() % myGrid.getColumns() == 0
        && neighbor.getId() % myGrid.getColumns() == myGrid.getColumns() - 1;
  }

  private void generateLifeMap(Grid grid) {
    energyMap = new HashMap<>();
    for (int i = 0; i < grid.getRows(); i++) {
      for (int j = 0; j < grid.getColumns(); j++) {
        if (grid.getCell(i, j).getNextState() == WatorState.FISH) {
          energyMap.put(grid.getCell(i, j), MAX_FISH_LIFE);
        } else if (grid.getCell(i, j).getNextState() == WatorState.SHARK) {
          energyMap.put(grid.getCell(i, j), MAX_SHARK_LIFE);
        }
      }
    }
  }

  /**
   * Creates the grid for WatorWorld
   *
   * @param rows          Integer: The amount of rows in the grid
   * @param columns       Integer: The amount of columns in the grid
   * @param initialStates A String[] denoting the initial states of each cell
   * @return The new grid
   */
  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    myGrid = new WatorGrid(rows, columns, new WatorRuleset(), initialStates);
    generateLifeMap(myGrid);
    return myGrid;
  }

}
