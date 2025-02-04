package cellsociety.ruleset;

import cellsociety.cell.GameOfLifeCell;
import java.util.List;

public abstract class Ruleset {

  public Ruleset() {}

  public abstract void updateState(GameOfLifeCell cell, List<GameOfLifeCell> neighbors);

}
