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

  @Override
  public CellState getPrevState() {
    return prevState;
  }

  @Override
  public CellState getCurrState() {
    return currState;
  }

  @Override
  public void setPrevState(CellState state) {
    prevState = state;
  }

  @Override
  public void setCurrState(CellState state) {
    currState = state;
  }
}
