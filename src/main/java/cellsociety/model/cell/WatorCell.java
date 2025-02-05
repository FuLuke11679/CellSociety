package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class WatorCell extends Cell {

  public WatorCell(int id, CellState prevState, CellState currState) {
    super(id, prevState, currState);
  }

  @Override
  public CellState getPrevState() {
    return null;
  }

  @Override
  public CellState getCurrState() {
    return null;
  }

  @Override
  public void setPrevState(CellState state) {

  }

  @Override
  public void setCurrState(CellState state) {

  }
}
