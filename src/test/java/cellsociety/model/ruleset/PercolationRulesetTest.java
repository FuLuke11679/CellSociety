package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.PercolationCell;
import cellsociety.model.cell.PercolationCell.PercolationState;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class PercolationRulesetTest {

  Cell cell;
  List<Cell> neighbors;
  Ruleset ruleset;

  @BeforeEach
  void setUp() {
    ruleset = new PercolationRuleset();
  }

  @Test
  void updateStateForBlockedCell() {
    cell = new PercolationCell(2, null, PercolationState.BLOCKED);
    neighbors = new ArrayList<>(List.of(
        new PercolationCell(0, PercolationState.BLOCKED, PercolationState.BLOCKED),
        new PercolationCell(1, PercolationState.BLOCKED, PercolationState.BLOCKED),
        new PercolationCell(3, null, PercolationState.BLOCKED),
        new PercolationCell(4, null, PercolationState.BLOCKED)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(PercolationState.BLOCKED, cell.getCurrState());
    assertEquals(PercolationState.BLOCKED, cell.getNextState());
  }

  @Test
  void updateStateForPercolatedCell() {
    cell = new PercolationCell(2, null, PercolationState.PERCOLATED);
    neighbors = new ArrayList<>(List.of(
        new PercolationCell(0, PercolationState.BLOCKED, PercolationState.OPEN),
        new PercolationCell(1, PercolationState.OPEN, PercolationState.BLOCKED),
        new PercolationCell(3, null, PercolationState.PERCOLATED),
        new PercolationCell(4, null, PercolationState.PERCOLATED)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(PercolationState.PERCOLATED, cell.getCurrState());
    assertEquals(PercolationState.PERCOLATED, cell.getNextState());
  }

  @Test
  void updateStateForOpenCellWithoutPercolatedNeighbors() {
    cell = new PercolationCell(2, null, PercolationState.OPEN);
    neighbors = new ArrayList<>(List.of(
        new PercolationCell(0, PercolationState.BLOCKED, PercolationState.OPEN),
        new PercolationCell(1, PercolationState.OPEN, PercolationState.BLOCKED),
        new PercolationCell(3, null, PercolationState.OPEN),
        new PercolationCell(4, null, PercolationState.OPEN)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(PercolationState.OPEN, cell.getCurrState());
    assertEquals(PercolationState.OPEN, cell.getNextState());
  }

  @Test
  void updateStateForOpenCellWithPercolatedNeighbor() {
    cell = new PercolationCell(2, null, PercolationState.OPEN);
    neighbors = new ArrayList<>(List.of(
        new PercolationCell(0, PercolationState.PERCOLATED, PercolationState.PERCOLATED),
        new PercolationCell(1, PercolationState.BLOCKED, PercolationState.BLOCKED),
        new PercolationCell(3, null, PercolationState.OPEN),
        new PercolationCell(4, null, PercolationState.OPEN)
    ));
    ruleset.updateState(cell, neighbors);
    assertEquals(PercolationState.OPEN, cell.getCurrState());
    assertEquals(PercolationState.PERCOLATED, cell.getNextState());
  }

}