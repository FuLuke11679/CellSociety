package cellsociety.model.grid;

import cellsociety.model.cell.SugarscapePatch;
import cellsociety.model.agent.SugarscapeAgent;
import cellsociety.model.cell.Cell;
import cellsociety.model.state.CellState;
import cellsociety.model.ruleset.SugarscapeRuleset;
import java.util.ArrayList;
import java.util.List;

public class SugarscapeGrid extends Grid {

  public SugarscapeGrid(int rows, int columns, SugarscapeRuleset ruleset, String[] initialStates) {
    super(rows, columns, ruleset, initialStates);
  }

  public boolean hasAgentAt(int row, int col) {
    Cell cell = getCell(row, col);
    return cell instanceof SugarscapePatch && ((SugarscapePatch) cell).hasAgent();
  }

  @Override
  public Cell createCell(int id, CellState currState, CellState nextState, String cellType) {
    int[] values = ((SugarscapeRuleset)getRuleset()).getInitialValues();
    int sugarValue = values[id];
    try {
      SugarscapePatch patch = new SugarscapePatch(id, currState, nextState, sugarValue, 25);
      if (currState.equals(cellsociety.model.state.SugarscapeState.AGENT)) {
        SugarscapeAgent agent = new SugarscapeAgent(10);
        patch.setAgent(agent);
      }
      return patch;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  @Override
  public List<Cell> getNeighbors(int row, int col) {
    Grid grid = getGrid();
    List<Cell> neighbors = new ArrayList<>();

    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    for (int i = 0; i < 4; i++) {
      int numRows = getRows();
      int numCols = getColumns();
      int newRow = (row + dx[i] + numRows) % numRows;  // Wrap row
      int newCol = (col + dy[i] + numCols) % numCols;  // Wrap column

      neighbors.add(grid.getCell(newRow, newCol));
    }
    return neighbors;
  }

  @Override
  public void update() {
    getRuleset().updateGridState();
    moveNextStateToCurrent();
  }
}
