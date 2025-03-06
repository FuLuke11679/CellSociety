package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.state.CellState;
import cellsociety.parser.GeneralConwayParser;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: Daniel Rodriguez-Florido
 * This class is the Ruleset for the General Conway Game of Life
 */
public class GeneralConwayRuleset extends ConwayRuleset {

  private static final String RULE_PARAM_NAME = "rules";

  private final Set<Integer> birthVals;
  private final Set<Integer> survivalVals;

  /**
   * Constructor for the General Conway Game of Life Ruleset
   * @param params Map of relevant parameters to ruleset
   *               (rules)
   */
  public GeneralConwayRuleset(Map<String, String> params) {
    birthVals = new HashSet<>();
    survivalVals = new HashSet<>();
    String rules = params.getOrDefault(RULE_PARAM_NAME, "B3/S23");
    GeneralConwayParser.parseRuleString(rules, birthVals, survivalVals);
  }

  @Override
  public void updateCellState(Cell cell, List<Cell> neighbors) {
    int aliveCells = countAliveNeighbors(neighbors);
    if (cell.getCurrState() == ConwayState.DEAD && birthVals.contains(aliveCells)) {
      birthCell(cell);
    } else if (cell.getCurrState() == ConwayState.ALIVE && survivalVals.contains(aliveCells)) {
      maintainCell(cell);
    } else {
      killCell(cell);
    }
  }

  @Override
  public CellState getDefaultCellState() {
    return ConwayState.DEAD;
  }


}
