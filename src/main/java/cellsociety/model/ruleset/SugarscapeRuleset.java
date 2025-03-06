package cellsociety.model.ruleset;

import cellsociety.model.cell.SugarscapePatch;
import cellsociety.model.agent.SugarscapeAgent;
import cellsociety.model.cell.Cell;
import cellsociety.model.grid.SugarscapeGrid;
import cellsociety.model.state.CellState;
import cellsociety.model.state.SugarscapeState;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Defines the ruleset for the Sugarscape simulation.
 * Handles agent movement, sugar growth, and other Sugarscape-specific logic.
 * @author Luke
 */
public class SugarscapeRuleset extends Ruleset {

  private static final String SUGAR_GROW_RATE_PARAM_NAME = "sugarGrowBackRate";
  private static final String SUGAR_GROW_BACK_PARAM_NAME = "sugarGrowBackInterval";
  private static final String AGENT_VISION_PARAM_NAME = "agentVision";
  private static final String AGENT_METABOLISM_PARAM_NAME = "agentMetabolism";

  private SugarscapeGrid myGrid;
  private final int sugarGrowBackRate;
  private final int sugarGrowBackInterval;
  private final int agentVision;
  private final int agentMetabolism;
  private int tickCounter = 0;
  private int[] initialValues;

  /**
   * Constructs a new SugarscapeRuleset based on the given parameters.
   *
   * @param params A map containing relevant simulation parameters such as sugar growth rate and agent vision.
   */
  public SugarscapeRuleset(Map<String, String> params) {
    this.sugarGrowBackRate = Integer.parseInt(params.get(SUGAR_GROW_RATE_PARAM_NAME));
    this.sugarGrowBackInterval = Integer.parseInt(params.get(SUGAR_GROW_BACK_PARAM_NAME));
    this.agentVision = Integer.parseInt(params.get(AGENT_VISION_PARAM_NAME));
    this.agentMetabolism = Integer.parseInt(params.get(AGENT_METABOLISM_PARAM_NAME));
  }

  /**
   * Sets the initial values for sugar levels in each patch.
   *
   * @param initialValues An array of initial sugar levels for each cell.
   */
  public void setInitialValues(int[] initialValues) {
    this.initialValues = initialValues;
  }

  /**
   * Updates the grid state by growing sugar and moving agents.
   */
  @Override
  public void updateGridState() {
    tickCounter++;
    if (tickCounter >= sugarGrowBackInterval) {
      growSugar();
      tickCounter = 0;
    }
    moveAgents();
  }

  /**
   * Increases sugar levels in patches at the defined growth rate.
   */
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

  /**
   * Moves agents across the grid based on their vision and metabolism.
   */
  private void moveAgents() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        Cell cell = myGrid.getCell(i, j);
        if (cell instanceof SugarscapePatch) {
          SugarscapePatch patch = (SugarscapePatch) cell;
          if (patch.hasAgent()) {
            SugarscapeAgent agent = patch.getAgent();
            List<SugarscapePatch> visionPatches = myGrid.getNeighbors(i, j).stream()
                .filter(c -> c instanceof SugarscapePatch)
                .map(c -> (SugarscapePatch) c)
                .collect(Collectors.toList());
            agent.move(visionPatches, patch, agentVision, agentMetabolism);
          }
        }
      }
    }
    resetAgentMoves();
  }

  /**
   * Resets agent movement status after each step.
   */
  private void resetAgentMoves() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        Cell cell = myGrid.getCell(i, j);
        SugarscapePatch patch = (SugarscapePatch) cell;
        if (patch.hasAgent()) {
          patch.getAgent().resetMovement();
        }
      }
    }
  }

  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {}

  /**
   * Creates a new SugarscapeGrid and initializes agent placements and sugar levels.
   *
   * @param rows          The number of rows in the grid.
   * @param columns       The number of columns in the grid.
   * @param initialStates An array representing the initial states of each cell.
   * @return The created SugarscapeGrid.
   */
  @Override
  public SugarscapeGrid createGrid(int rows, int columns, String[] initialStates) {
    myGrid = new SugarscapeGrid(rows, columns, this, initialStates);
    if (initialValues != null) {
      int count = 0;
      for (int i = 0; i < myGrid.getRows(); i++) {
        for (int j = 0; j < myGrid.getColumns(); j++) {
          Cell cell = myGrid.getCell(i, j);
          ((SugarscapePatch) cell).setSugarAmount(initialValues[count]);
          count++;
        }
      }
    }
    initializeAgents();
    return myGrid;
  }

  /**
   * Places agents in the grid based on the initial configuration.
   */
  private void initializeAgents() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getColumns(); j++) {
        SugarscapePatch patch = (SugarscapePatch) myGrid.getCell(i, j);
        if (patch.getCurrState().toString().equals("AGENT")) {
          int index = i * myGrid.getColumns() + j;
          int initSugar = (initialValues != null && index < initialValues.length) ? initialValues[index] : 0;
          patch.setAgent(new SugarscapeAgent(initSugar));
        }
      }
    }
  }

  /**
   * Gets the initial sugar values for the grid.
   *
   * @return An array of initial sugar values.
   */
  public int[] getInitialValues() {
    return initialValues;
  }

  /**
   * Returns the default state for a cell in the Sugarscape simulation.
   *
   * @return The default cell state (PATCH).
   */
  @Override
  public CellState getDefaultCellState() {
    return SugarscapeState.PATCH;
  }
}
