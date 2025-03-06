package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.grid.CellShapeFactory;
import cellsociety.model.grid.EdgeFactory;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.NeighborhoodFactory;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido Testing class for Segregation Simulation
 */

class SegregationRulesetTest {

  private Ruleset myRuleset;
  private Grid myGrid;

  @BeforeEach
  void setUp() {
    String[] initialStates = {"BLU", "R", "EM", "EM", "EM",
        "R", "R", "EM", "EM", "EM",
        "EM", "EM", "EM", "EM", "EM",
        "EM", "EM", "EM", "BLU", "BLU",
        "EM", "EM", "EM", "BLU", "R"};
    myRuleset = new SegregationRuleset(Map.of("thresh", "0.5"));
    myGrid = myRuleset.createGrid(5, 5, initialStates);
    myGrid.setEdgeHandler(EdgeFactory.createEdgeHandler(""));
    myGrid.setNeighborhoodStrategy(NeighborhoodFactory.createNeighborhoodStrategy(""));
    myGrid.setCellShape(CellShapeFactory.createCellShape(""));
  }

  @Test
  void updateGridState_CellIsSatisfied_CellSwapsToEmptySpot() {
    myGrid.update();
    myGrid.printGrid();

    assertEquals(SegregationState.EMPTY, myGrid.getCell(0, 0).getCurrState());
  }

  @Test
  void updateGridState_CellIsNotSatisfied_CellDoesNotSwapToEmptySpot() {
    myGrid.update();
    myGrid.printGrid();

    assertEquals(SegregationState.RED, myGrid.getCell(0, 1).getCurrState());
  }

}