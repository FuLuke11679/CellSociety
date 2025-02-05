package cellsociety.model.cell;

import java.util.HashMap;
import java.util.Map;
import cellsociety.model.state.CellState;
import javafx.scene.paint.Color;

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
