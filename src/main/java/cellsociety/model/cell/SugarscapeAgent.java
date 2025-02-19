package cellsociety.model.cell;

import cellsociety.model.state.CellState;

/**
 * Represents an agent that moves on top of a sugar patch.
 * This class extends SugarscapePatch so that the underlying sugar
 * information is preserved even when an agent is present.
 */
public class SugarscapeAgent{
  private int agentSugar;
  private int vision;
  private int metabolism;

  /**
   * Constructs a SugarscapeAgent with the given parameters.
   *
   * @param agentSugar  The agent's initial sugar level.
   * @param vision      How far the agent can see (in grid cells).
   * @param metabolism  The amount of sugar the agent uses per tick.
   */
  public SugarscapeAgent(int agentSugar, int vision, int metabolism) {
    // Initialize the patch portion.
    this.agentSugar = agentSugar;
    this.vision = vision;
    this.metabolism = metabolism;
  }

  /**
   * Returns the current sugar stored by the agent.
   */
  public int getAgentSugar() {
    return agentSugar;
  }

  /**
   * Returns the agent's vision range.
   */
  public int getVision() {
    return vision;
  }

  /**
   * Returns the agent's metabolism rate.
   */
  public int getMetabolism() {
    return metabolism;
  }

  /**
   * Increases the agent's sugar by the specified amount.
   *
   * @param amount The sugar collected from the patch.
   */
  public void collectSugar(int amount) {
    agentSugar += amount;
  }

  /**
   * Consumes sugar equal to the metabolism rate.
   * If the agent's sugar falls to zero or below, it is considered dead.
   */
  public void consumeSugar() {
    agentSugar -= metabolism;
    if (agentSugar <= 0) {
      // Here you could mark the agent as dead.
      // For instance, you might convert this cell back to a plain patch.
      agentSugar = 0;
    }
  }
}
