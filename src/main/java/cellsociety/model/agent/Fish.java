package cellsociety.model.agent;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.state.CellState;
import java.util.Collections;
import java.util.List;

/**
 * Author: Daniel Rodriguez-Florido
 * <p>
 * Fish agent that implements WatorAgent. Agent used in WatorWorld
 */
public class Fish implements WatorAgent {

  private final int reproductionTime;

  private int timeToReproduce;
  private boolean moved;

  /**
   * Constructor for the Fish agent
   *
   * @param timeToReproduce The amount of time it takes for a fish to reproduce
   */
  public Fish(int timeToReproduce) {
    this.timeToReproduce = timeToReproduce;
    this.moved = false;
    reproductionTime = timeToReproduce;
  }

  /**
   * @param neighbors The list of neighbors of the cell
   */
  @Override
  public void move(List<Cell> neighbors) {
    WatorCell toMove = findNeighborToMove(neighbors);
    if (toMove != null) {
      timeToReproduce--;
      if (timeToReproduce < 0) {
        timeToReproduce = reproductionTime; // Reset reproduction time
      }
      moved = true;
      toMove.setAgent(this);
      toMove.setNextState(WatorState.FISH);
    }
  }

  /**
   * Gets a random empty neighbor to move a cell to
   *
   * @param neighbors The neighbors of the cell to check
   * @return An empty Cell that is the candidate for swapping to
   */
  private WatorCell findNeighborToMove(List<Cell> neighbors) {
    Collections.shuffle(neighbors);
    for (Cell neighbor : neighbors) {
      if (neighbor.getCurrState() == WatorState.WATER && neighbor.getNextState() == null) {
        return (WatorCell) neighbor;
      }
    }
    System.out.println();
    return null;
  }

  /**
   * Getter to check if the agent has moved or not
   *
   * @return If the agent has moved or not
   */
  public boolean getMoved() {
    return moved;
  }

  /**
   * Setter for the moved status of an agent
   *
   * @param moved boolean indicating true if an agent has moved, false otherwise
   */
  @Override
  public void setMoved(boolean moved) {
    this.moved = moved;
  }

  /**
   * @return -1 to indicate nothing. This is a do-nothing function that is only implemented for
   * sharks.
   */
  @Override
  public int getEnergy() {
    return -1; // Do nothing
  }

  /**
   * Getter for the timeToReproduce of the agent.
   *
   * @return The amount of steps a fish has left to make to reproduce.
   */
  @Override
  public int getReproductionTime() {
    return timeToReproduce;
  }

  /**
   * Getter for the state of the agent.
   *
   * @return FISH state since a Fish agent corresponds to fish state.
   */
  public CellState getState() {
    return WatorState.FISH;
  }

}
