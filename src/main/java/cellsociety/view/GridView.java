package cellsociety.view;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.cell.SugarscapePatch;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.grid.Grid;
import cellsociety.model.state.CellState;
import cellsociety.model.state.SugarscapeState;
import cellsociety.view.shapes.ShapeFactory;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 * GridView is responsible for rendering the simulation grid. It creates and updates the graphical
 * representation of the grid cells based on the simulation state.
 *
 * @author Luke
 * @author Daniel
 * @author Palo
 */
public class GridView {

  private final static Set<Class<?>> hasGradient = Set.of(SugarscapePatch.class);

  private final Pane gridPane;
  private final Scene myScene;
  private int rows;
  private int columns;
  private int cellSize;
  private final int SIZE_GRID = 600;
  private final int WINDOW_WIDTH = 600;
  private final int WINDOW_HEIGHT = 800;
  private VBox infoBox;
  private final Shape[][] cellShapes;
  private final Grid grid;
  private final Locale myLocale;
  private int numIterations;
  private String currentCellShape = "Rectangular";

  public enum ColorScheme {
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

  /**
   * Constructs a GridView with the specified parameters.
   *
   * @param rows        the number of rows in the grid
   * @param columns     the number of columns in the grid
   * @param simType     the type of simulation being displayed
   * @param title       the title of the simulation
   * @param author      the author of the simulation
   * @param description the description of the simulation
   * @param grid        the grid data model
   * @param scheme      the color scheme for the simulation
   * @param myLocale    the locale for resource bundle lookup
   */
  public GridView(int rows, int columns, String simType, String title, String author,
      String description, Grid grid, ColorScheme scheme, Locale myLocale) {
    this.rows = rows;
    this.columns = columns;
    this.cellSize = SIZE_GRID / rows;
    this.gridPane = new Pane();
    this.cellShapes = new Shape[rows][columns];
    this.grid = grid;
    this.myLocale = myLocale;
    this.numIterations = 0;
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myLocale);

    initializeGrid();
    setupSimulationInfo(simType, title, author, description);

    StackPane gridContainer = new StackPane(gridPane);
    gridContainer.setMinSize(SIZE_GRID, SIZE_GRID);
    gridContainer.setMaxSize(SIZE_GRID, SIZE_GRID);
    gridContainer.setAlignment(Pos.CENTER);  // Center it properly

    BorderPane layout = new BorderPane();
    layout.setCenter(gridContainer);
    layout.setTop(infoBox);
    this.myScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);

  }

  /**
   * Initializes the grid from the given `CellUnit` list.
   */
  private void initializeGrid() {
    gridPane.getChildren().clear();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        Shape shape = ShapeFactory.createShape(currentCellShape, cellSize, row, col);
        shape.setFill(getCellColor(grid.getCell(row, col)));
        shape.setStroke(Color.BLACK);
        gridPane.getChildren().add(shape);
        cellShapes[row][col] = shape;
      }
    }
  }

  /**
   * Updates the grid efficiently by modifying only changed cells. Don't want to pass entire grid
   * into front end.
   */
  public void update() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        Cell cell = grid.getCell(row, col);
        cellShapes[row][col].setFill(getCellColor(cell));
      }
    }
    this.numIterations++;
    incrementIterations();
  }

  /**
   * Determines the color of a cell based on its current state.
   *
   * @param cell the cell whose color is to be determined
   * @return the color representing the cell's state
   */
  private Color getCellColor(Cell cell) {
    if (hasGradient.contains(cell.getClass())) {
      SugarscapePatch patch = (SugarscapePatch) cell;
      if (patch.hasAgent()) {
        return Color.RED;
      } else {
        double fraction = (double) patch.getSugarAmount() / patch.getMaxSugar();
        return Color.WHITE.interpolate(cellColors.get(SugarscapeState.PATCH), fraction);
      }
    } else {
      return cellColors.get(cell.getCurrState());
    }
  }

  /**
   * Displays simulation metadata at the top.
   */
  private void setupSimulationInfo(String simType, String simName, String author,
      String description) {
    ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myLocale);
    infoBox = new VBox();
    infoBox.getChildren().addAll(
        new Text(simInfo.getString("simulation") + simName),
        new Text(simInfo.getString("sim_type") + simType),
        new Text(simInfo.getString("author") + author),
        new Text(simInfo.getString("description") + description),
        new Text(simInfo.getString("iterations") + this.numIterations)
    );
  }

  /**
   * Redraws the grid if the cell shape is changed
   *
   * @param newRows       Number of rows
   * @param newCols       Number of columns
   * @param newShapeClass New shape we are redrawing to
   */
  public void redrawGrid(int newRows, int newCols, String newShapeClass) {
    this.rows = newRows;
    this.columns = newCols;
    this.cellSize = SIZE_GRID / Math.max(rows, columns);
    this.currentCellShape = ShapeFactory.getFullyQualifiedName(newShapeClass);
    initializeGrid();
  }

  /**
   * Increments the number of iterations displayed on the Grid
   */
  private void incrementIterations() {
    if (!infoBox.getChildren().isEmpty()) {
      Text iterationsText = (Text) infoBox.getChildren().get(infoBox.getChildren().size() - 1);
      ResourceBundle simInfo = ResourceBundle.getBundle("SimInfo", myLocale);

      iterationsText.setText(simInfo.getString("iterations") + this.numIterations);
    }

  }

  /**
   * Returns the updated scene.
   */
  public Scene getScene() {
    return myScene;
  }
}
