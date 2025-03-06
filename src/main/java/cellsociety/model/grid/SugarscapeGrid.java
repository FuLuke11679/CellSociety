package cellsociety.model.grid;

import cellsociety.model.ruleset.SugarscapeRuleset;
import cellsociety.model.cell.Cell;
import java.util.List;

/**
 * Grid for the Sugarscape simulation.
 */
public class SugarscapeGrid extends Grid {

  public SugarscapeGrid(int rows, int columns, SugarscapeRuleset ruleset, String[] initialStates) {
    super(rows, columns, ruleset, initialStates);
  }

  @Override
  public void update() {
    getRuleset().updateGridState();
    moveNextStateToCurrent();
  }
}
