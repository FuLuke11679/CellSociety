package cellsociety.model.cell;

import cellsociety.model.state.CellState;

public abstract class Cell {

  private final int id;
  CellState prevState;
  CellState currState;

  public Cell( int id, CellState prevState, CellState currState){
      this.id = id;
      this.prevState = prevState;
      this.currState = currState;
    }

    public int getId () {
      return id;
    }

    public abstract CellState getPrevState ();

    public abstract CellState getCurrState ();

    public abstract void setPrevState(CellState state);

  public abstract void setCurrState(CellState state);

}
