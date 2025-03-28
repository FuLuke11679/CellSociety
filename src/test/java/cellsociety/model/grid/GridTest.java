package cellsociety.model.grid;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.ruleset.ConwayRuleset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GridTest {

  private final int rows = 5;
  private final int cols = 5;
  private final String[] initialStates = {
      "A", "D", "A", "D", "A",
      "D", "A", "D", "A", "D",
      "A", "D", "A", "D", "A",
      "D", "A", "D", "A", "D",
      "A", "D", "A", "D", "A"
  };
  private Grid grid;

  @BeforeEach
  void setUp() {
    grid = new ConwayGrid(rows, cols, new ConwayRuleset(), initialStates);
  }

  // Negative Test: Ensure update does not throw exceptions on empty grids
  @Test
  void update_DoesNotThrowOnEmptyGrid() {
    Grid emptyGrid = new ConwayGrid(0, 0, new ConwayRuleset(), new String[0]);
    assertDoesNotThrow(emptyGrid::update);
  }

  // Negative Test: Ensure out-of-bounds access throws exception
  @Test
  void getCell_ThrowsExceptionForInvalidCoordinates() {
    assertThrows(IndexOutOfBoundsException.class, () -> grid.getCell(-1, 0));
    assertThrows(IndexOutOfBoundsException.class, () -> grid.getCell(rows, cols));
  }

  // Tests getLength: Ensures correct grid size
  @Test
  void getLength_ReturnsCorrectValue() {
    assertEquals(rows * cols, grid.getLength());
  }

  // Negative Test: Ensure empty grid returns zero length
  @Test
  void getLength_ReturnsZeroForEmptyGrid() {
    Grid emptyGrid = new ConwayGrid(0, 0, new ConwayRuleset(), new String[0]);
    assertEquals(0, emptyGrid.getLength());
  }

}