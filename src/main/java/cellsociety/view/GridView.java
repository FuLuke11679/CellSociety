package cellsociety.view;

import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.cell.SugarscapeCell.SugarscapeState;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.grid.Grid;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.state.CellState;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
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
  private final int SIZE_GRID = 600;
  private final int WINDOW_WIDTH = 600;
  private final int WINDOW_HEIGHT = 800;
  private VBox infoBox;
  Rectangle[][] cellRectangles;  // Store references for easy updates
  private Grid grid;

  public enum ColorScheme{
    LIGHT,
    DARK,
    DUKE,
    UNC
  }

  private final Map<CellState, Color> cellColors = Map.ofEntries(
      Map.entry(ConwayState.ALIVE, Color.BLACK),
      Map.entry(ConwayState.DEAD, Color.WHITE),
      Map.entry(PercolationState.BLOCKED, Color.BLACK),
      Map.entry(PercolationState.PERCOLATED, Color.BLUE),
      Map.entry(PercolationState.OPEN, Color.WHITE),
      Map.entry(FireState.TREE, Color.GREEN),
      Map.entry(FireState.BURNING, Color.BROWN),
      Map.entry(FireState.EMPTY, Color.WHITE),
      Map.entry(SegregationState.BLUE, Color.BLUE),
      Map.entry(SegregationState.RED, Color.RED),
      Map.entry(SegregationState.EMPTY, Color.WHITE),
      Map.entry(WatorState.SHARK, Color.LIGHTBLUE),
      Map.entry(WatorState.FISH, Color.LIGHTGREEN),
      Map.entry(WatorState.WATER, Color.WHITE),
      Map.entry(SugarscapeState.EMPTY, Color.WHITE),
      Map.entry(SugarscapeState.LOW_SUGAR, Color.YELLOW),
      Map.entry(SugarscapeState.MEDIUM_SUGAR, Color.ORANGE),
      Map.entry(SugarscapeState.HIGH_SUGAR, Color.RED),
      Map.entry(SugarscapeState.OCCUPIED, Color.BLACK)
  );

  private final Map<ColorScheme, Color> schemeColors = Map.ofEntries(
      Map.entry(ColorScheme.LIGHT, Color.WHITESMOKE),
      Map.entry(ColorScheme.DARK, Color.GRAY),
      Map.entry(ColorScheme.DUKE, Color.BLUE),
      Map.entry(ColorScheme.UNC, Color.LIGHTBLUE)
  );

  /**
   * Constructor for GridView.
   */
  public GridView(int rows, int columns, String simType, String title, String author, String description, Grid grid, ColorScheme scheme) {
    this.rows = rows;
    this.columns = columns;
    this.cellSize = SIZE_GRID / rows;
    this.gridPane = new GridPane();
    this.cellRectangles = new Rectangle[rows][columns];
    this.grid = grid;

    initializeGrid();
    setupSimulationInfo(simType, title, author, description);

    BorderPane layout = new BorderPane();
    layout.setBottom(gridPane);
    layout.setTop(infoBox);
    layout.setBackground(new Background(new BackgroundFill(schemeColors.get(scheme), CornerRadii.EMPTY, Insets.EMPTY)));

    this.myScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
  }

  /**
   * Initializes the grid from the given `CellUnit` list.
   */
  private void initializeGrid() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        Rectangle rect = new Rectangle(cellSize, cellSize);
        rect.setFill(cellColors.get(grid.getCell(row, col).getCurrState()));
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
  public void update() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        cellRectangles[row][col].setFill(cellColors.get(grid.getCell(row, col).getCurrState()));
      }
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
