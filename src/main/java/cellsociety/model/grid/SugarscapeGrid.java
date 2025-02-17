package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SugarscapeCell;
import cellsociety.model.cell.SugarscapePatchCell;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.List;

public class SugarscapeGrid extends Grid {

  private List<List<SugarscapePatchCell>> patches;

  public SugarscapeGrid(int rows, int columns, Ruleset ruleset, String[] initialStates) {
    super(rows, columns, ruleset, initialStates);
    this.patches = new ArrayList<>();
    initializePatches();
  }

  private void initializePatches() {
    for (int x = 0; x < getRows(); x++) {
      List<SugarscapePatchCell> patchRow = new ArrayList<>();
      for (int y = 0; y < getColumns(); y++) {
        SugarscapePatchCell patch = new SugarscapePatchCell(
            x * getColumns() + y,
            SugarscapePatchCell.SugarscapePatchState.LOW_SUGAR, // Example initial state
            null,
            4, // Initial sugar
            4, // Max capacity
            1, // Grow back rate
            1  // Grow back interval
        );
        patchRow.add(patch);
      }
      patches.add(patchRow);
    }
  }

  public SugarscapePatchCell getPatch(int row, int col) {
    return patches.get(row).get(col);
  }

  public void updatePatches() {
    for (int x = 0; x < getRows(); x++) {
      for (int y = 0; y < getColumns(); y++) {
        patches.get(x).get(y).growSugar();
      }
    }
  }
}