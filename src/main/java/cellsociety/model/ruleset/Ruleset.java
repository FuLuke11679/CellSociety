package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.grid.Grid;
import cellsociety.model.state.CellState;
import java.util.List;

public abstract class Ruleset {

  public Ruleset() {}

  public abstract void updateState(Cell cell, List<Cell> neighbors);

  public abstract Grid createGrid(int rows, int columns, String[] initialStates);

  public CellState getState(Cell cell, Cell neighbor) {
    if (neighbor.getId() < cell.getId()) {
      return neighbor.getPrevState();
    }
    return neighbor.getCurrState();
  }

}
