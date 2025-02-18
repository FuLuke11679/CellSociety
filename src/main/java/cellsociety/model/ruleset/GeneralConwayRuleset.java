package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.parser.GeneralConwayParser;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Daniel Rodriguez-Florido
 * This class is the Ruleset for the General Conway Game of Life
 */
public class GeneralConwayRuleset extends ConwayRuleset {

  Set<Integer> birthVals;
  Set<Integer> survivalVals;

  public GeneralConwayRuleset(String rules) {
    birthVals = new HashSet<>();
    survivalVals = new HashSet<>();
    GeneralConwayParser.parseRuleString(rules, birthVals, survivalVals);
  }

  /**
   *
   * @param cell
   * @param neighbors
   */
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

  /**
   *
   */
  @Override
  public void updateGridState() {
  }

}
