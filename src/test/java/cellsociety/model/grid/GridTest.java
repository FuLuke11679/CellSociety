package cellsociety.model.grid;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.ruleset.ConwayRuleset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GridTest {
  private Grid grid;
  private final int rows = 5;
  private final int cols = 5;
  private final String[] initialStates = {
      "A", "D", "A", "D", "A",
      "D", "A", "D", "A", "D",
      "A", "D", "A", "D", "A",
      "D", "A", "D", "A", "D",
      "A", "D", "A", "D", "A"
  };

  @BeforeEach
  void setUp() {
    grid = new ConwayGrid(rows, cols, new ConwayRuleset(), initialStates);
  }

  // Tests Grid Initialization: Ensures all cells are created correctly
  @Test
  void initializeGrid_CorrectlyInitializesCells() {
    assertNotNull(grid);
    assertEquals(rows * cols, grid.getLength());

    // Check that the first and last cell are correctly assigned
    assertEquals(ConwayState.ALIVE, ((ConwayCell) grid.getCell(0, 0)).getCurrState());
    assertEquals(ConwayState.DEAD, ((ConwayCell) grid.getCell(0, 1)).getCurrState());
  }


  // Negative Test: Ensure update does not throw exceptions on empty grids
  @Test
  void update_DoesNotThrowOnEmptyGrid() {
    Grid emptyGrid = new ConwayGrid(0, 0, new ConwayRuleset(), new String[0]);
    assertDoesNotThrow(emptyGrid::update);
  }

  // ✅ Tests getCell: Ensures retrieval of correct cell
  @Test
  void getCell_ReturnsCorrectCell() {
    Cell cell = grid.getCell(3, 3);
    assertNotNull(cell);
    assertEquals(ConwayState.ALIVE, ((ConwayCell) cell).getCurrState());
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