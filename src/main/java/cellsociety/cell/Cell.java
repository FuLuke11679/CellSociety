package cellsociety.cell;

import cellsociety.cell.GameOfLifeCell.State;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;

public abstract class Cell {

  private final int id;
  private Color color;

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

  public static String getColorName(Color c) {return colorNames.get(c); }

  public abstract State getPrevState();

  public abstract State getCurrState();

  public abstract void setPrevState(State state);

  public abstract void setCurrState(State state);



}
