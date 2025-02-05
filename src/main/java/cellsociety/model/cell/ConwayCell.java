package cellsociety.model.cell;

import cellsociety.model.state.CellState;
import javafx.scene.paint.Color;

public class ConwayCell extends Cell {

  public enum ConwayState implements CellState {
    ALIVE,
    DEAD
  }

  public ConwayCell(int id, ConwayState prevState, ConwayState currState) {
    super(id, prevState, currState);
    if (currState == ConwayState.DEAD) {
      setColor(Color.WHITE);
    } else {
      setColor(Color.BLACK);
    }
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

}
