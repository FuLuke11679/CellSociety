package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.FireCell;
import cellsociety.model.cell.SegregationCell;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SegregationGrid extends Grid {

  public SegregationGrid(int rows, int columns, Ruleset ruleset, String[] cells) {
    super(rows, columns, ruleset, cells);
  }

  @Override
  public void initializeGrid() {
    myGrid = new ArrayList<>();
    int count = 0;
    for (int x = 0; x < rows; x++) {
      List<Cell> row = new ArrayList<>();
      for (int y = 0; y < columns; y++) {
        CellState initialState = getInitialState(myCells[count]);
        // Use the appropriate cell type based on the state
        Cell cell = new SegregationCell(count, null, initialState);
        row.add(cell);
        count++;
      }
      myGrid.add(row);
    }
  }

}
