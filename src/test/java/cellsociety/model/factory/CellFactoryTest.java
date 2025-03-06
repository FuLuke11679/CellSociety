package cellsociety.model.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.state.CellState;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Testing file for the CellFactory object
 */
class CellFactoryTest {

  @Test
  void createCell_validCellName_success() {
    Cell c = CellFactory.createCell(0, ConwayState.ALIVE, null);
    assertNotNull(c);
  }

  @Test
  void createCell_invalidCellName_failure() { // Negative Test
    assertThrows(RuntimeException.class,
        () -> CellFactory.createCell(0, fakeStates.FAKE1, null));
  }

  enum fakeStates implements CellState {
    FAKE1,
    FAKE2
  }

}