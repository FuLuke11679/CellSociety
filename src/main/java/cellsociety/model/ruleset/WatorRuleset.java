package cellsociety.model.ruleset;

import cellsociety.model.agent.WatorAgentFactory;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.agent.WatorAgent;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.WatorGrid;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Daniel Rodriguez-Florido
 * The back-end ruleset logic to preform the WatorWorld Simulation
 *
 * The agent factory handles and retrieves all the necessary parameters when creating agents
 */

public class WatorRuleset extends Ruleset {

  private WatorGrid myGrid;
  private final WatorAgentFactory agentFactory;

  /**
   * Constructor for the WatorRuleset
   * @param params The map of parameters relevant to the ruleset
   *               (fishBreedTime, fishStarveTime, sharkBreedTime, sharkStarveTime)
   */
  public WatorRuleset(Map<String, String> params) {
    super();
    agentFactory = new WatorAgentFactory(params);
  }

  /**
   * Dummy function in this implementation, used for others (strategy pattern)
   */
  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {
  }

  /**
   * Updates sharks, then fish, and maintains any unaffected cell.
   */
  @Override
  public void updateGridState() {

    List<WatorCell> agentCells = getAgentCells();

    // First move the Shark agents
    for (WatorCell c : agentCells) {
      if (c.getCurrState() == WatorState.SHARK) {
        moveCell(c);
        reproduceCell(c);
      }
    }

    agentCells = getAgentCells(); // Update in case any fish were eaten

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

  /**
   * Moves the cell from one spot to the next
   * @param cell The cell we wish to move
   */
  private void moveCell(WatorCell cell) {
    int cellRow = cell.getId() / myGrid.getRows();
    int cellCol = cell.getId() % myGrid.getRows();
    List<Cell> neighbors = myGrid.getNeighbors(cellRow, cellCol);
    if (cell.getAgent() != null) {
      WatorAgent agent = cell.getAgent();
      agent.move(neighbors);
    }
  }

  /**
   * Function to create a new agent once reproduction time has come
   * @param cell The cell we wish to spawn an agent on
   */
  private void reproduceCell(WatorCell cell) {
    if (cell.getAgent() != null && cell.getAgent().getReproductionTime() == 0) {
      cell.setAgent(agentFactory.createWatorAgent(cell.getCurrState()));
      cell.setNextState(cell.getAgent().getState());
    }
  }

  /**
   * Gets the active agents on the grid
   * @return List of WatorCells that have an active agent on them
   */
  private List<WatorCell> getAgentCells() {
    List<WatorCell> agentCells = new ArrayList<>();
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        WatorCell currCell = (WatorCell) myGrid.getCell(i, j);
        if (currCell.getAgent() != null && currCell.getNextState() == null) {
          agentCells.add(currCell);
        }
      }
    }
    return agentCells;
  }

  /**
   * Removes any cell that has died from the grid
   */
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

  /**
   * Checks whether a cell has died either by running out of energy or by leaving a space
   * @param cell The cell we wish to check if dead
   * @return Whether the cell is dead or not
   */
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
    initializeAgents();
    return myGrid;
  }

  /**
   * Function used in the creation of the grid to ensure agents are placed where they need to be
   */
  private void initializeAgents() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        WatorCell currCell = (WatorCell) myGrid.getCell(i, j);
        if (currCell.getCurrState() != WatorState.WATER) {
          currCell.setAgent(agentFactory.createWatorAgent(currCell.getCurrState()));
        }
      }
    }
  }

  @Override
  public CellState getDefaultCellState() {
    return WatorState.WATER;
  }

}
