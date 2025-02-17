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

/**
 * Author: Daniel Rodriguez-Florido
 * The back-end ruleset logic to preform the WatorWorld Simulation
 */

public class WatorRuleset extends Ruleset {

  private static final int FISH_ENERGY_VALUE = 5;

  private WatorGrid myGrid;
  private List<Cell> fishCells;
  private List<Cell> sharkCells;
  private Map<Cell, Integer> fishEnergyMap;
  private Map<Cell, Integer> sharkEnergyMap;
  private Map<Cell, Integer> fishReproductionMap;
  private Map<Cell, Integer> sharkReproductionMap;

  private final int maxSharkEnergy;
  private final int maxFishEnergy;
  private final int sharkReproductionTime;
  private final int fishReproductionTime;


  /**
   * Constructor for the WatorRuleset
   * @param fishBreedTime The amount of chronons (time units) it takes for a fish to reproduce
   * @param fishStarveTime The amount of chronons it takes for a fish to die
   * @param sharkBreedTime The amount of chronons it takes for a shark to reproduce
   * @param sharkStarveTime The amount of chronons it takes for a shark to die
   */
  public WatorRuleset(int fishBreedTime, int fishStarveTime, int sharkBreedTime, int sharkStarveTime) {
    super();
    fishReproductionTime = fishBreedTime;
    maxFishEnergy = fishStarveTime;
    sharkReproductionTime = sharkBreedTime;
    maxSharkEnergy = sharkStarveTime;
  }

  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {
  }

  /**
   * Updates sharks, then fish, and maintains any unaffected cell.
   */
  @Override
  public void updateGridState() {

    sharkCells = new ArrayList<>(sharkEnergyMap.keySet());
    killDeadCells();

    for (Cell c : sharkCells) {
      moveShark(c);
    }

    fishCells = new ArrayList<>(fishEnergyMap.keySet());
    killDeadCells();

    for (Cell c : fishCells) {
      moveFish(c);
    }

    maintainRestOfGrid();

  }

  /**
   * Moves a fish from its original spot to a new random empty space
   * @param cell The fish cell we wish to move
   */
  private void moveFish(Cell cell) {
    int cellRow = cell.getId() / myGrid.getRows();
    int cellCol = cell.getId() % myGrid.getRows();
    List<Cell> neighbors = myGrid.getNeighbors(cellRow, cellCol);
    Cell toMove = getRandomEmptySpot(neighbors);
    if (toMove != null) {
      swapFishAndEmptyCell(cell, toMove);
    }
  }

  /**
   * Moves a shark from its original spot to a random fish space or empty space if no fish around
   * @param cell The shark cell we wish to move
   */
  private void moveShark(Cell cell) {
    int cellRow = cell.getId() / myGrid.getRows();
    int cellCol = cell.getId() % myGrid.getRows();
    List<Cell> neighbors = myGrid.getNeighbors(cellRow, cellCol);
    Cell toMove = getRandomFishOrEmptySpot(neighbors);

    if (toMove != null) {
      if (toMove.getCurrState() == WatorState.FISH) {
        sharkEatFish(cell, toMove);
      } else {
        swapSharkAndEmptyCell(cell, toMove);
      }
    }
  }

