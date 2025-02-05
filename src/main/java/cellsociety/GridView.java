package cellsociety;

import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
  private VBox buttons;
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
    //Hard coded for now
    setupSimulationInfo("CA", "Wa-Tor World", "Luke", "This is a description");

    BorderPane layout = new BorderPane();
    layout.setCenter(gridPane);
    layout.setTop(infoBox);
    layout.setRight(buttons);  //new

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
  public void update(int length) {
    for (int id = 0; id < length; id++) {
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
    //new
    buttons = new VBox();
    Button button1 = new Button("Button 1");
    Button button2 = new Button("Button 2");
    Button button3 = new Button("Button 3");
    buttons.getChildren().addAll(button1, button2, button3);
  }

  /**
   * Returns the updated scene.
   */
  public Scene getScene() {
    return myScene;
  }
}
