package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

class ConwayRulesetTest {

  Cell cell;
  Ruleset ruleset;
  List<Cell> neighbors;

  @BeforeEach
  void setUp() {
    cell = new ConwayCell(2, ConwayState.ALIVE, null);
    ruleset = new ConwayRuleset();
  }

  @Test
  void updateCellStateLessThan2AliveNeighbors() {
    cell.setNextState(ConwayState.ALIVE);
    neighbors = new ArrayList<>(List.of(
        new ConwayCell(0, ConwayState.DEAD, ConwayState.ALIVE),
        new ConwayCell(1, ConwayState.DEAD, ConwayState.DEAD),
        new ConwayCell(3, null, ConwayState.DEAD),
        new ConwayCell(4, null, ConwayState.ALIVE)
    ));
    ruleset.updateCellState(cell, neighbors);
    assertEquals(ConwayState.ALIVE, cell.getCurrState());
    assertEquals(ConwayState.DEAD, cell.getNextState());
  }

  @Test
  void updateCellStateMoreThan3AliveNeighbors() {
    cell.setNextState(ConwayState.ALIVE);
    neighbors = new ArrayList<>(List.of(
        new ConwayCell(0, ConwayState.ALIVE, null),
        new ConwayCell(1, ConwayState.ALIVE, null),
        new ConwayCell(3, ConwayState.ALIVE, null),
        new ConwayCell(4, ConwayState.ALIVE, null)
    ));
    ruleset.updateCellState(cell, neighbors);
    assertEquals(ConwayState.ALIVE, cell.getCurrState());
    assertEquals(ConwayState.DEAD, cell.getNextState());
  }

  @Test
  void updateCellStateLiveOnWith2AliveNeighbors() {
    neighbors = new ArrayList<>(List.of(
        new ConwayCell(0, ConwayState.ALIVE, null),
        new ConwayCell(1, ConwayState.DEAD, null),
        new ConwayCell(3, ConwayState.ALIVE, null),
        new ConwayCell(4, ConwayState.DEAD, null)
    ));
    ruleset.updateCellState(cell, neighbors);
    assertEquals(ConwayState.ALIVE, cell.getNextState());
  }

  @Test
  void updateCellStateReproduceWith3AliveNeighbors() {
    cell.setCurrState(ConwayState.DEAD);
    neighbors = new ArrayList<>(List.of(
        new ConwayCell(0, ConwayState.ALIVE, null),
        new ConwayCell(1, ConwayState.DEAD, null),
        new ConwayCell(3, ConwayState.ALIVE, null),
        new ConwayCell(4, ConwayState.ALIVE, null)
    ));
    ruleset.updateCellState(cell, neighbors);
    assertEquals(ConwayState.DEAD, cell.getCurrState());
    assertEquals(ConwayState.ALIVE, cell.getNextState());
  }

}