package cellsociety.ruleset;

import cellsociety.cell.Cell;
import java.util.List;

public abstract class Ruleset {

  public Ruleset() {}

  public abstract void updateState(Cell cell, List<Cell> neighbors);

}
