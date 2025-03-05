package cellsociety.model.grid;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.factory.CellFactory;
import cellsociety.model.cell.SugarscapePatch;
import cellsociety.model.ruleset.SugarscapeRuleset;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SugarscapeGridTest {
  private SugarscapeGrid grid;
  private final int rows = 5;
  private final int cols = 5;
  private final String[] initialStates = {
      "PATCH", "PATCH", "PATCH", "PATCH", "PATCH",
      "PATCH", "AGENT", "PATCH", "PATCH", "PATCH",
      "PATCH", "PATCH", "PATCH", "PATCH", "PATCH",
      "PATCH", "PATCH", "PATCH", "AGENT", "PATCH",
      "PATCH", "PATCH", "PATCH", "PATCH", "PATCH"
  };
  private final int[] initialValues = {
      1,2,3,4,5,
      1,2,3,4,5,
      1,2,3,4,5,
      1,2,3,4,5,
      1,2,3,4,5};

  @BeforeEach
  void setUp() {
    Map<String, String> params = Map.of(
        "sugarGrowBackRate", "1",
        "sugarGrowBackInterval", "1",
        "agentVision", "3",
        "agentMetabolism", "2"
    );
    SugarscapeRuleset sugarRuleset = new SugarscapeRuleset(params);
    sugarRuleset.setInitialValues(initialValues);
    grid = sugarRuleset.createGrid(rows, cols, initialStates);
  }

  @Test
  void hasAgentAt_ReturnsTrueWhenAgentExists() {
    assertTrue(grid.hasAgentAt(1, 1));
    assertTrue(grid.hasAgentAt(3, 3));
  }

  @Test
  void hasAgentAt_ReturnsFalseWhenNoAgent() {
    assertFalse(grid.hasAgentAt(0, 0));
    assertFalse(grid.hasAgentAt(4, 4));
  }

  @Test
  void createCell_CreatesPatchCorrectly() {
    SugarscapePatch cell = (SugarscapePatch) CellFactory.createCell(0, cellsociety.model.state.SugarscapeState.PATCH);
    assertNotNull(cell);
    assertEquals(1, cell.getSugarAmount());
  }

  @Test
  void getNeighbors_ReturnsCorrectNeighborCount() {
    assertEquals(4, grid.getNeighbors(2, 2).size()); // Middle cell should have 4 neighbors
    assertEquals(4, grid.getNeighbors(0, 0).size()); // Corner cell should have 4 neighbors still. The graph wraps around.
  }

  @Test
  void update_DoesNotThrowOnGridUpdate() {
    assertDoesNotThrow(grid::update);
  }

}