package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.PercolationCell;
import cellsociety.model.cell.PercolationCell.PercolationState;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido Testing file for Percolation Simulation
 */
class PercolationRulesetTest {

  Cell cell;
  List<Cell> neighbors;
  Ruleset ruleset;

  @BeforeEach
  void setUp() {
    ruleset = new PercolationRuleset();
  }

  @Test
  void updateCellState_CellIsBlocked_CellStaysBlocked() {
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
  void updateCellState_CellIsPercolated_CellStaysPercolated() {
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
  void updateCellState_OpenCellPercolationWithoutPercolatedNeighbors_OpenCellDoesNotGetPercolated() {
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
  void updateCellState_OpenCellPercolationWithPercolatedNeighbors_OpenCellBecomesPercolated() {
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