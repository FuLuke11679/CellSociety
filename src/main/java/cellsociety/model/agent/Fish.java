package cellsociety.model.agent;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.state.CellState;
import java.util.Collections;
import java.util.List;

public class Fish implements WatorAgent {

  private final int REPRODUCTION_TIME;

  private int timeToReproduce;
  private boolean moved;

  public Fish(int timeToReproduce) {
    this.timeToReproduce = timeToReproduce;
    this.moved = false;
    REPRODUCTION_TIME = timeToReproduce;
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
        timeToReproduce = REPRODUCTION_TIME; // Reset reproduction time
      }
      moved = true;
      toMove.setAgent(this);
      toMove.setNextState(WatorState.FISH);
    }
  }

  /**
   * Gets a random empty neighbor to move a cell to
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

  public boolean getMoved() {
    return moved;
  }

  /**
   * @param moved
   */
  @Override
  public void setMoved(boolean moved) {
    this.moved = moved;
  }

  /**
   * @return -1 to indicate nothing. This is a do-nothing function that is only implemented
   *         for sharks.
   */
  @Override
  public int getEnergy() {
    return -1; // Do nothing
  }

  /**
   * @return The amount of steps a fish has left to make to reproduce.
   */
  @Override
  public int getReproductionTime() {
    return timeToReproduce;
  }

  public CellState getState() {
    return WatorState.FISH;
  }

}
