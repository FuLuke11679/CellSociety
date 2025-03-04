package cellsociety.model.agent;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.state.CellState;
import java.util.List;

public class Shark implements WatorAgent {

  private final int REPRODUCTION_TIME;

  private int energy;
  private int timeToReproduce;
  private boolean moved;

  public Shark(int energy, int timeToReproduce) {
    this.energy = energy;
    this.timeToReproduce = timeToReproduce;
    this.moved = false;
    REPRODUCTION_TIME = timeToReproduce;
  }

  /**
   * @param neighbors
   */
  @Override
  public void move(List<Cell> neighbors) {

  }

  /**
   * @return
   */
  @Override
  public boolean getMoved() {
    return moved;
  }

  /**
   * @return
   */
  @Override
  public int getEnergy() {
    return 0;
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
