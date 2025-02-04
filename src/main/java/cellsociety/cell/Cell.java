package cellsociety.cell;

import javafx.scene.paint.Color;

public abstract class Cell {

  private final int id;
  private Color color;

  public Cell(int id, Color color) {
    this.id = id;
    this.color = color;
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

}
