package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SugarscapeCell;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.SegregationGrid;
import cellsociety.model.grid.SugarscapeGrid;
import cellsociety.model.cell.SugarscapePatchCell;
import java.util.List;
import java.util.Map;

public class SugarscapeRuleset extends Ruleset {
  private SugarscapeGrid myGrid;
  Map<Cell, Integer> patches;

  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    myGrid = new SugarscapeGrid(rows, columns, new SugarscapeRuleset(), initialStates);
    patches = getEmptyCells(myGrid);
    return myGrid;
  }

  @Override
  public void updateState(Cell cell, List<Cell> neighbors) {
    if (cell instanceof SugarscapeCell) {
      SugarscapeCell agent = (SugarscapeCell) cell;

      Grid grid = agent.getGrid();

      // Find the best patch to move to
      SugarscapePatchCell bestPatch = findBestPatch(agent, neighbors, grid);
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
      SugarscapePatchCell patch = grid.getPatch(row, col);

      if (!patch.isOccupied() && patch.getSugar() > maxSugar) {
        maxSugar = patch.getSugar();
        bestPatch = patch;
      }
    }

    return bestPatch;
  }
  @Override
  public Grid createGrid(int rows, int columns, String[] initialStates) {
    return new SugarscapeGrid(rows, columns, new SugarscapeRuleset(), initialStates);
  }
}