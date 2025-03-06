package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cellsociety.model.cell.FireCell.FireState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FireCellTest {

  Cell cell;

  @BeforeEach
  void setUp() {
    cell = new FireCell(0, FireState.BURNING, FireState.EMPTY);
  }

  @Test
  void getCurrState() {
    assertEquals(FireState.BURNING, cell.getCurrState());
  }

  @Test
  void getNextState() {
    assertEquals(FireState.EMPTY, cell.getNextState());
  }

  @Test
  void setCurrState() {
    cell.setCurrState(FireState.BURNING);
    assertEquals(FireState.BURNING, cell.getCurrState());
  }

  @Test
  void setNextState() {
    cell.setNextState(FireState.EMPTY);
    assertEquals(FireState.EMPTY, cell.getNextState());
  }
}