package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.List;

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
