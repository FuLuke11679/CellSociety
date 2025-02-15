package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.grid.Grid;
import cellsociety.model.state.CellState;
import java.util.List;

public abstract class Ruleset {

  public Ruleset() {
  }

  public abstract void updateState(Cell cell, List<Cell> neighbors);

  public abstract Grid createGrid(int rows, int columns, String[] initialStates);

  protected CellState getState(Cell cell, Cell neighbor) {
    return neighbor.getCurrState();
  }

}
