package cellsociety.cell;

import javafx.scene.paint.Color;

public class GameOfLifeCell extends Cell {

  public enum State {
    ALIVE,
    DEAD
  }

  private State prevState;
  private State currState;

  public GameOfLifeCell(int id, Color color, State prevState, State currState) {
    super(id, color);
    this.prevState = prevState;
    this.currState = currState;
  }

  public State getPrevState() {
    return prevState;
  }

  public State getCurrState() {
    return currState;
  }

  public void setPrevState(State prevState) {
    this.prevState = prevState;
  }

  public void setCurrState(State currState) {
    this.currState = currState;
  }

}
