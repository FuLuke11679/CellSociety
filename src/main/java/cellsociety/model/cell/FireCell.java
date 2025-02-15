package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class FireCell extends Cell {

  public enum FireState implements CellState {
    EMPTY,
    TREE,
    BURNING
  }

  public FireCell(int id, CellState currState, CellState nextState) {
    super(id, currState, nextState);
  }

}
