package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class ConwayCell extends Cell {

  public enum ConwayState implements CellState {
    ALIVE,
    DEAD
  }

  public ConwayCell(int id, CellState currState, CellState nextState) {
    super(id, currState, nextState);
  }

}
