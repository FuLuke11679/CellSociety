package cellsociety.model.cell;

import cellsociety.model.agent.SugarscapeAgent;
import cellsociety.model.state.CellState;

/**
 * Represents a sugar patch in the Sugarscape simulation.
 * Each patch has a sugar level that regenerates over time and may contain an agent.
 *
 * @Author Luke
 */
public class SugarscapePatch extends Cell {
  private int sugarAmount;
  private final int maxSugar;
  private SugarscapeAgent agent;

  /**
   * Constructs a SugarscapePatch with a specified initial and maximum sugar level.
   *
   * @param id           Unique identifier for the patch.
   * @param currState    The current state of the patch.
   * @param nextState    The next state of the patch after an update.
   * @param initialSugar The starting sugar level for the patch.
   * @param maxSugar     The maximum sugar level the patch can reach.
   */
  public SugarscapePatch(int id, CellState currState, CellState nextState, int initialSugar, int maxSugar) {
    super(id, currState, nextState);
    this.maxSugar = maxSugar;
    this.sugarAmount = initialSugar;
    this.agent = null;
  }

  /**
   * Gets the current amount of sugar available in the patch.
   *
   * @return The current sugar amount.
   */
  public int getSugarAmount() { return sugarAmount; }

  /**
   * Gets the maximum sugar level this patch can hold.
   *
   * @return The maximum sugar level.
   */
  public int getMaxSugar() { return maxSugar; }

  /**
   * Checks if an agent is currently occupying this patch.
   *
   * @return True if an agent is present, false otherwise.
   */
  public boolean hasAgent() { return agent != null; }

  /**
   * Retrieves the agent currently on this patch.
   *
   * @return The SugarscapeAgent on this patch, or null if unoccupied.
   */
  public SugarscapeAgent getAgent() { return agent; }

  /**
   * Assigns an agent to this patch.
   *
   * @param agent The agent to be placed on this patch.
   */
  public void setAgent(SugarscapeAgent agent) { this.agent = agent; }

  /**
   * Removes any agent currently occupying this patch.
   */
  public void removeAgent() { this.agent = null; }

  /**
   * Increases the sugar amount of this patch by a specified growth rate, capping at maxSugar.
   *
   * @param growBackRate The rate at which sugar regenerates.
   */
  public void growSugar(int growBackRate) {
    sugarAmount = Math.min(sugarAmount + growBackRate, maxSugar);
  }

  /**
   * Harvests all available sugar from the patch, resetting its level to zero.
   */
  public void harvestSugar() {
    sugarAmount = 0;
  }

  /**
   * Sets the current sugar amount to a specific value.
   *
   * @param initialValue The new sugar amount for this patch.
   */
  public void setSugarAmount(int initialValue) {
    sugarAmount = initialValue;
  }
}
