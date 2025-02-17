package cellsociety.model.grid;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SugarscapeCell;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;

public class SugarscapeGrid extends Grid {

  public SugarscapeGrid(int rows, int columns, Ruleset ruleset, String[] initialStates) {
    super(rows, columns, ruleset, initialStates);
  }

  @Override
  protected CellState getInitialState(String stateSymbol) {
    return SugarscapeCell.SugarscapeState.valueOf(stateSymbol);
  }

  @Override
  public Cell createCell(int id, CellState currState, CellState nextState, String cellType) {
    int sugar = (int) (Math.random() * 25) + 5; // Random initial sugar
    int metabolism = (int) (Math.random() * 4) + 1; // Random metabolism
    int vision = (int) (Math.random() * 6) + 1; // Random vision
    return new SugarscapeCell(id, currState, nextState, sugar, metabolism, vision);
  }
}