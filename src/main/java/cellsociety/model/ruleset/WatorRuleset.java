package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.agent.WatorAgent;
import cellsociety.model.agent.Fish;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.WatorGrid;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Daniel Rodriguez-Florido
 * The back-end ruleset logic to preform the WatorWorld Simulation
 */

public class WatorRuleset extends Ruleset {

  private static final String FISH_BREED_PARAM_NAME = "fishBreedTime";
  private static final String MAX_FISH_ENERGY_PARAM_NAME = "fishStarveTime";
  private static final String SHARK_BREED_PARAM_NAME = "sharkBreedTime";
  private static final String MAX_SHARK_ENERGY_PARAM_NAME = "sharkStarveTime";

  private static final int FISH_ENERGY_VALUE = 5;

  private WatorGrid myGrid;
  private List<WatorCell> agentCells;
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
   * @param params The map of parameters relevant to the ruleset
   *               (fishBreedTime, fishStarveTime, sharkBreedTime, sharkStarveTime)
   */
  public WatorRuleset(Map<String, String> params) {
    super();
    fishReproductionTime = Integer.parseInt(params.get(FISH_BREED_PARAM_NAME));
    maxFishEnergy = Integer.parseInt(params.get(MAX_FISH_ENERGY_PARAM_NAME));
    sharkReproductionTime = Integer.parseInt(params.get(SHARK_BREED_PARAM_NAME));
    maxSharkEnergy = Integer.parseInt(params.get(MAX_SHARK_ENERGY_PARAM_NAME));
  }

  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {
  }

  /**
   * Updates sharks, then fish, and maintains any unaffected cell.
   */
  @Override
  public void updateGridState() {

    agentCells = getAgentCells();

    // First move the Shark agents
    for (WatorCell c : agentCells) {
      if (c.getCurrState() == WatorState.SHARK) {
        moveCell(c);
        reproduceCell(c);
      }
    }

    // Then move the Fish agents
    for (WatorCell c : agentCells) {
      if (c.getCurrState() == WatorState.FISH) {
        moveCell(c);
        reproduceCell(c);
      }
    }

    killDeadCells();
    maintainRestOfGrid(myGrid);

  }

  private void moveCell(WatorCell cell) {
    int cellRow = cell.getId() / myGrid.getRows();
    int cellCol = cell.getId() % myGrid.getRows();
    List<Cell> neighbors = myGrid.getNeighbors(cellRow, cellCol);
    if (cell.getAgent() != null) {
      WatorAgent agent = cell.getAgent();
      agent.move(neighbors);
    }
  }

