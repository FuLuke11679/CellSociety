package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell;
import cellsociety.model.cell.ConwayCell.ConwayState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConwayCellTest {

  Cell cell;

  @BeforeEach
  void setUp() {
    cell = new ConwayCell(0, ConwayState.DEAD, ConwayState.ALIVE);
  }

  @Test
  void getPrevState() {
    assertEquals(ConwayState.DEAD, cell.getPrevState());
  }

  @Test
  void getCurrState() {
    assertEquals(ConwayState.ALIVE, cell.getCurrState());
  }

  @Test
  void setPrevState() {
    cell.setPrevState(ConwayState.ALIVE);
    assertEquals(ConwayState.ALIVE, cell.getPrevState());
  }

  @Test
  void setCurrState() {
    cell.setCurrState(ConwayState.DEAD);
    assertEquals(ConwayState.DEAD, cell.getCurrState());
  }
}