package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.FireCell;
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
  void getPrevState() {
    assertEquals(FireState.BURNING, cell.getPrevState());
  }

  @Test
  void getCurrState() {
    assertEquals(FireState.EMPTY, cell.getCurrState());
  }

  @Test
  void setPrevState() {
    cell.setPrevState(FireState.BURNING);
    assertEquals(FireState.BURNING, cell.getPrevState());
  }

  @Test
  void setCurrState() {
    cell.setCurrState(FireState.EMPTY);
    assertEquals(FireState.EMPTY, cell.getCurrState());
  }
}