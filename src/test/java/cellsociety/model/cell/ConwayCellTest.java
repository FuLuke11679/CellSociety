package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cellsociety.model.cell.ConwayCell.ConwayState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Testing file for the ConwayCell
 */
class ConwayCellTest {

  Cell cell;

  @BeforeEach
  void setUp() {
    cell = new ConwayCell(0, ConwayState.DEAD, ConwayState.ALIVE);
  }

  @Test
  void getCurrState() {
    assertEquals(ConwayState.DEAD, cell.getCurrState());
  }

  @Test
  void getNextState() {
    assertEquals(ConwayState.ALIVE, cell.getNextState());
  }

  @Test
  void setCurrState() {
    cell.setCurrState(ConwayState.ALIVE);
    assertEquals(ConwayState.ALIVE, cell.getCurrState());
  }

  @Test
  void setNextState() {
    cell.setNextState(ConwayState.DEAD);
    assertEquals(ConwayState.DEAD, cell.getNextState());
  }
}