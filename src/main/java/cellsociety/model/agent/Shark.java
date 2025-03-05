package cellsociety.model.agent;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.state.CellState;
import java.util.Collections;
import java.util.List;

public class Shark implements WatorAgent {

  private final int reproductionTimeConstant;

  private int energy;
  private int timeToReproduce;
  private final int sharkEnergyGain;
  private boolean moved;

  public Shark(int sharkEnergyGain, int timeToReproduce) {
    this.energy = 5; // Sharks always start with 5 energy points
    this.timeToReproduce = timeToReproduce;
    this.sharkEnergyGain = sharkEnergyGain;
    this.moved = false;
    reproductionTimeConstant = timeToReproduce;
  }

  /**
   * @param neighbors
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
   * @return
   */
  @Override
  public boolean getMoved() {
    return moved;
  }

  public void setMoved(boolean moved) {
    this.moved = moved;
  }

  /**
   * @return
   */
  @Override
  public int getEnergy() {
    return energy;
  }

  /**
   * @return
   */
  @Override
  public int getReproductionTime() {
    return timeToReproduce;
  }

  /**
   * @return
   */
  @Override
  public CellState getState() {
    return WatorState.SHARK;
  }

}
