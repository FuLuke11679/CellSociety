package cellsociety.model.factory;

import static org.junit.jupiter.api.Assertions.*;

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

  enum fakeStates implements CellState {
    FAKE1,
    FAKE2
  }

  @Test
  void createCell_validCellName_success() {
    Cell c = CellFactory.createCell(0, ConwayState.ALIVE);
    assertNotNull(c);
  }

  @Test
  void createCell_invalidCellName_failure() { // Negative Test
    assertThrows(RuntimeException.class,
        () -> CellFactory.createCell(0, fakeStates.FAKE1));
  }

}