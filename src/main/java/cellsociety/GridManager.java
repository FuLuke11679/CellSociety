package cellsociety;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class GridManager {
  private GridPane gridPane;
  private int rows;
  private int columns;
  private int cellSize;
  private Color aliveColor;
  private Color deadColor;
  private double density;

  /**
   * Constructor for GridManager.
   *
   * @param rows       Number of rows in the grid.
   * @param columns    Number of columns in the grid.
   * @param cellSize   Size of each cell in pixels.
   * @param aliveColor Color for alive cells.
   * @param deadColor  Color for dead cells.
   * @param density    Density of alive cells in the initial state.
   */
  public GridManager(int rows, int columns, int cellSize, Color aliveColor, Color deadColor, double density) {
    this.rows = rows;
    this.columns = columns;
    this.cellSize = cellSize;
    this.aliveColor = aliveColor;
    this.deadColor = deadColor;
    this.density = density;
    this.gridPane = new GridPane();
    this.gridPane.setHgap(1); // Small gap between cells
    this.gridPane.setVgap(1);
  }

  /**
   * Initialize the grid with random states based on the density.
   */
  public void initializeGrid() {
    Random random = new Random();
    for (int x = 0; x < rows; x++) {
      for (int y = 0; y < columns; y++) {
        Rectangle rect = new Rectangle(cellSize, cellSize);
        rect.setFill(random.nextDouble() < density ? aliveColor : deadColor);
        gridPane.add(rect, y, x); // Note: GridPane uses (column, row) order
      }
    }
  }

  /**
   * Get the GridPane for display.
   *
   * @return The GridPane containing the grid.
   */
  public GridPane getGridPane() {
    return gridPane;
  }
}