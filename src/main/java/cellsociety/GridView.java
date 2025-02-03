package cellsociety;

import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GridView {
  private GridPane gridPane;
  private Scene myScene;
  private int rows;
  private int columns;
  private int cellSize;
  private final int SIZE_GRID = 500;
  private final int WINDOW_WIDTH = 500;
  private final int WINDOW_HEIGHT = 700;
  private VBox infoBox;
  private Rectangle[][] cellRectangles;  // Store references for easy updates

  /**
   * Constructor for GridView.
   */
  public GridView(int rows, int columns, List<List<CellUnit>> grid) {
    this.rows = rows;
    this.columns = columns;
    this.cellSize = SIZE_GRID / rows;
    this.gridPane = new GridPane();
    this.cellRectangles = new Rectangle[rows][columns];

    initializeFromGrid(grid);
    //Hard coded for now
    setupSimulationInfo("CA", "Wa-Tor World", "Luke", "This is a description");

    BorderPane layout = new BorderPane();
    layout.setCenter(gridPane);
    layout.setTop(infoBox);

    this.myScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
  }

  /**
   * Initializes the grid from the given `CellUnit` list.
   */
  private void initializeFromGrid(List<List<CellUnit>> grid) {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        Rectangle rect = new Rectangle(cellSize, cellSize);
        rect.setFill(grid.get(row).get(col).getColor());
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);
        gridPane.add(rect, col, row);  // (column, row) order
        cellRectangles[row][col] = rect;  // Store reference for quick updates
      }
    }
  }

  /**
   * Updates the grid efficiently by modifying only changed cells.
   */
  public void update(List<List<CellUnit>> grid, List<Integer> updatedCells) {
    for (int id : updatedCells) {
      int row = id / columns;
      int col = id % columns;
      cellRectangles[row][col].setFill(grid.get(row).get(col).getColor());
    }
  }

  /**
   * Displays simulation metadata at the top.
   */
  private void setupSimulationInfo(String simType, String simName, String author, String description) {
    infoBox = new VBox();
    infoBox.getChildren().addAll(
        new Text("Simulation: " + simName),
        new Text("Type: " + simType),
        new Text("Author: " + author),
        new Text("Description: " + description)
    );
  }

  /**
   * Returns the updated scene.
   */
  public Scene getScene() {
    return myScene;
  }
}
