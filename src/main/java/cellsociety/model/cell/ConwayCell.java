package cellsociety.model.cell;

import cellsociety.model.state.CellState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;

public class ConwayCell extends Cell {

  public enum ConwayState implements CellState {
    ALIVE,
    DEAD
  }

  public ConwayCell(int id, CellState prevState, CellState currState) {
    super(id, prevState, currState);
  }

  public CellState getPrevState() {
    return prevState;
  }

  public CellState getCurrState() {
    return currState;
  }

  public void setPrevState(CellState prevState) {
    this.prevState = prevState;
  }

  public void setCurrState(CellState currState) {
    this.currState = currState;
  }

  public static List<CellState> getStates(){
    return Arrays.asList(ConwayState.values());
  }

}
