package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.cell.ConwayCell;
import cellsociety.state.CellState;
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
  private Grid grid;


  /**
   * Constructor for GridView.
   */
  public GridView(int rows, int columns, Grid grid) {
    this.rows = rows;
    this.columns = columns;
    this.cellSize = SIZE_GRID / rows;
    this.gridPane = new GridPane();
    this.cellRectangles = new Rectangle[rows][columns];
    this.grid = grid;

    initializeGrid();
    //hard coded for now
    String description = "Any live cell with fewer than two live neighbours dies, as if by underpopulation.\n"
        + "Any live cell with two or three live neighbours lives on to the next generation.\n"
        + "Any live cell with more than three live neighbours dies, as if by overpopulation.\n"
        + "Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.";
    //Hard coded for now
    setupSimulationInfo("Game of Life", "Random", "Luke, Daniel, Palo, and Ishan", description);

    BorderPane layout = new BorderPane();
    layout.setBottom(gridPane);
    layout.setTop(infoBox);

    this.myScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
  }

  /**
   * Initializes the grid from the given `CellUnit` list.
   */
  private void initializeGrid() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        Rectangle rect = new Rectangle(cellSize, cellSize);
        rect.setFill(grid.getColor(row, col));
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);
        gridPane.add(rect, col, row);  // (column, row) order
        cellRectangles[row][col] = rect;  // Store reference for quick updates
      }
    }
  }

  /**
   * Updates the grid efficiently by modifying only changed cells. Don't want to pass entire grid into front end.
   */
  public void update(List<Integer> updatedCells) {
    for (int id : updatedCells) {
      int row = id / columns;
      int col = id % columns;
      cellRectangles[row][col].setFill(grid.getColor(row, col));
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
    for(CellState state : ConwayCell.getStates()){
      Color stateColor = ConwayCell.getStateColor(state);
      infoBox.getChildren().add(new Text(state.toString() + ": " + Cell.getColorName(stateColor)));
    }
  }

  /**
   * Returns the updated scene.
   */
  public Scene getScene() {
    return myScene;
  }
}
