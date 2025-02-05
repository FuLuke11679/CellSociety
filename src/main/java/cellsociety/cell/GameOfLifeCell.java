
package cellsociety.cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

public class GameOfLifeCell extends Cell {

  public enum State {
    ALIVE,
    DEAD
  }

  private State prevState;
  private State currState;

  public GameOfLifeCell(int id, State prevState, State currState) {
    super(id);
    this.prevState = prevState;
    this.currState = currState;

    setColor(getStateColor(currState));
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
  //returns list of states
  public static ArrayList<State> getStates(){
    return new ArrayList<>(Arrays.asList(State.values()));
  }
  public static Color getStateColor(State state) {
    if (state == State.DEAD) {
      return Color.WHITE;
    } else {
      return Color.BLACK;
    }
  }

}