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
  void updateCellStateForBlockedCell() {
    cell = new PercolationCell(2, PercolationState.BLOCKED, null);
    neighbors = new ArrayList<>(List.of(
        new PercolationCell(0, PercolationState.BLOCKED, null),
        new PercolationCell(1, PercolationState.BLOCKED, null),
        new PercolationCell(3, PercolationState.BLOCKED, null),
        new PercolationCell(4, PercolationState.BLOCKED, null)
    ));
    ruleset.updateCellState(cell, neighbors);
    assertEquals(PercolationState.BLOCKED, cell.getCurrState());
    assertEquals(PercolationState.BLOCKED, cell.getNextState());
  }

  @Test
  void updateCellStateForPercolatedCell() {
    cell = new PercolationCell(2, PercolationState.PERCOLATED, null);
    neighbors = new ArrayList<>(List.of(
        new PercolationCell(0, PercolationState.OPEN, null),
        new PercolationCell(1, PercolationState.BLOCKED, null),
        new PercolationCell(3, PercolationState.PERCOLATED, null),
        new PercolationCell(4, PercolationState.PERCOLATED, null)
    ));
    ruleset.updateCellState(cell, neighbors);
    assertEquals(PercolationState.PERCOLATED, cell.getCurrState());
    assertEquals(PercolationState.PERCOLATED, cell.getNextState());
  }

  @Test
  void updateCellStateForOpenCellWithoutPercolatedNeighbors() {
    cell = new PercolationCell(2, PercolationState.OPEN, null);
    neighbors = new ArrayList<>(List.of(
        new PercolationCell(0, PercolationState.OPEN, null),
        new PercolationCell(1, PercolationState.BLOCKED, null),
        new PercolationCell(3, PercolationState.OPEN, null),
        new PercolationCell(4, PercolationState.OPEN, null)
    ));
    ruleset.updateCellState(cell, neighbors);
    assertEquals(PercolationState.OPEN, cell.getCurrState());
    assertEquals(PercolationState.OPEN, cell.getNextState());
  }

  @Test
  void updateCellStateForOpenCellWithPercolatedNeighbor() {
    cell = new PercolationCell(2, PercolationState.OPEN, null);
    neighbors = new ArrayList<>(List.of(
        new PercolationCell(0, PercolationState.PERCOLATED, null),
        new PercolationCell(1, PercolationState.BLOCKED, null),
        new PercolationCell(3, PercolationState.OPEN, null),
        new PercolationCell(4, PercolationState.OPEN, null)
    ));
    ruleset.updateCellState(cell, neighbors);
    assertEquals(PercolationState.OPEN, cell.getCurrState());
    assertEquals(PercolationState.PERCOLATED, cell.getNextState());
  }

}