package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.grid.SugarscapeGrid;
import cellsociety.model.cell.SugarscapePatch;
import cellsociety.model.agent.SugarscapeAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SugarscapeRulesetTest {
  private SugarscapeRuleset ruleset;
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
      1, 2, 3, 4, 5,
      1, 2, 3, 4, 5,
      1, 2, 3, 4, 5,
      1, 2, 3, 4, 5,
      1, 2, 3, 4, 5
  };

  @BeforeEach
  void setUp() {
    ruleset = new SugarscapeRuleset(2, 1, 3, 2);
    ruleset.setInitialValues(initialValues);
    grid = ruleset.createGrid(rows, cols, initialStates);

  }

  @Test
  void setInitialValues_SetsValuesCorrectly() {
    assertArrayEquals(initialValues, ruleset.getInitialValues());
  }

  @Test
  void updateGridState_DoesNotThrow() {
    assertDoesNotThrow(() -> ruleset.updateGridState());
  }

  @Test
  void updateCellState_DoesNotThrow() {
    SugarscapePatch cell = (SugarscapePatch) grid.getCell(2, 2);
    assertDoesNotThrow(() -> ruleset.updateCellState(cell, grid.getNeighbors(2, 2)));
  }

  @Test
  void createGrid_CreatesGridSuccessfully() {
    SugarscapeGrid newGrid = ruleset.createGrid(rows, cols, initialStates);
    assertNotNull(newGrid);
    assertEquals(rows, newGrid.getRows());
    assertEquals(cols, newGrid.getColumns());
  }

  @Test
  void getInitialValues_ReturnsCorrectValues() {
    assertArrayEquals(initialValues, ruleset.getInitialValues());
  }
}
