package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public abstract class Cell {

  private final int id;
  protected CellState prevState;
  protected CellState currState;

  public Cell( int id, CellState prevState, CellState currState){
      this.id = id;
      this.prevState = prevState;
      this.currState = currState;
    }

    public int getId () {
      return id;
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
   * @param state The state you wish to set the previous state of the cell to
   */
  public void setPrevState(CellState state) {
    prevState = state;
  }

  /**
   * @param state The state you wish to set the current state of the cell to
   */
  public void setCurrState(CellState state) {
    currState = state;
  }

}
