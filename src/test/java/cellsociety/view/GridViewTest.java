package cellsociety.view;

import cellsociety.model.grid.ConwayGrid;
import cellsociety.model.grid.Grid;
import cellsociety.model.ruleset.ConwayRuleset;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GridViewTest {

  private static GridView gridView;
  private static Grid grid;
  private static final int ROWS = 4;
  private static final int COLUMNS = 4;

  @BeforeAll
  static void initJavaFX() {
    Thread thread = new Thread(() -> Platform.startup(() -> {}));
    thread.setDaemon(true);
    thread.start();
  }

  @BeforeEach
  void setUp() throws Exception {
    Platform.runLater(() -> {
      String[] initialCells = {
          "A", "D", "A", "D",
          "D", "D", "D", "A",
          "A", "D", "A", "D",
          "D", "A", "D", "A"
      };
      grid = new ConwayGrid(ROWS, COLUMNS, new ConwayRuleset(), initialCells);
      gridView = new GridView(ROWS, COLUMNS, grid);
    });
    Thread.sleep(500); // Allow JavaFX to initialize
  }

  @Test
  void testUpdate() throws Exception {
    Platform.runLater(() -> {
      grid.update();

      // Call update() on GridView
      gridView.update();

      // Access cell rectangles (assuming public or through a getter)
      Rectangle[][] cellRectangles = gridView.cellRectangles;

      // Verify UI updates reflect grid changes
      assertEquals(Color.WHITE, cellRectangles[0][0].getFill(), "Top-left cell should be WHITE.");
      assertEquals(Color.BLACK, cellRectangles[1][1].getFill(), "Cell (1,1) should be BLACK.");
    });

    Thread.sleep(500); // Allow JavaFX to update UI before assertions
  }

}

