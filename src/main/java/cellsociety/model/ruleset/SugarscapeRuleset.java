package cellsociety.model.ruleset;

import cellsociety.model.grid.SugarscapeGrid;
import cellsociety.model.cell.SugarscapePatch;
import cellsociety.model.cell.SugarscapeAgent;
import cellsociety.model.cell.Cell;
import java.util.List;
import java.util.ArrayList;

public class SugarscapeRuleset extends Ruleset {
  private SugarscapeGrid myGrid;
  private int sugarGrowBackRate;
  private int sugarGrowBackInterval;
  private int tickCounter = 0;

  public SugarscapeRuleset(int sugarGrowBackRate, int sugarGrowBackInterval) {
    this.sugarGrowBackRate = sugarGrowBackRate;
    this.sugarGrowBackInterval = sugarGrowBackInterval;
  }

  @Override
  public void updateGridState() {
    tickCounter++;
    if (tickCounter >= sugarGrowBackInterval) {
      growSugar();
      tickCounter = 0;
    }
    moveAgents();
  }

  private void growSugar() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        Cell cell = myGrid.getCell(i, j);
        if (cell instanceof SugarscapePatch) {
          ((SugarscapePatch) cell).growSugar(sugarGrowBackRate);
        }
      }
    }
  }

  private void moveAgents() {
    // Loop through all patches; if a patch contains an agent, try to move it.
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        Cell cell = myGrid.getCell(i, j);
        if (cell instanceof SugarscapePatch) {
          SugarscapePatch patch = (SugarscapePatch) cell;
          if (patch.hasAgent()) {
            moveAgent(i, j, patch.getAgent());
          }
        }
      }
    }
  }

  private void moveAgent(int row, int col, SugarscapeAgent agent) {
    int vision = agent.getVision();
    SugarscapePatch bestPatch = findBestPatch(row, col, vision);
    if (bestPatch != null) {
      // Agent collects sugar from the best patch.
      agent.collectSugar(bestPatch.getSugarAmount());
      bestPatch.harvestSugar();
      // Move the agent from the old patch to the best patch.
      int newRow = bestPatch.getId() / myGrid.getRows();
      int newCol = bestPatch.getId() % myGrid.getRows();
      myGrid.moveAgent(row, col, newRow, newCol);
      // Agent consumes sugar due to metabolism.
      agent.consumeSugar();
      if (agent.getAgentSugar() <= 0) {
        // Agent dies; remove it from the patch.
        SugarscapePatch newPatch = (SugarscapePatch) myGrid.getCell(newRow, newCol);
        newPatch.removeAgent();
      }
    }
  }

  private SugarscapePatch findBestPatch(int row, int col, int vision) {
    List<SugarscapePatch> candidates = getVisionPatches(row, col, vision);
    SugarscapePatch best = null;
    int maxSugar = -1;
    int minDistance = Integer.MAX_VALUE;
    for (SugarscapePatch patch : candidates) {
      int patchRow = patch.getId() / myGrid.getRows();
      int patchCol = patch.getId() % myGrid.getRows();
      if (!patch.hasAgent() && patch.getSugarAmount() > maxSugar) {
        best = patch;
        maxSugar = patch.getSugarAmount();
        minDistance = getDistance(row, col, patchRow, patchCol);
      } else if (!patch.hasAgent() && patch.getSugarAmount() == maxSugar) {
        int dist = getDistance(row, col, patchRow, patchCol);
        if (dist < minDistance) {
          best = patch;
          minDistance = dist;
        }
      }
    }
    return best;
  }

  private List<SugarscapePatch> getVisionPatches(int row, int col, int vision) {
    List<SugarscapePatch> patches = new ArrayList<>();
    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};
    for (int i = 0; i < 4; i++) {
      for (int v = 1; v <= vision; v++) {
        int newRow = row + v * dx[i];
        int newCol = col + v * dy[i];
        if (myGrid.isValidPosition(newRow, newCol)) {
          Cell cell = myGrid.getCell(newRow, newCol);
          if (cell instanceof SugarscapePatch) {
            patches.add((SugarscapePatch) cell);
          }
        }
      }
    }
    return patches;
  }

  private int getDistance(int row1, int col1, int row2, int col2) {
    return Math.abs(row1 - row2) + Math.abs(col1 - col2);
  }

  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {
    // Unused for Sugarscape.
  }

  @Override
  public SugarscapeGrid createGrid(int rows, int columns, String[] initialStates) {
    myGrid = new SugarscapeGrid(rows, columns, this, initialStates);
    return myGrid;
  }
}
