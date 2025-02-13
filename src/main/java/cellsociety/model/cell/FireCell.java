package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class FireCell extends Cell {

  public enum FireState implements CellState {
    EMPTY,
    TREE,
    BURNING
  }

  public FireCell(int id, FireState prevState, CellState currState) {
    super(id, prevState, currState);
  }

}
