package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SugarscapeCell;
import cellsociety.model.cell.SugarscapePatchCell;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.SegregationGrid;
import cellsociety.model.grid.SugarscapeGrid;
import java.util.List;

public class SugarscapeRuleset extends Ruleset {
  private SugarscapeGrid myGrid;

  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {
    if (cell instanceof SugarscapeCell) {
      SugarscapeCell agent = (SugarscapeCell) cell;

      // Find the best patch to move to
      SugarscapePatchCell bestPatch = findBestPatch(agent, neighbors, myGrid);
      if (bestPatch != null) {
        agent.moveToPatch(bestPatch);
      }

      // Subtract metabolism
      agent.setSugar(agent.getSugar() - agent.getSugarMetabolism());

      // Check if agent dies
      if (!agent.isAlive()) {
        agent.setNextState(SugarscapeCell.SugarscapeState.EMPTY);
      }
    }
  }

  private SugarscapePatchCell findBestPatch(SugarscapeCell agent, List<Cell> neighbors, Grid grid) {
    SugarscapePatchCell bestPatch = null;
    int maxSugar = -1;

    for (Cell neighbor : neighbors) {
      int row = neighbor.getId() / grid.getColumns();
      int col = neighbor.getId() % grid.getColumns();
      SugarscapePatchCell patch = ((SugarscapeGrid) grid).getPatch(row, col);

      if (!patch.isOccupied() && patch.getSugar() > maxSugar) {
        maxSugar = patch.getSugar();
        bestPatch = patch;
      }
    }

    return bestPatch;
  }

  @Override
  public void updateGridState() {
    // Update patches (e.g., sugar regrowth)
    myGrid.updatePatches();
  }

  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    myGrid = new SugarscapeGrid(rows, columns, this, initialStates);
    return myGrid;
  }
}