  private void reproduceCell(WatorCell cell) {
    if (cell.getAgent() != null && cell.getAgent().getMoved()) {
      WatorAgent agent = cell.getAgent();
      if (agent.getReproductionTime() == 0) {
        cell.setNextState(agent.getState());
        cell.setAgent(new Fish(fishReproductionTime));
      }
    }
  }

//  /**
//   * Moves a fish from its original spot to a new random empty space
//   * @param cell The fish cell we wish to move
//   */
//  private void moveFish(Cell cell) {
//    int cellRow = cell.getId() / myGrid.getRows();
//    int cellCol = cell.getId() % myGrid.getRows();
//    List<Cell> neighbors = myGrid.getNeighbors(cellRow, cellCol);
//    Cell toMove = getRandomEmptySpot(neighbors);
//    if (toMove != null) {
//      swapActiveAndEmptyCell(cell, toMove, fishEnergyMap, fishReproductionMap, fishReproductionTime);
//    }
//  }
//
//  /**
//   * Moves a shark from its original spot to a random fish space or empty space if no fish around
//   * @param cell The shark cell we wish to move
//   */
//  private void moveShark(Cell cell) {
//    int cellRow = cell.getId() / myGrid.getRows();
//    int cellCol = cell.getId() % myGrid.getRows();
//    List<Cell> neighbors = myGrid.getNeighbors(cellRow, cellCol);
//    Cell toMove = getRandomFishOrEmptySpot(neighbors);
//
//    if (toMove != null) {
//      if (toMove.getCurrState() == WatorState.FISH) {
//        sharkEatFish(cell, toMove);
//      } else {
//        swapActiveAndEmptyCell(cell, toMove, sharkEnergyMap, sharkReproductionMap, sharkReproductionTime);
//      }
//    }
//  }

//  /**
//   * Gets a random empty neighbor to move a cell to
//   * @param neighbors The neighbors of the cell to check
//   * @return An empty Cell that is the candidate for swapping to
//   */
//  private Cell getRandomEmptySpot(List<Cell> neighbors) {
//    Collections.shuffle(neighbors);
//    for (Cell neighbor : neighbors) {
//      if (neighbor.getCurrState() == WatorState.WATER && neighbor.getNextState() == null) {
//        return neighbor;
//      }
//    }
//    return null;
//  }

//  /**
//   * For sharks to find a random spot to move to. Attempts to find fish and if does not find fish
//   * then returns a random empty spot to move to.
//   * @param neighbors The neighbors of the shark cell
//   * @return The cell the shark can move to
//   */
//  private Cell getRandomFishOrEmptySpot(List<Cell> neighbors) {
//    Collections.shuffle(neighbors);
//    for (Cell neighbor : neighbors) {
//      if (neighbor.getCurrState() == WatorState.FISH && neighbor.getNextState() == null) {
//        return neighbor;
//      }
//    }
//    return getRandomEmptySpot(neighbors);
//  }

//  /**
//   * Makes shark eat a fish that is neighboring
//   *
//   * @param shark The shark cell we wish to swap
//   * @param fish The fish cell we wish to eat
//   */
//  private void sharkEatFish(Cell shark, Cell fish) {
//    fish.setNextState(shark.getCurrState()); // Change the fish cell to shark cell
//    shark.setNextState(WatorState.WATER);
//
//    fishEnergyMap.remove(fish);
//
//    if (sharkEnergyMap.get(shark) > 1) { // If the shark has enough energy to live on
//      sharkEnergyMap.put(fish, sharkEnergyMap.get(shark) - 1 + FISH_ENERGY_VALUE);
//      sharkReproductionMap.put(fish, sharkReproductionMap.get(shark) - 1);
//    }
//    sharkEnergyMap.remove(shark);
//
//    if (sharkReproductionMap.containsKey(fish) && sharkReproductionMap.get(fish) == 0) {
//      makeActiveCell(shark);
//      sharkReproductionMap.put(fish, sharkReproductionTime); // Reset reproduction time for cell that birthed
//    }
//  }

  /**
   * Visually "kills" (sets to default state) any cell that no longer has any energy.
   */
//  private void killDeadCells() {
//    for (int i = 0; i < myGrid.getRows(); i++) {
//      for (int j = 0; j < myGrid.getColumns(); j++) {
//        Cell currCell = myGrid.getCell(i, j);
//        if (currCell.getCurrState() == WatorState.FISH && !fishEnergyMap.containsKey(currCell)) {
//          currCell.setCurrState(WatorState.WATER);
//          fishReproductionMap.remove(currCell);
//        }
//        if (currCell.getCurrState() == WatorState.SHARK && !sharkEnergyMap.containsKey(currCell)) {
//          currCell.setCurrState(WatorState.WATER);
//          sharkReproductionMap.remove(currCell);
//        }
//      }
//    }
//  }

  private List<WatorCell> getAgentCells() {
    List<WatorCell> agentCells = new ArrayList<>();
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        WatorCell currCell = (WatorCell) myGrid.getCell(i, j);
        if (currCell.getAgent() != null) {
          agentCells.add(currCell);
        }
      }
    }
    return agentCells;
  }

  private void killDeadCells() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        WatorCell currCell = (WatorCell) myGrid.getCell(i, j);
        if (isCellDead(currCell)) {
          currCell.setNextState(WatorState.WATER);
          currCell.setAgent(null);
        }
      }
    }
  }

  private boolean isCellDead(WatorCell cell) {
    if (cell.getAgent() == null) {
      return true;
    }
    if (cell.getAgent().getMoved() && cell.getNextState() == null) {
      return true;
    }
    return cell.getAgent().getEnergy() == 0;
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
    return myGrid;
  }

  @Override
  public CellState getDefaultCellState() {
    return WatorState.WATER;
  }

}
