//package cellsociety.model.ruleset;
//
//import cellsociety.model.cell.ConwayCell.ConwayState;
//import cellsociety.model.cell.SugarscapePatch;
//import cellsociety.model.agent.SugarscapeAgent;
//import cellsociety.model.cell.Cell;
//import cellsociety.model.state.CellState;
//import cellsociety.model.state.SugarscapeState;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class SugarscapeRuleset extends Ruleset {
//
//  private static final String SUGAR_GROW_RATE_PARAM_NAME = "sugarGrowBackRate";
//  private static final String SUGAR_GROW_BACK_PARAM_NAME = "sugarGrowBackInterval";
//  private static final String AGENT_VISION_PARAM_NAME = "agentVision";
//  private static final String AGENT_METABOLISM_PARAM_NAME = "agentMetabolism";
//
//  private SugarscapeGrid myGrid;
//  private final int sugarGrowBackRate;
//  private final int sugarGrowBackInterval;
//  private final int agentVision;
//  private final int agentMetabolism;
//  private int tickCounter = 0;
//  private int[] initialValues;
//
//  public SugarscapeRuleset(Map<String, String> params) {
//    this.sugarGrowBackRate = Integer.parseInt(params.get(SUGAR_GROW_RATE_PARAM_NAME));
//    this.sugarGrowBackInterval = Integer.parseInt(params.get(SUGAR_GROW_BACK_PARAM_NAME));
//    this.agentVision = Integer.parseInt(params.get(AGENT_VISION_PARAM_NAME));
//    this.agentMetabolism = Integer.parseInt(params.get(AGENT_METABOLISM_PARAM_NAME));
//  }
//
//  public void setInitialValues(int[] initialValues) {
//    this.initialValues = initialValues;
//  }
//
//  @Override
//  public void updateGridState() {
//    tickCounter++;
//    if (tickCounter >= sugarGrowBackInterval) {
//      growSugar();
//      tickCounter = 0;
//    }
//    moveAgents();
//  }
//
//  private void growSugar() {
//    for (int i = 0; i < myGrid.getRows(); i++) {
//      for (int j = 0; j < myGrid.getColumns(); j++) {
//        Cell cell = myGrid.getCell(i, j);
//        if (cell instanceof SugarscapePatch) {
//          ((SugarscapePatch) cell).growSugar(sugarGrowBackRate);
//        }
//      }
//    }
//  }
//
//  private void moveAgents() {
//    for (int i = 0; i < myGrid.getRows(); i++) {
//      for (int j = 0; j < myGrid.getColumns(); j++) {
//        Cell cell = myGrid.getCell(i, j);
//        if (cell instanceof SugarscapePatch) {
//          SugarscapePatch patch = (SugarscapePatch) cell;
//          if (patch.hasAgent()) {
//            SugarscapeAgent agent = patch.getAgent();
//            List<SugarscapePatch> visionPatches = myGrid.getNeighbors(i, j).stream()
//                .filter(c -> c instanceof SugarscapePatch)
//                .map(c -> (SugarscapePatch) c)
//                .collect(Collectors.toList());
//            agent.move(visionPatches, patch, agentVision, agentMetabolism);
//          }
//        }
//      }
//    }
//    resetAgentMoves();
//  }
//
//  private void resetAgentMoves() {
//    for (int i = 0; i < myGrid.getRows(); i++) {
//      for (int j = 0; j < myGrid.getColumns(); j++) {
//        Cell cell = myGrid.getCell(i, j);
//        if (cell instanceof SugarscapePatch) {
//          SugarscapePatch patch = (SugarscapePatch) cell;
//          if (patch.hasAgent()) {
//            patch.getAgent().resetMovement();
//          }
//        }
//      }
//    }
//  }
//
//  @Override
//  public void updateCellState(Cell cell, List<Cell> neighbors) {}
//
//  @Override
//  public SugarscapeGrid createGrid(int rows, int columns, String[] initialStates) {
//    myGrid = new SugarscapeGrid(rows, columns, this, initialStates);
//    return myGrid;
//  }
//
//  public int[] getInitialValues() {
//    return initialValues;
//  }
//
//  @Override
//  public CellState getDefaultCellState() {
//    return SugarscapeState.PATCH;
//  }
//
//}
