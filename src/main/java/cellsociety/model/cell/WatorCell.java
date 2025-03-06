package cellsociety.model.cell;

import cellsociety.model.agent.WatorAgent;
import cellsociety.model.state.CellState;

/**
 * Author: Daniel Rodriguez-Florido
 * <p>
 * Cell class for the WatorWorld Simulation
 */
public class WatorCell extends Cell {

  private WatorAgent agent;

  public WatorCell(int id, CellState currState, CellState nextState) {
    super(id, currState, nextState);
  }

  /**
   * Getter for the agent on the cell
   *
   * @return The active agent on the cell
   */
  public WatorAgent getAgent() {
    return agent;
  }

  /**
   * Setter for the agent on the cell
   *
   * @param agent The agent we wish to instantiate on this cell
   */
  public void setAgent(WatorAgent agent) {
    this.agent = agent;
  }

  public enum WatorState implements CellState {
    FISH,
    SHARK,
    WATER
  }

}
