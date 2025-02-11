package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public class SegregationCell extends Cell {

  public enum SegregationState implements CellState {
    RED,
    BLUE,
    EMPTY
  }

  public SegregationCell(int id, CellState prevState, CellState currState) {
    super(id, prevState, currState);
  }

  /**
   * @return The previous state of the cell
   */
  public CellState getPrevState() {
    return prevState;
  }

  /**
   * @return The current state of the cell
   */
  public CellState getCurrState() {
    return currState;
  }

  /**
   * @param state The new state you wish to make the previous state
   */
  public void setPrevState(CellState state) {
    this.prevState = state;
  }

  /**
   * @param state The new state that you wish to apply to the Cell
   */
  public void setCurrState(CellState state) {
    this.currState = state;
  }

}
