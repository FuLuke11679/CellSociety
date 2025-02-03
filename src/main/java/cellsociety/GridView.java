package cellsociety;

import java.util.List;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridView {
  private Grid myGrid;
  private Scene myScene;
  private Group root;
  private int rows;
  private int columns;
  private int cellSize;
  private final int SIZE_GRID = 500;
  private final int WINDOW_WIDTH = 500;
  private final int WINDOW_HEIGHT = 700;



  /**
   *
   * @param rows      Number of rows in Grid
   * @param columns   Number of columns in Grid
   */
  //add Grid object to constructor later
  public GridView(int rows, int columns, List<List<CellUnit>> grid) {
    this.rows = rows;
    this.columns = columns;
    this.cellSize = SIZE_GRID/rows;
    //this.myGrid = grid;  //takes grid object as parameter (has to read entire Grid everytime)
    this.root = new Group();
    //initializeGridView();
    initializeFromGrid(grid);
  }


  public void initializeFromGrid(List<List<CellUnit>> grid){
    for(List<CellUnit> row: grid){
      for (CellUnit cell: row){
        Rectangle rect = new Rectangle(cellSize, cellSize);
        rect.setFill(cell.getColor());
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);
        rect.setX(cell.getID()%columns * cellSize);
        rect.setY(cell.getID()/columns * cellSize + WINDOW_HEIGHT-SIZE_GRID);
        root.getChildren().add(rect);
      }
    }
    myScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
  }


  public void update(List<List<CellUnit>> grid, List<Integer> updatedCells){
    //don't want to return a new scene object
    for(int id: updatedCells){
      int row = id/rows;
      int col = id%columns;
      root.getChildren().remove(removeRectangleAt(row, col));
      Rectangle rect = new Rectangle(cellSize, cellSize);
      rect.setFill(grid.get(row).get(col).getColor());
      rect.setStroke(Color.BLACK);
      rect.setStrokeWidth(1);
      rect.setX(col * cellSize);
      rect.setY(row * cellSize + WINDOW_HEIGHT-SIZE_GRID);
      root.getChildren().add(rect);
    }
  }

  // Method to remove a Rectangle at a specific X, Y coordinate
  private Rectangle removeRectangleAt(double targetX, double targetY) {
    // Search for the rectangle with the given coordinates
    for (javafx.scene.Node node : root.getChildren()) {
      if (node instanceof Rectangle) {
        Rectangle rect = (Rectangle) node;
        if (rect.getX() == targetX && rect.getY() == targetY) {
          return rect;
        }
      }
    }
    return null;
  }

  public Scene getScene(){
    return myScene;
  }


}
