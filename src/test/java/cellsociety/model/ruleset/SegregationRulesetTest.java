package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.grid.CellShapeFactory;
import cellsociety.model.grid.EdgeFactory;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.NeighborhoodFactory;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido
 * Testing class for Segregation Simulation
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
    myRuleset = new SegregationRuleset(Map.of("thresh", "0.4"));
    myGrid = myRuleset.createGrid(5, 5, initialStates);
    setGridParams(myGrid);
  }

  void setGridParams(Grid grid) {
    grid.setEdgeHandler(EdgeFactory.createEdgeHandler("Toroidal"));
    grid.setNeighborhoodStrategy(NeighborhoodFactory.createNeighborhoodStrategy("VonNeumann"));
    grid.setCellShape(CellShapeFactory.createCellShape("Rectangular"));
  }

  @Test
  void updateGridState_CellIsSatisfied_CellSwapsToEmptySpot() {
    myGrid.update();
    assertEquals(SegregationState.EMPTY, myGrid.getCell(0, 0).getCurrState());
  }

  @Test
  void updateGridState_CellIsSatisfied_CellDoesNotSwapToEmptySpot() {
    myGrid.update();
    myGrid.printGrid();
    assertEquals(SegregationState.RED, myGrid.getCell(0, 1).getCurrState());
  }

  @Test
  void updateGridState_NoEmptyCells_UnsatisfiedCellsRemainInPlace() {
    String[] initialStates = {
        "BLU", "R", "BLU", "R", "BLU",
        "R", "BLU", "R", "BLU", "R",
        "BLU", "R", "BLU", "R", "BLU",
        "R", "BLU", "R", "BLU", "R",
        "BLU", "R", "BLU", "R", "BLU"
    };
    Ruleset rulesetNoEmpty = new SegregationRuleset(Map.of("thresh", "0.5"));
    Grid gridNoEmpty = rulesetNoEmpty.createGrid(5, 5, initialStates);
    setGridParams(gridNoEmpty);

    // Capture initial state
    String[] beforeUpdate = new String[25];
    for (int i = 0; i < 25; i++) {
      beforeUpdate[i] = gridNoEmpty.getCell(i / 5, i % 5).getCurrState().toString();
    }

    gridNoEmpty.update();

    for (int i = 0; i < 25; i++) {
      assertEquals(beforeUpdate[i], gridNoEmpty.getCell(i / 5, i % 5).getCurrState().toString());
    }
  }

  @Test
  void updateGridState_SingleCellGrid_BehavesCorrectly() {
    String[] initialStates = {"R"};
    Ruleset singleCellRuleset = new SegregationRuleset(Map.of("thresh", "0.5"));
    Grid singleCellGrid = singleCellRuleset.createGrid(1, 1, initialStates);
    setGridParams(singleCellGrid);

    singleCellGrid.update();

    assertEquals("RED", singleCellGrid.getCell(0, 0).getCurrState().toString());
  }

  @Test
  void updateGridState_AllEmptyGrid_NoSwapsOccur() {
    String[] initialStates = {
        "EM", "EM", "EM",
        "EM", "EM", "EM",
        "EM", "EM", "EM"
    };
    Ruleset rulesetAllEmpty = new SegregationRuleset(Map.of("thresh", "0.5"));
    Grid allEmptyGrid = rulesetAllEmpty.createGrid(3, 3, initialStates);
    setGridParams(allEmptyGrid);
    allEmptyGrid.update();
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        assertEquals("EMPTY", allEmptyGrid.getCell(i, j).getCurrState().toString());
      }
    }
  }

  @Test
  void updateGridState_ThresholdOne_AllUnsatisfiedCellsSwap() {
    String[] initialStates = {
        "R", "R", "EM",
        "R", "BLU", "EM",
        "EM", "EM", "EM"
    };
    Ruleset rulesetThresholdOne = new SegregationRuleset(Map.of("thresh", "1.0"));
    Grid gridThresholdOne = rulesetThresholdOne.createGrid(3, 3, initialStates);
    setGridParams(gridThresholdOne);

    gridThresholdOne.update();

    assertNotEquals("BLUE", gridThresholdOne.getCell(1, 1).getCurrState().toString());
  }

}