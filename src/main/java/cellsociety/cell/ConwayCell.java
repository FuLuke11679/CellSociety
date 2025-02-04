package cellsociety.cell;

import cellsociety.state.CellState;
import javafx.scene.paint.Color;

public class ConwayCell extends Cell {

  public enum GameOfLifeState implements CellState {
    ALIVE,
    DEAD;

    @Override
    public String getName() {
      return "";
    }
  }

  public ConwayCell(int id, GameOfLifeState prevState, GameOfLifeState currState) {
    super(id, prevState, currState);
    if (currState == GameOfLifeState.DEAD) {
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
