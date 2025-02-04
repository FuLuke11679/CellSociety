package cellsociety.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.cell.Cell;
import cellsociety.cell.ConwayCell;
import cellsociety.cell.ConwayCell.GameOfLifeState;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.*;

class ConwayRulesetTest {

  Cell cell;
  Ruleset ruleset;
  List<Cell> neighbors;

  @BeforeEach
  void setUp() {
    cell = new ConwayCell(2, null, GameOfLifeState.ALIVE);
    ruleset = new ConwayRuleset();
  }

  @Test
  void updateStateLessThan2AliveNeighbors() {
    cell.setCurrState(GameOfLifeState.ALIVE);
    neighbors = new ArrayList<>(List.of(
        new ConwayCell(0, GameOfLifeState.DEAD, GameOfLifeState.ALIVE),
        new ConwayCell(1, GameOfLifeState.DEAD, GameOfLifeState.DEAD),
        new ConwayCell(3, null, GameOfLifeState.DEAD),
        new ConwayCell(4, null, GameOfLifeState.ALIVE)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(GameOfLifeState.ALIVE, cell.getPrevState());
    assertEquals(GameOfLifeState.DEAD, cell.getCurrState());
    assertEquals(Color.WHITE, cell.getColor());
  }

  @Test
  void updateStateMoreThan3AliveNeighbors() {
    cell.setCurrState(GameOfLifeState.ALIVE);
    neighbors = new ArrayList<>(List.of(
        new ConwayCell(0, GameOfLifeState.ALIVE, GameOfLifeState.DEAD),
        new ConwayCell(1, GameOfLifeState.ALIVE, GameOfLifeState.DEAD),
        new ConwayCell(3, null, GameOfLifeState.ALIVE),
        new ConwayCell(4, null, GameOfLifeState.ALIVE)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(GameOfLifeState.ALIVE, cell.getPrevState());
    assertEquals(GameOfLifeState.DEAD, cell.getCurrState());
    assertEquals(Color.WHITE, cell.getColor());
  }

  @Test
  void updateStateReproduceWith2AliveNeighbors() {
    cell.setCurrState(GameOfLifeState.DEAD);
    neighbors = new ArrayList<>(List.of(
        new ConwayCell(0, GameOfLifeState.DEAD, GameOfLifeState.ALIVE),
        new ConwayCell(1, GameOfLifeState.ALIVE, GameOfLifeState.DEAD),
        new ConwayCell(3, null, GameOfLifeState.ALIVE),
        new ConwayCell(4, null, GameOfLifeState.DEAD)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(GameOfLifeState.DEAD, cell.getPrevState());
    assertEquals(GameOfLifeState.ALIVE, cell.getCurrState());
    assertEquals(Color.BLACK, cell.getColor());
  }

  @Test
  void updateStateReproduceWith3AliveNeighbors() {
    cell.setCurrState(GameOfLifeState.DEAD);
    neighbors = new ArrayList<>(List.of(
        new ConwayCell(0, GameOfLifeState.DEAD, GameOfLifeState.ALIVE),
        new ConwayCell(1, GameOfLifeState.ALIVE, GameOfLifeState.DEAD),
        new ConwayCell(3, null, GameOfLifeState.ALIVE),
        new ConwayCell(4, null, GameOfLifeState.ALIVE)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(GameOfLifeState.DEAD, cell.getPrevState());
    assertEquals(GameOfLifeState.ALIVE, cell.getCurrState());
    assertEquals(Color.BLACK, cell.getColor());
  }

}