package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.grid.CellShapeFactory;
import cellsociety.model.grid.ConwayGrid;
import cellsociety.model.grid.EdgeFactory;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.NeighborhoodFactory;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido Testing file for General Conway Game of Life
 */
class GeneralConwayRulesetTest {

  Ruleset myRuleset;
  Grid myGrid;

  @BeforeEach
  void setUp() {
    myRuleset = new GeneralConwayRuleset(Map.of("rules", "B2/S"));
    String[] initialStates = {
        "D", "D", "D", "D", "D",
        "D", "A", "D", "D", "D",
        "D", "D", "A", "D", "D",
        "D", "D", "D", "D", "D",
        "D", "D", "D", "D", "D",
    };
    myGrid = new ConwayGrid(5, 5, myRuleset, initialStates);
    myGrid.setEdgeHandler(EdgeFactory.createEdgeHandler(""));
    myGrid.setNeighborhoodStrategy(NeighborhoodFactory.createNeighborhoodStrategy(""));
    myGrid.setCellShape(CellShapeFactory.createCellShape(""));
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
    myRuleset = new GeneralConwayRuleset(Map.of("rules", "/2"));
    myGrid.update();
    assertEquals(ConwayState.DEAD, myGrid.getCell(1, 1).getCurrState());
    assertEquals(ConwayState.ALIVE, myGrid.getCell(1, 2).getCurrState());
    assertEquals(ConwayState.ALIVE, myGrid.getCell(2, 1).getCurrState());
  }

}