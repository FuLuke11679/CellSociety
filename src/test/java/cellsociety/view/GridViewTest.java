package cellsociety.view;

import cellsociety.model.Grid;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.ruleset.ConwayRuleset;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.Scene;

import static org.junit.jupiter.api.Assertions.*;

class GridViewTest {

  private GridView gridView;
  private Grid grid;

  @BeforeEach
  void setUp() {
    String[] initialCells = {
        "A", "D", "A", "D",
        "D", "A", "D", "A",
        "A", "D", "A", "D",
        "D", "A", "D", "A"
    };
    grid = new Grid(4, 4, new ConwayRuleset(), initialCells);
    gridView = new GridView(4, 4, grid);
  }

  @Test
  void testUpdate() {
    grid.update();
    gridView.update(16); // Should update the grid view with 16 cells.
    Rectangle firstCell = (Rectangle) gridView.getScene().getRoot().getChildrenUnmodifiable().get(0);

    // Now you can safely call getFill() on the Rectangle
    assertEquals(Color.WHITE, firstCell.getFill(), "The first cell should be white after update.");
  }


  @Test
  void testGetScene() {
    Scene scene = gridView.getScene();
    assertNotNull(scene, "Scene should not be null.");
  }
}
