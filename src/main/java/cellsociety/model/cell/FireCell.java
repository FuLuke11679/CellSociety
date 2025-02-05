package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class FireCell extends Cell {

  public enum FireState implements CellState {
    EMPTY,
    TREE,
    BURNING;
  }

  public FireCell(int id, FireState prevState, FireState currState) {
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
    this.prevState = state;
  }

  @Override
  public void setCurrState(CellState state) {
    this.currState = state;
  }
}
