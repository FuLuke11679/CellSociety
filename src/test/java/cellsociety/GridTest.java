package cellsociety;

import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GridTest {
  private Grid grid;
  private final int rows = 5;
  private final int cols = 5;

  @BeforeEach
  void setUp() {
    grid = new Grid(rows, cols);
  }

  @Test
  void testInitializeGridNotEmpty() {
    for (int x = 0; x < rows; x++) {
      for (int y = 0; y < cols; y++) {
        assertNotNull(grid.getColor(x, y));
      }
    }
  }

  @Test
  void testInitializeGridCorrectSize() {
    assertEquals(rows * cols, grid.getLength());
  }

  @Test
  void testUpdateChangesCellStates() {
    Color beforeUpdate = grid.getColor(2, 2);
    grid.update();
    Color afterUpdate = grid.getColor(2, 2);
    assertNotEquals(beforeUpdate, afterUpdate);
  }

  @Test
  void testUpdatePreservesGridSize() {
    grid.update();
    assertEquals(rows * cols, grid.getLength());
  }

  @Test
  void testGetColorReturnsValidColor() {
    assertTrue(grid.getColor(0, 0) == Color.BLACK || grid.getColor(0, 0) == Color.WHITE);
  }

  @Test
  void testGetColorOutOfBoundsThrowsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> grid.getColor(rows, cols));
  }

  @Test
  void testGetLengthMatchesInitialization() {
    assertEquals(rows * cols, grid.getLength());
  }

  @Test
  void testGetLengthUnchangedAfterUpdate() {
    grid.update();
    assertEquals(rows * cols, grid.getLength());
  }
}
