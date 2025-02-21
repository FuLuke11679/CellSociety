package cellsociety.model.grid;

import cellsociety.model.cell.SugarscapePatch;
import cellsociety.model.cell.SugarscapeAgent;
import cellsociety.model.cell.Cell;
import cellsociety.model.ruleset.Ruleset;
import cellsociety.model.state.CellState;
import cellsociety.model.state.SugarscapeState;
import cellsociety.model.ruleset.SugarscapeRuleset;

public class SugarscapeGrid extends Grid {

  public SugarscapeGrid(int rows, int columns, SugarscapeRuleset ruleset, String[] initialStates) {
    super(rows, columns, ruleset, initialStates);
  }

  // Helper to determine if a patch at (row, col) has an agent.
  public boolean hasAgentAt(int row, int col) {
    Cell cell = getCell(row, col);
    if (cell instanceof SugarscapePatch) {
      return ((SugarscapePatch) cell).hasAgent();
    }
    return false;
  }
  @Override
  public Cell createCell(int id, CellState currState, CellState nextState, String cellType) {
    // Retrieve sugar value from the ruleset’s initial values.
    int[] values = ((SugarscapeRuleset)getRuleset()).getInitialValues();
    int sugarValue = values[id];
    try {
      // Always create a SugarscapePatch
      SugarscapePatch patch = new SugarscapePatch(id, currState, nextState, sugarValue);
      // If the initial state indicates an agent, attach a default agent.
      if (currState.equals(cellsociety.model.state.SugarscapeState.AGENT)) {
        SugarscapeAgent agent = new SugarscapeAgent(10, 1, 2); // or use parameters from XML
        patch.setAgent(agent);
      }
      return patch;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void update() {
    // Perform the grid-level update defined in SugarscapeRuleset
    getRuleset().updateGridState();
    // Then move the next states to current states.
    moveNextStateToCurrent();
  }

  // Moves an agent from one patch to another.
  public void moveAgent(int oldRow, int oldCol, int newRow, int newCol) {
    SugarscapePatch oldPatch = (SugarscapePatch) getCell(oldRow, oldCol);
    SugarscapeAgent agent = oldPatch.getAgent();
    // Preserve the old patch's sugar (if desired) by keeping its sugarAmount.
    oldPatch.removeAgent();
    SugarscapePatch newPatch = (SugarscapePatch) getCell(newRow, newCol);
    newPatch.setAgent(agent);
  }

  public boolean isValidPosition(int row, int col) {
    return row >= 0 && row < getRows() && col >= 0 && col < getColumns();
  }

}
