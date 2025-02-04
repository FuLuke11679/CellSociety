package cellsociety.cell;

import cellsociety.cell.GameOfLifeCell.State;
import javafx.scene.paint.Color;

public abstract class Cell {

  private final int id;
  private Color color;

  public Cell(int id) {
    this.id = id;
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

  public abstract State getPrevState();

  public abstract State getCurrState();

  public abstract void setPrevState(State state);

  public abstract void setCurrState(State state);

}
