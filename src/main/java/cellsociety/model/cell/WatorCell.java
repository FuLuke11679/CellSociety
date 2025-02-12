package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class WatorCell extends Cell {

  public enum WatorState implements CellState {
    FISH,
    SHARK,
    WATER
  }

  public WatorCell(int id, CellState prevState, CellState currState) {
    super(id, prevState, currState);
  }

}
