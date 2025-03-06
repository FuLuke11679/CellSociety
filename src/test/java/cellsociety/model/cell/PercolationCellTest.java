package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cellsociety.model.cell.PercolationCell.PercolationState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Testing file for the PercolationCell
 */
class PercolationCellTest {

  Cell cell;

  @BeforeEach
  void setUp() {
    cell = new PercolationCell(0, PercolationState.OPEN, PercolationState.PERCOLATED);
  }

  @Test
  void getCurrState() {
    assertEquals(PercolationState.OPEN, cell.getCurrState());
  }

  @Test
  void getNextState() {
    assertEquals(PercolationState.PERCOLATED, cell.getNextState());
  }

  @Test
  void setCurrState() {
    cell.setCurrState(PercolationState.PERCOLATED);
    assertEquals(PercolationState.PERCOLATED, cell.getCurrState());
  }

  @Test
  void setNextState() {
    cell.setNextState(PercolationState.BLOCKED);
    assertEquals(PercolationState.BLOCKED, cell.getNextState());
  }
}