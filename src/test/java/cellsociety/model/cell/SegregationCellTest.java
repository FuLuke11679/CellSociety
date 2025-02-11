package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.SegregationCell.SegregationState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SegregationCellTest {

  Cell cell;

  @BeforeEach
  void setUp() {
    cell = new SegregationCell(2, SegregationState.BLUE, SegregationState.EMPTY);
  }

  @Test
  void getPrevState() {
    assertEquals(SegregationState.BLUE, cell.getPrevState());
  }

  @Test
  void getCurrState() {
    assertEquals(SegregationState.EMPTY, cell.getCurrState());
  }

  @Test
  void setPrevState() {
    cell.setPrevState(SegregationState.EMPTY);
    assertEquals(SegregationState.EMPTY, cell.getPrevState());
  }

  @Test
  void setCurrState() {
    cell.setCurrState(SegregationState.RED);
    assertEquals(SegregationState.RED, cell.getCurrState());
  }
}