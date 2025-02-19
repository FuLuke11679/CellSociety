package cellsociety.model.grid;

import cellsociety.model.cell.SugarscapePatch;
import cellsociety.model.cell.SugarscapeAgent;
import cellsociety.model.cell.Cell;
import cellsociety.model.state.CellState;
import cellsociety.model.state.SugarscapeState;
import cellsociety.model.ruleset.SugarscapeRuleset;

public class SugarscapeGrid extends Grid {
  private SugarscapeRuleset ruleset;

  public SugarscapeGrid(int rows, int columns, SugarscapeRuleset ruleset, String[] initialStates) {
    super(rows, columns, ruleset, initialStates);
    this.ruleset = ruleset;
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
    System.out.println("Test");
    int[] initialValues = getValues();
    for(int i = 0; i< initialValues.length; i++) {
      System.out.print(initialValues[i] + " ");
    }
    // Retrieve sugar value from precomputed sugarValues array
    int sugarValue = initialValues[id]; // Assuming sugarValues was populated in XMLParser

    try {
      // Dynamically create cell type
      Class<?> cellClass = Class.forName("cellsociety.model.cell." + cellType);

      if (cellClass.equals(SugarscapePatch.class)) {
        return (Cell) cellClass.getConstructor(int.class, CellState.class, CellState.class, int.class)
            .newInstance(id, currState, nextState, sugarValue);
      }
      else if (cellClass.equals(SugarscapeAgent.class)) {
        // Default agent parameters (you may modify as needed)
        SugarscapeAgent agent = new SugarscapeAgent(10, 3, 2);
        SugarscapePatch patch = new SugarscapePatch(id, SugarscapeState.PATCH, nextState, sugarValue);
        patch.setAgent(agent);
        return patch;
      }
      else {
        return (Cell) cellClass.getConstructor(int.class, CellState.class, CellState.class)
            .newInstance(id, currState, nextState);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null; // Return a default cell or handle error appropriately
    }
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
