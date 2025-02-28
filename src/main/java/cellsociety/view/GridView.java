package cellsociety.view;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.cell.SugarscapePatch;
import cellsociety.model.state.SugarscapeState;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.grid.Grid;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.state.CellState;
import cellsociety.view.shapes.ShapeFactory;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
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
import javafx.scene.shape.Shape;
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
  private Shape[][] cellShapes;
  private Grid grid;
  private Locale myLocale;
  private String currentCellShape = "Rectangular";

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
      Map.entry(SugarscapeState.PATCH, Color.GREEN),
      Map.entry(SugarscapeState.AGENT, Color.RED)
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
  public GridView(int rows, int columns, String simType, String title, String author, String description, Grid grid, ColorScheme scheme, Locale myLocale) {
    this.rows = rows;
    this.columns = columns;
    this.cellSize = SIZE_GRID / rows;
    this.gridPane = new GridPane();
    this.cellShapes = new Shape[rows][columns];
    this.grid = grid;
    this.myLocale = myLocale;

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
    gridPane.getChildren().clear();  // ✅ Clears old shapes before adding new ones
    gridPane.setGridLinesVisible(true);

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        Shape shape = ShapeFactory.createShape(currentCellShape, cellSize, row, col);
        shape.setFill(getCellColor(grid.getCell(row, col)));
        shape.setStroke(Color.BLACK);
        gridPane.add(shape, col, row);
        cellShapes[row][col] = shape;
      }
    }
  }

  /**
   * Updates the grid efficiently by modifying only changed cells. Don't want to pass entire grid into front end.
   */
  public void update() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        Cell cell = grid.getCell(row, col);
        cellShapes[row][col].setFill(getCellColor(cell));
      }
    }
  }
  private Color getCellColor(Cell cell) {
    if (cell instanceof SugarscapePatch) {
      SugarscapePatch patch = (SugarscapePatch) cell;
      if (patch.hasAgent()) {
        return Color.RED;
      } else {
        double fraction = (double) patch.getSugarAmount() / patch.getMaxSugar();
        return Color.WHITE.interpolate(Color.DARKGREEN, fraction);
      }
    } else {
      return cellColors.get(cell.getCurrState());
    }
  }

  /**
   * Displays simulation metadata at the top.
   */
  private void setupSimulationInfo(String simType, String simName, String author, String description) {
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myLocale);
    infoBox = new VBox();
    infoBox.getChildren().addAll(
        new Text(simInfo.getString("simulation") + simName),
        new Text(simInfo.getString("sim_type") + simType),
        new Text(simInfo.getString("author") + author),
        new Text(simInfo.getString("description") + description)
    );
  }
  public void redrawGrid(int newRows, int newCols, String newShapeClass) {
    this.rows = newRows;
    this.columns = newCols;
    this.cellSize = SIZE_GRID / Math.max(rows, columns);
    this.currentCellShape = ShapeFactory.getFullyQualifiedName(newShapeClass);
    initializeGrid();
  }

  /**
   * Returns the updated scene.
   */
  public Scene getScene() {
    return myScene;
  }
}
