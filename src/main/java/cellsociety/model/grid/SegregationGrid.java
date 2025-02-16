package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SegregationCell;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.List;

public class SegregationGrid extends Grid {

  public SegregationGrid(int rows, int columns, Ruleset ruleset, String[] cells) {
    super(rows, columns, ruleset, cells);
  }

}
