package cellsociety.model.cell;

import cellsociety.model.agent.Fish;
import cellsociety.model.agent.WatorAgent;
import cellsociety.model.state.CellState;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Cell class for the WatorWorld Simulation
 */
public class WatorCell extends Cell {

  private WatorAgent agent;

  public enum WatorState implements CellState {
    FISH,
    SHARK,
    WATER
  }

  public WatorCell(int id, CellState currState, CellState nextState) {
    super(id, currState, nextState);
    if (currState == WatorState.FISH) {
      agent = new Fish(5);
    } else {
      agent = null;
    }
  }

  public WatorAgent getAgent() {
    return agent;
  }

  public void setAgent(WatorAgent agent) {
    this.agent = agent;
  }

}
