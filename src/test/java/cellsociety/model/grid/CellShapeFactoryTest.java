package cellsociety.model.grid;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cellsociety.model.grid.shape.CellShape;
import cellsociety.model.grid.shape.RectangularShape;
import org.junit.jupiter.api.Test;

class CellShapeFactoryTest {

  @Test
  void testCreateCellShapeValid() {
    // For a valid type, e.g., "Rectangular", the factory should return a RectangularShape.
    CellShape cellShape = CellShapeFactory.createCellShape("Rectangular");
    assertNotNull(cellShape, "The CellShape instance should not be null.");
    assertTrue(cellShape instanceof RectangularShape, "Expected instance of RectangularShape.");
  }

  @Test
  void testCreateCellShapeInvalidFallsBack() {
    // For an invalid type, the factory should fall back to RectangularShape.
    CellShape cellShape = CellShapeFactory.createCellShape("NonExistentShape");
    assertNotNull(cellShape, "The fallback CellShape instance should not be null.");
    assertTrue(cellShape instanceof RectangularShape,
        "Expected fallback instance of RectangularShape.");
  }
}
