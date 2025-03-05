package cellsociety.model.cell;

import cellsociety.model.agent.Fish;
import cellsociety.model.agent.Shark;
import cellsociety.model.agent.WatorAgent;
import cellsociety.model.ruleset.WatorRuleset;
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
  }

  public WatorAgent getAgent() {
    return agent;
  }

  public void setAgent(WatorAgent agent) {
    this.agent = agent;
  }

}
