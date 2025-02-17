package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.WatorGrid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatorRuleset extends Ruleset {

  private static final int MAX_SHARK_ENERGY = 10;
  private static final int MAX_FISH_ENERGY = 10;
  private static final int SHARK_REPRODUCTION_TIME = 5;
  private static final int FISH_REPRODUCTION_TIME = 5;

  private WatorGrid myGrid;
  private List<Cell> fishCells;
  private List<Cell> sharkCells;
  private Map<Cell, Integer> fishEnergyMap;
  private Map<Cell, Integer> sharkEnergyMap;

  public WatorRuleset() {
    super();
  }

  /**
   * @param cell      The cell whose state me must check
   * @param neighbors The neighbors who determine what the cell should do
   */
  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {



  }

  /**
   *
   */
  @Override
  public void updateGridState() {

//    sharkCells = new ArrayList<>(sharkEnergyMap.keySet());
    fishCells = new ArrayList<>(fishEnergyMap.keySet());

    killDeadCells();

//    for (Cell c : sharkCells) {
//      moveShark(c);
//    }

    for (Cell c : fishCells) {
      moveFish(c);
    }

    maintainRestOfGrid();

  }

  private void moveFish(Cell cell) {
    int cellRow = cell.getId() / myGrid.getRows();
    int cellCol = cell.getId() % myGrid.getRows();
    List<Cell> neighbors = myGrid.getNeighbors(cellRow, cellCol);
    Cell toMove = getRandomEmptySpot(neighbors);
    if (toMove != null) {
      swapFishAndEmptyCell(cell, toMove);
    }
  }

  private void moveShark(Cell cell) {
    int cellRow = cell.getId() / myGrid.getRows();
    int cellCol = cell.getId() % myGrid.getRows();
    List<Cell> neighbors = myGrid.getNeighbors(cellRow, cellCol);
    Cell toMove = getRandomFishOrEmptySpot(neighbors);

    if (toMove != null) {
      System.out.println("Shark Cell ID: " + cell.getId());
      System.out.println("ToMove Cell ID: " + toMove.getId());
      System.out.println();
      if (toMove.getCurrState() == WatorState.FISH) {
        sharkEatFish(cell, toMove);
      } else {
        swapSharkAndEmptyCell(cell, toMove);
      }
    }
  }

  private Cell getRandomEmptySpot(List<Cell> neighbors) {
    Collections.shuffle(neighbors);
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == WatorState.WATER && neighbor.getNextState() == null) {
        return neighbor;
      }
    }
    return null;
  }

  private Cell getRandomFishOrEmptySpot(List<Cell> neighbors) {
    Collections.shuffle(neighbors);
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == WatorState.FISH && neighbor.getNextState() == null) {
        return neighbor;
      }
    }
    return getRandomEmptySpot(neighbors);
  }

  /**
   * Function to "swap" two cells in a grid
   *
   * @param fish The fish cell we wish to swap
   * @param emptyCell The empty cell we wish to swap it with
   */
  private void swapFishAndEmptyCell(Cell fish, Cell emptyCell) {

    System.out.println("Fish ID: " + fish.getId() + " Swap ID: " + emptyCell.getId());

    fish.setNextState(emptyCell.getCurrState());
    emptyCell.setNextState(fish.getCurrState());

    if (fishEnergyMap.get(fish) > 1) { // If the fish has enough energy to live on
      fishEnergyMap.put(emptyCell, fishEnergyMap.get(fish) - 1);
    }
    fishEnergyMap.remove(fish);

  }

  /**
   * Function to "swap" two cells in a grid
   *
   * @param shark The shark cell we wish to swap
   * @param emptyCell The empty cell we wish to swap it with
   */
  private void swapSharkAndEmptyCell(Cell shark, Cell emptyCell) {
    shark.setNextState(emptyCell.getCurrState());
    emptyCell.setNextState(shark.getCurrState());

    if (sharkEnergyMap.get(shark) > 1) { // If the fish has enough energy to live on
      sharkEnergyMap.put(emptyCell, sharkEnergyMap.get(shark) - 1);
    }
    sharkEnergyMap.remove(shark);
  }

  /**
   * Makes shark eat a fish that is neighboring
   *
   * @param shark The shark cell we wish to swap
   * @param fish The fish cell we wish to eat
   */
  private void sharkEatFish(Cell shark, Cell fish) {
    fish.setNextState(shark.getCurrState()); // Change the fish cell to shark cell
    fish.setNextState(WatorState.WATER);

    fishEnergyMap.remove(fish);

    if (sharkEnergyMap.get(shark) > 1) { // If the shark has enough energy to live on
      sharkEnergyMap.put(fish, sharkEnergyMap.get(shark) - 1); // TODO: Add energy for eating fish
    }
    sharkEnergyMap.remove(shark);
  }

  private void reproduceCell(Cell cell, Cell child) {
    child.setCurrState(child.getNextState());
    child.setNextState(cell.getNextState());
    sharkEnergyMap.put(child, MAX_SHARK_ENERGY);
  }

  private void generateLifeMap(Grid grid) {
    fishCells = new ArrayList<>();
    fishEnergyMap = new HashMap<>();
    sharkCells = new ArrayList<>();
    sharkEnergyMap = new HashMap<>();
    for (int i = 0; i < grid.getRows(); i++) {
      for (int j = 0; j < grid.getColumns(); j++) {
        Cell cellToAdd = grid.getCell(i, j);
        if (cellToAdd.getCurrState() == WatorState.FISH) {
          fishCells.add(cellToAdd);
          fishEnergyMap.put(cellToAdd, MAX_FISH_ENERGY);
        } else if (cellToAdd.getCurrState() == WatorState.SHARK) {
          sharkCells.add(cellToAdd);
          sharkEnergyMap.put(cellToAdd, MAX_SHARK_ENERGY);
        }
      }
    }
  }

  /**
   * Function that moves all the current states into next states for unaffected cells
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

  private void killDeadCells() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        Cell currCell = myGrid.getCell(i, j);
        if (!fishEnergyMap.containsKey(currCell)) {
          currCell.setCurrState(WatorState.WATER);
        }
        if (!sharkEnergyMap.containsKey(currCell)) {
          currCell.setCurrState(WatorState.WATER);
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
    myGrid = new WatorGrid(rows, columns, this, initialStates);
    generateLifeMap(myGrid);
    return myGrid;
  }

}
