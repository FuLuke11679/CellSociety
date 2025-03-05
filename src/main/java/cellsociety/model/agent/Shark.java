package cellsociety.model.agent;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.state.CellState;
import java.util.Collections;
import java.util.List;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Shark agent that implements WatorAgent. Used for the WatorWorld simulation.
 */
public class Shark implements WatorAgent {

  private final int reproductionTimeConstant;

  private int energy;
  private int timeToReproduce;
  private final int sharkEnergyGain;
  private boolean moved;

  /**
   * Constructor for the Shark agent
   * @param sharkEnergyGain The amount of energy a shark gains when eating a fish
   * @param timeToReproduce The amount of time it takes for a shark to reproduce
   */
  public Shark(int sharkEnergyGain, int timeToReproduce) {
    this.energy = 5; // Sharks always start with 5 energy points
    this.timeToReproduce = timeToReproduce;
    this.sharkEnergyGain = sharkEnergyGain;
    this.moved = false;
    reproductionTimeConstant = timeToReproduce;
  }

  /**
   * Moves the agent from one cell to the next
   *
   * @param neighbors The candidates for where the agent can move
   */
  @Override
  public void move(List<Cell> neighbors) {
    WatorCell toMove = findNeighborToMove(neighbors);
    if (toMove != null) {
      energy--;
      timeToReproduce--;
      if (timeToReproduce < 0) {
        timeToReproduce = reproductionTimeConstant; // Reset  reproduction time
      }
      if (toMove.getCurrState() == WatorState.FISH) {
        energy += sharkEnergyGain;
      }
      moved = true;
      toMove.setAgent(this);
      toMove.setNextState(WatorState.SHARK);
    }
  }

  /**
   * Finds a suitable neighbor to move to, prioritizing fish
   * @param neighbors The list of candidate cells
   * @return The WatorCell to move to
   */
  private WatorCell findNeighborToMove(List<Cell> neighbors) {
    Collections.shuffle(neighbors);
    WatorCell toMove = null;
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == WatorState.WATER && neighbor.getNextState() == null) {
        toMove = (WatorCell) neighbor;
      }
      if (neighbor.getCurrState() == WatorState.FISH && neighbor.getNextState() == null) {
        return (WatorCell) neighbor; // Return first fish found
      }
    }
    return toMove; // Return water cell if no fish found
  }

  /**
   * Getter for the moved status of the agent
   *
   * @return The value of the instance variable moved (boolean)
   */
  @Override
  public boolean getMoved() {
    return moved;
  }

  /**
   * Setter for the moved flag of the agent
   * @param moved True or false indicating if the agent moved or not
   */
  public void setMoved(boolean moved) {
    this.moved = moved;
  }

  /**
   * Getter for the energy of the agent
   *
   * @return int indicating how much energy the agent has
   */
  @Override
  public int getEnergy() {
    return energy;
  }

  /**
   * Getter for the amount of time an agent needs to reproduce
   *
   * @return int indicating how much time is left
   */
  @Override
  public int getReproductionTime() {
    return timeToReproduce;
  }

  /**
   * Getter for the CellState of the agent
   *
   * @return SHARK state since the shark agent always belongs on a SHARK cell
   */
  @Override
  public CellState getState() {
    return WatorState.SHARK;
  }

}
