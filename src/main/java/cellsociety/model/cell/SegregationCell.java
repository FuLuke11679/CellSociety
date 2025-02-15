package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class SegregationCell extends Cell {

  public enum SegregationState implements CellState {
    RED,
    BLUE,
    EMPTY
  }

  public SegregationCell(int id, CellState currState, CellState nextState) {
    super(id, currState, nextState);
  }

}
