package cellsociety.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.cell.Cell;
import cellsociety.cell.GameOfLifeCell;
import cellsociety.cell.GameOfLifeCell.State;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.*;

class GameOfLifeRulesetTest {

  Cell cell;
  Ruleset ruleset;
  List<Cell> neighbors;

  @BeforeEach
  void setUp() {
    cell = new GameOfLifeCell(2, null, State.ALIVE);
    ruleset = new GameOfLifeRuleset();
  }

  @Test
  void updateStateLessThan2AliveNeighbors() {
    cell.setCurrState(State.ALIVE);
    neighbors = new ArrayList<>(List.of(
        new GameOfLifeCell(0, State.DEAD, State.ALIVE),
        new GameOfLifeCell(1, State.DEAD, State.DEAD),
        new GameOfLifeCell(3, null, State.DEAD),
        new GameOfLifeCell(4, null, State.ALIVE)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(State.ALIVE, cell.getPrevState());
    assertEquals(State.DEAD, cell.getCurrState());
    assertEquals(Color.WHITE, cell.getColor());
  }

  @Test
  void updateStateMoreThan3AliveNeighbors() {
    cell.setCurrState(State.ALIVE);
    neighbors = new ArrayList<>(List.of(
      new GameOfLifeCell(0, State.ALIVE, State.DEAD),
      new GameOfLifeCell(1, State.ALIVE, State.DEAD),
      new GameOfLifeCell(3, null, State.ALIVE),
      new GameOfLifeCell(4, null, State.ALIVE)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(State.ALIVE, cell.getPrevState());
    assertEquals(State.DEAD, cell.getCurrState());
    assertEquals(Color.WHITE, cell.getColor());
  }

  @Test
  void updateStateReproduceWith2AliveNeighbors() {
    cell.setCurrState(State.DEAD);
    neighbors = new ArrayList<>(List.of(
        new GameOfLifeCell(0, State.DEAD, State.ALIVE),
        new GameOfLifeCell(1, State.ALIVE, State.DEAD),
        new GameOfLifeCell(3, null, State.ALIVE),
        new GameOfLifeCell(4, null, State.DEAD)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(State.DEAD, cell.getPrevState());
    assertEquals(State.ALIVE, cell.getCurrState());
    assertEquals(Color.BLACK, cell.getColor());
  }

  @Test
  void updateStateReproduceWith3AliveNeighbors() {
    cell.setCurrState(State.DEAD);
    neighbors = new ArrayList<>(List.of(
        new GameOfLifeCell(0, State.DEAD, State.ALIVE),
        new GameOfLifeCell(1, State.ALIVE, State.DEAD),
        new GameOfLifeCell(3, null, State.ALIVE),
        new GameOfLifeCell(4, null, State.ALIVE)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(State.DEAD, cell.getPrevState());
    assertEquals(State.ALIVE, cell.getCurrState());
    assertEquals(Color.BLACK, cell.getColor());
  }

}