package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class SugarscapeCell extends Cell {

  public enum SugarscapeState implements CellState {
    EMPTY,
    LOW_SUGAR,
    MEDIUM_SUGAR,
    HIGH_SUGAR,
    OCCUPIED
  }

  private int sugar;
  private int sugarMetabolism;
  private int vision;
  private SugarscapePatchCell currentPatch; // Reference to the patch the agent is currently on

  public SugarscapeCell(int id, CellState currState, CellState nextState, int sugar, int sugarMetabolism, int vision) {
    super(id, currState, nextState);
    this.sugar = sugar;
    this.sugarMetabolism = sugarMetabolism;
    this.vision = vision;
    this.currentPatch = null; // Initially, the agent is not on any patch
  }

  public int getSugar() {
    return sugar;
  }

  public void setSugar(int sugar) {
    this.sugar = sugar;
  }

  public int getSugarMetabolism() {
    return sugarMetabolism;
  }

  public int getVision() {
    return vision;
  }

  public SugarscapePatchCell getCurrentPatch() {
    return currentPatch;
  }

  public void setCurrentPatch(SugarscapePatchCell patch) {
    this.currentPatch = patch;
  }

  public void consumeSugar() {
    if (currentPatch != null && currentPatch.getSugar() > 0) {
      int sugarConsumed = Math.min(currentPatch.getSugar(), 1); // Example: consume 1 unit of sugar
      currentPatch.setSugar(currentPatch.getSugar() - sugarConsumed);
      this.sugar += sugarConsumed;
    }
  }

  public void moveToPatch(SugarscapePatchCell destination) {
    if (currentPatch != null) {
      currentPatch.setSugar(currentPatch.getSugar() + 0); // Optionally, leave some sugar behind
    }
    this.currentPatch = destination;
    this.consumeSugar(); // Consume sugar from the new patch
  }

  public boolean isAlive() {
    return this.sugar > 0;
  }
}