package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.grid.ConwayGrid;
import cellsociety.model.grid.Grid;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeneralConwayRulesetTest {

  Ruleset myRuleset;
  Grid myGrid;

  @BeforeEach
  void setUp() {
    myRuleset = new GeneralConwayRuleset("B2/S");
    String[] initialStates = {
        "D","D","D","D","D",
        "D","A","D","D","D",
        "D","D","A","D","D",
        "D","D","D","D","D",
        "D","D","D","D","D",
    };
    myGrid = new ConwayGrid(5, 5, myRuleset, initialStates);
  }

  @Test
  void updateCellState_SeedVariationWithBSRule_CellDiesButBirthsTwo() {
    myGrid.update();
    assertEquals(ConwayState.DEAD, myGrid.getCell(1, 1).getCurrState());
    assertEquals(ConwayState.ALIVE, myGrid.getCell(1, 2).getCurrState());
    assertEquals(ConwayState.ALIVE, myGrid.getCell(2, 1).getCurrState());
  }

  @Test
  void updateCellState_SeedVariationWithSBRule_CellDiesButBirthsTwo() {
    myRuleset = new GeneralConwayRuleset("/2");
    myGrid.update();
    assertEquals(ConwayState.DEAD, myGrid.getCell(1, 1).getCurrState());
    assertEquals(ConwayState.ALIVE, myGrid.getCell(1, 2).getCurrState());
    assertEquals(ConwayState.ALIVE, myGrid.getCell(2, 1).getCurrState());
  }

}