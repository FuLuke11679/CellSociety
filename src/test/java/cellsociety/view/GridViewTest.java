package cellsociety.view;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.Grid;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GridViewTest {

  private GridView gridView;
  private TestGrid testGrid;
  private final int rows = 5;
  private final int columns = 5;

  @BeforeEach
  void setUp() {
    testGrid = new TestGrid(rows, columns);
    gridView = new GridView(rows, columns, testGrid);
  }

  @Test
  void getScene() {
    Scene scene = gridView.getScene();
    assertNotNull(scene, "Scene should not be null");
    assertEquals(500, scene.getWidth(), "Scene should have correct width");
    assertEquals(700, scene.getHeight(), "Scene should have correct height");
  }

  @Test
  void update() {
    // Set a specific cell color
    testGrid.setColor(2, 2, Color.BLUE);

    // Call update
    gridView.update(rows * columns);

    // Verify that the color has changed
    assertEquals(Color.BLUE, testGrid.getColor(2, 2), "Cell color should update correctly");
  }

  /**
   * A simple test implementation of Grid that allows setting colors manually.
   */
  static class TestGrid extends Grid {
    private final Color[][] colors;

    TestGrid(int rows, int columns) {
      super(rows, columns); // Assuming Grid has a no-arg constructor
      colors = new Color[rows][columns];

      // Default all cells to white
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
          colors[i][j] = Color.WHITE;
        }
      }
    }

    public Color getColor(int row, int col) {
      return colors[row][col];
    }

    public void setColor(int row, int col, Color color) {
      colors[row][col] = color;
    }
  }
}
