package cellsociety;

import java.util.List;

public abstract class Parser {
  //what info is needed from Parser
  private String simulationType;
  private String author;
  private String title;
  private String description;
  private int width;
  private int height;
  private int rows;
  private int columns;
  //we calculate cellSize (based on width, height, rows, columns
  public Parser(){
  }

  public abstract int getWidth();

  public abstract int getHeight();

  public abstract String getTitle();

  public abstract int getRows();

  public abstract int getColumns();

  public abstract List<CellUnit> getInitialStates();


}
