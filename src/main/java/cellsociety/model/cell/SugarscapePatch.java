package cellsociety.model.cell;

import cellsociety.model.agent.SugarscapeAgent;
import cellsociety.model.state.CellState;

/**
 * Represents a sugar patch in the Sugarscape. Holds the current sugar level and grows
 * independently.
 */
public class SugarscapePatch extends Cell {

  private int sugarAmount;
  private final int maxSugar;
  private SugarscapeAgent agent;

  public SugarscapePatch(int id, CellState currState, CellState nextState, int initialSugar,
      int maxSugar) {
    super(id, currState, nextState);
    this.maxSugar = maxSugar;
    this.sugarAmount = initialSugar;
    this.agent = null;
  }

  public int getSugarAmount() {
    return sugarAmount;
  }

  public int getMaxSugar() {
    return maxSugar;
  }

  public boolean hasAgent() {
    return agent != null;
  }

  public SugarscapeAgent getAgent() {
    return agent;
  }

  public void setAgent(SugarscapeAgent agent) {
    this.agent = agent;
  }

  public void removeAgent() {
    this.agent = null;
  }

  public void growSugar(int growBackRate) {
    sugarAmount = Math.min(sugarAmount + growBackRate, maxSugar);
  }

  public void harvestSugar() {
    sugarAmount = 0;
  }
}