  /**
   * Gets a random empty neighbor to move a cell to
   * @param neighbors The neighbors of the cell to check
   * @return An empty Cell that is the candidate for swapping to
   */
  private Cell getRandomEmptySpot(List<Cell> neighbors) {
    Collections.shuffle(neighbors);
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == WatorState.WATER && neighbor.getNextState() == null) {
        return neighbor;
      }
    }
    return null;
  }

  /**
   * For sharks to find a random spot to move to. Attempts to find fish and if does not find fish
   * then returns a random empty spot to move to.
   * @param neighbors The neighbors of the shark cell
   * @return The cell the shark can move to
   */
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
    fish.setNextState(emptyCell.getCurrState());
    emptyCell.setNextState(fish.getCurrState());

    if (fishEnergyMap.get(fish) > 1) { // If the fish has enough energy to live on
      fishEnergyMap.put(emptyCell, fishEnergyMap.get(fish) - 1);
      fishReproductionMap.put(emptyCell, fishReproductionMap.get(fish) - 1);
    }
    fishEnergyMap.remove(fish);

    if (fishReproductionMap.containsKey(emptyCell) && fishReproductionMap.get(emptyCell) == 0) {
      makeFish(fish);
      fishReproductionMap.put(emptyCell, fishReproductionTime); // Reset reproduction time for cell that birthed
    }

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
      sharkReproductionMap.put(emptyCell, sharkReproductionMap.get(shark) - 1);
    }
    sharkEnergyMap.remove(shark);

    // Reproduce shark if necessary
    if (sharkReproductionMap.containsKey(emptyCell) && sharkReproductionMap.get(emptyCell) == 0) {
      makeShark(shark);
      sharkReproductionMap.put(emptyCell, sharkReproductionTime); // Reset reproduction time for cell that birthed
    }

  }


  /**
   * Makes shark eat a fish that is neighboring
   *
   * @param shark The shark cell we wish to swap
   * @param fish The fish cell we wish to eat
   */
  private void sharkEatFish(Cell shark, Cell fish) {
    fish.setNextState(shark.getCurrState()); // Change the fish cell to shark cell
    shark.setNextState(WatorState.WATER);

    fishEnergyMap.remove(fish);

    if (sharkEnergyMap.get(shark) > 1) { // If the shark has enough energy to live on
      sharkEnergyMap.put(fish, sharkEnergyMap.get(shark) - 1 + FISH_ENERGY_VALUE);
      sharkReproductionMap.put(fish, sharkReproductionMap.get(shark) - 1);
    }
    sharkEnergyMap.remove(shark);

    if (sharkReproductionMap.containsKey(fish) && sharkReproductionMap.get(fish) == 0) {
      makeShark(shark);
      sharkReproductionMap.put(fish, sharkReproductionTime); // Reset reproduction time for cell that birthed
    }
  }

  /**
   * Makes a new shark cell. Used for reproduction purposes
   * @param cell The cell we wish to turn into a new shark cell
   */
  private void makeShark(Cell cell) {
    cell.setNextState(WatorState.SHARK);
    sharkEnergyMap.put(cell, maxSharkEnergy);
    sharkReproductionMap.put(cell, sharkReproductionTime);
  }

  /**
   * Makes a new fish cell. Used for reproduction purposes.
   * @param cell The cell we wish to turn into a new fish cell
   */
  private void makeFish(Cell cell) {
    cell.setNextState(WatorState.FISH);
    fishEnergyMap.put(cell, maxFishEnergy);
    fishReproductionMap.put(cell, fishReproductionTime);
  }

  /**
   * Generates the initial states for all the tracking maps.
   * @param grid The grid to grab the states from.
   */
  private void generateEnergyAndReproductionMaps(Grid grid) {
    fishCells = new ArrayList<>();
    fishEnergyMap = new HashMap<>();
    fishReproductionMap = new HashMap<>();
    sharkCells = new ArrayList<>();
    sharkEnergyMap = new HashMap<>();
    sharkReproductionMap = new HashMap<>();
    for (int i = 0; i < grid.getRows(); i++) {
      for (int j = 0; j < grid.getColumns(); j++) {
        Cell cellToAdd = grid.getCell(i, j);
        if (cellToAdd.getCurrState() == WatorState.FISH) {
          fishCells.add(cellToAdd);
          fishEnergyMap.put(cellToAdd, maxFishEnergy);
          fishReproductionMap.put(cellToAdd, fishReproductionTime);
        } else if (cellToAdd.getCurrState() == WatorState.SHARK) {
          sharkCells.add(cellToAdd);
          sharkEnergyMap.put(cellToAdd, maxSharkEnergy);
          sharkReproductionMap.put(cellToAdd, sharkReproductionTime);
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

  /**
   * Visually "kills" (sets to default state) any cell that no longer has any energy.
   */
  private void killDeadCells() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        Cell currCell = myGrid.getCell(i, j);
        if (currCell.getCurrState() == WatorState.FISH && !fishEnergyMap.containsKey(currCell)) {
          currCell.setCurrState(WatorState.WATER);
          fishReproductionMap.remove(currCell);
        }
        if (currCell.getCurrState() == WatorState.SHARK && !sharkEnergyMap.containsKey(currCell)) {
          currCell.setCurrState(WatorState.WATER);
          sharkReproductionMap.remove(currCell);
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
    generateEnergyAndReproductionMaps(myGrid);
    return myGrid;
  }

}
