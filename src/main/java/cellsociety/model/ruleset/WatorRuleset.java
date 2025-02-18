package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.WatorGrid;
import cellsociety.model.state.CellState;
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

    maintainRestOfGrid(myGrid);

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
      swapActiveAndEmptyCell(cell, toMove, fishEnergyMap, fishReproductionMap, fishReproductionTime);
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
        swapActiveAndEmptyCell(cell, toMove, sharkEnergyMap, sharkReproductionMap, sharkReproductionTime);
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
   * Function to swap any type of cell into an empty cell
   * @param activeCell The Cell that isn't empty
   * @param emptyCell The Cell that is empty
   * @param energyMap The energy map that we wish to modify (specific to to the cell type)
   * @param reproductionMap The reproduction map that we wish to modify (specific to cell type)
   * @param reproductionTime The reproduction time of the particular cell type
   */
  private void swapActiveAndEmptyCell(Cell activeCell, Cell emptyCell, Map<Cell, Integer> energyMap,
      Map<Cell, Integer> reproductionMap, int reproductionTime) {
    activeCell.setNextState(emptyCell.getCurrState());
    emptyCell.setNextState(activeCell.getCurrState());

    if (energyMap.get(activeCell) > 1) { // If the fish has enough energy to live on
      energyMap.put(emptyCell, energyMap.get(activeCell) - 1);
      reproductionMap.put(emptyCell, reproductionMap.get(activeCell) - 1);
    }
    energyMap.remove(activeCell);

    if (reproductionMap.containsKey(emptyCell) && reproductionMap.get(emptyCell) == 0) {
      makeActiveCell(activeCell);
      reproductionMap.put(emptyCell, reproductionTime); // Reset reproduction time for cell that birthed
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
      makeActiveCell(shark);
      sharkReproductionMap.put(fish, sharkReproductionTime); // Reset reproduction time for cell that birthed
    }
  }

  /**
   * Makes a new active cell with the CellState of cell
   * @param cell The cell we wish to make a new one of
   */
  private void makeActiveCell(Cell cell) {
    if (cell.getCurrState() == WatorState.FISH) {
      cell.setNextState(WatorState.FISH);
      fishEnergyMap.put(cell, maxFishEnergy);
      fishReproductionMap.put(cell, fishReproductionTime);
    } else if (cell.getCurrState() == WatorState.SHARK) {
      cell.setNextState(WatorState.SHARK);
      sharkEnergyMap.put(cell, maxSharkEnergy);
      sharkReproductionMap.put(cell, sharkReproductionTime);
    }
  }

  /**
   * Generates the initial states for all the tracking maps.
   * @param grid The grid to grab the states from.
   */
  protected void generateEnergyAndReproductionMaps(Grid grid) {
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
