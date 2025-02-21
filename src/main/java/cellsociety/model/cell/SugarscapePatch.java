package cellsociety.model.cell;

import cellsociety.model.state.CellState;

/**
 * Represents a sugar patch in the Sugarscape.
 * Holds the current sugar level, maximum sugar capacity,
 * and its grid position.
 */
public class SugarscapePatch extends Cell {
  private int sugarAmount;
  private int maxSugar;
  private SugarscapeAgent agent;

  /**
   * Constructs a SugarscapePatch with the specified parameters.
   *
   * @param id         Unique identifier for the cell.
   * @param currState  The cell's state (e.g., SugarscapeState.PATCH).
   */
  public SugarscapePatch(int id, CellState currState, CellState nextState, int initialSugar) {
    super(id, currState, nextState);
    //Change from being hard coded
    this.maxSugar = 25;
    this.sugarAmount = initialSugar;// Start fully grown.
    this.agent = null;
  }

  /**
   * Returns the current amount of sugar in this patch.
   */
  public int getSugarAmount() {
    return sugarAmount;
  }

  /**
   * Grows the sugar by the specified grow-back rate,
   * without exceeding the maximum capacity.
   *
   * @param growBackRate The number of sugar units to add.
   */
  public void growSugar(int growBackRate) {
    sugarAmount = Math.min(sugarAmount + growBackRate, maxSugar);
  }

  /**
   * Harvests all the sugar from this patch (sets it to 0).
   */
  public void harvestSugar() {
    sugarAmount = 0;
  }

  /**
   * Returns the maximum sugar capacity.
   */
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

}
