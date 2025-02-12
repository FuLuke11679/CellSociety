package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class PercolationCell extends Cell {

  public enum PercolationState implements CellState {
    BLOCKED,
    OPEN,
    PERCOLATED;
  }

  public PercolationCell(int id, CellState prevState, CellState currState) {
    super(id, prevState, currState);
  }

}
