package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.SegregationCell.SegregationState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Testing file for SegregationCell
 */
class SegregationCellTest {

  Cell cell;

  @BeforeEach
  void setUp() {
    cell = new SegregationCell(2, SegregationState.BLUE, SegregationState.EMPTY);
  }

  @Test
  void getCurrState() {
    assertEquals(SegregationState.BLUE, cell.getCurrState());
  }

  @Test
  void getNextState() {
    assertEquals(SegregationState.EMPTY, cell.getNextState());
  }

  @Test
  void setCurrState() {
    cell.setCurrState(SegregationState.EMPTY);
    assertEquals(SegregationState.EMPTY, cell.getCurrState());
  }

  @Test
  void setNextState() {
    cell.setNextState(SegregationState.RED);
    assertEquals(SegregationState.RED, cell.getNextState());
  }
}