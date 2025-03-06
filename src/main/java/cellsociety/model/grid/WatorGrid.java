package cellsociety.model.grid;

import cellsociety.model.ruleset.Ruleset;

public class WatorGrid extends Grid {

  public WatorGrid(int rows, int columns, Ruleset ruleset, String[] cells) {
    super(rows, columns, ruleset, cells);
  }

  @Override
  public void update() {
    getRuleset().updateGridState();
    // Second pass: Apply new states and update cells to utilize the next state
    moveNextStateToCurrent();
  }

}
