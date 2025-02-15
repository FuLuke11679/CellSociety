package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public abstract class Cell {

  private final int id;
  private CellState currState;
  private CellState nextState;

  public Cell(int id, CellState currState, CellState nextState) {
    this.id = id;
    this.currState = currState;
    this.nextState = nextState;
  }

  public int getId() {
    return id;
  }

  /**
   * @return The previous state of the cell
   */
  public CellState getCurrState() {
    return currState;
  }

  /**
   * @return The current state of the cell
   */
  public CellState getNextState() {
    return nextState;
  }

  /**
   * @param state The state you wish to set the previous state of the cell to
   */
  public void setCurrState(CellState state) {
    currState = state;
  }

  /**
   * @param state The state you wish to set the current state of the cell to
   */
  public void setNextState(CellState state) {
    nextState = state;
  }

}
