package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class ConwayCell extends Cell {

  public enum ConwayState implements CellState {
    ALIVE,
    DEAD
  }

  public ConwayCell(int id, CellState prevState, CellState currState) {
    super(id, prevState, currState);
  }

}
