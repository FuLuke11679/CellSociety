package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.PercolationCell.PercolationState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PercolationCellTest {

  Cell cell;

  @BeforeEach
  void setUp() {
    cell = new PercolationCell(0, PercolationState.OPEN, PercolationState.PERCOLATED);
  }

  @Test
  void getPrevState() {
    assertEquals(PercolationState.OPEN, cell.getPrevState());
  }

  @Test
  void getCurrState() {
    assertEquals(PercolationState.PERCOLATED, cell.getCurrState());
  }

  @Test
  void setPrevState() {
    cell.setPrevState(PercolationState.PERCOLATED);
    assertEquals(PercolationState.PERCOLATED, cell.getPrevState());
  }

  @Test
  void setCurrState() {
    cell.setCurrState(PercolationState.BLOCKED);
    assertEquals(PercolationState.BLOCKED, cell.getCurrState());
  }
}