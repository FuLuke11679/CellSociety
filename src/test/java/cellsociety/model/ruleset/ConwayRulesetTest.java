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
  void updateCellState_MoreThan3AliveNeighbors_CellDies() {
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
  void updateCellState_CellWith2AliveNeighbors_CellStaysAlive() {
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
  void updateCellState_CellWith3AliveNeighbors_CellBecomesAlive() {
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