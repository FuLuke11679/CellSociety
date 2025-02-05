package cellsociety.cell;

import cellsociety.cell.GameOfLifeCell.State;
import java.util.HashMap;
import java.util.Map;
import cellsociety.state.CellState;
import javafx.scene.paint.Color;

public abstract class Cell {

  private final int id;
  private Color color;
  CellState prevState;
  CellState currState;

  private static final Map<Color, String> colorNames = new HashMap<>();

  static {
    colorNames.put(Color.RED, "Red");
    colorNames.put(Color.GREEN, "Green");
    colorNames.put(Color.BLUE, "Blue");
    colorNames.put(Color.BLACK, "Black");
    colorNames.put(Color.WHITE, "White");
    colorNames.put(Color.YELLOW, "Yellow");
    colorNames.put(Color.PURPLE, "Purple");
    colorNames.put(Color.ORANGE, "Orange");
    colorNames.put(Color.GRAY, "Gray");
    colorNames.put(Color.PINK, "Pink");
  }

  public Cell(int id) {
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
  public static String getColorName(Color c) {return colorNames.get(c); }

  public abstract State getPrevState();

  public abstract CellState getCurrState();

  public abstract void setPrevState(CellState state);

  public abstract void setCurrState(CellState state);



}
}
