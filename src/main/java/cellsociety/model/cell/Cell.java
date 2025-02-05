package cellsociety.model.cell;

import cellsociety.model.state.CellState;
import javafx.scene.paint.Color;

public abstract class Cell {

  private final int id;
  private Color color;
  CellState prevState;
  CellState currState;

  public Cell(int id, CellState prevState, CellState currState) {
    this.id = id;
    this.prevState = prevState;
    this.currState = currState;
  }

  public int getId() {
    return id;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public abstract CellState getPrevState();

  public abstract CellState getCurrState();

  public abstract void setPrevState(CellState state);

  public abstract void setCurrState(CellState state);

}
