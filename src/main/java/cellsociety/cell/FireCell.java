package cellsociety.cell;

import cellsociety.state.CellState;
import javafx.scene.paint.Color;

public class FireCell extends Cell {

  public enum FireState implements CellState {
    EMPTY,
    TREE,
    BURNING;

    @Override
    public String getName() {
      return "";
    }
  }

  private static final Color TREE_COLOR = Color.GREEN;
  private static final Color BURNING_COLOR = Color.BROWN;
  private static final Color EMPTY_COLOR = Color.YELLOW;

  public FireCell(int id, FireState prevState, FireState currState) {
    super(id, prevState, currState);
    if (currState == FireState.EMPTY) {
      setColor(EMPTY_COLOR);
    } else if (currState == FireState.TREE) {
      setColor(TREE_COLOR);
    } else if (currState == FireState.BURNING) {
      setColor(BURNING_COLOR);
    }
  }

  @Override
  public CellState getPrevState() {
    return prevState;
  }

  @Override
  public CellState getCurrState() {
    return currState;
  }

  @Override
  public void setPrevState(CellState state) {
    this.prevState = state;
  }

  @Override
  public void setCurrState(CellState state) {
    this.currState = state;
  }
}
