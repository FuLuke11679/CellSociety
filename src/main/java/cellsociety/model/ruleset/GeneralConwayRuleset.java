package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.parser.GeneralConwayParser;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GeneralConwayRuleset extends ConwayRuleset {

  Set<Integer> birthVals;
  Set<Integer> survivalVals;

  public GeneralConwayRuleset(String rules) {
    birthVals = new HashSet<>();
    survivalVals = new HashSet<>();
    GeneralConwayParser.parseRuleString(rules, birthVals, survivalVals);
    System.out.println("Birth Vals: ");
    for (Integer birthVal : birthVals) {
      System.out.print(birthVal + " ");
    }
    System.out.println();
    System.out.println("Survival Vals: ");
    for (Integer survivalVal : survivalVals) {
      System.out.print(survivalVal + " ");
    }
    System.out.println();
  }

  /**
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
