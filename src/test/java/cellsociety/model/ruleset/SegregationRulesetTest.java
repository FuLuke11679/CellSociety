package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.grid.Grid;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    double similarityThreshold = 0.5;
    myRuleset = new SegregationRuleset(similarityThreshold);
    myGrid = myRuleset.createGrid(5, 5, initialStates);
  }

  @Test
  void updateCellStateWhenNeedsToSwap() {
    List<Cell> neighbors = List.of(
        myGrid.getCell(0, 1),
        myGrid.getCell(1, 0),
        myGrid.getCell(1, 1)
    );

    myGrid.update();
//    myRuleset.updateState(myGrid.getCell(0, 0), neighbors);
    myGrid.printGrid();

    assertEquals(SegregationState.EMPTY, myGrid.getCell(0, 0).getCurrState());
  }

  @Test
  void updateCellStateWhenDoesntNeedsToSwap() {
    myGrid.update();
    myGrid.printGrid();

    assertEquals(SegregationState.RED, myGrid.getCell(0, 1).getCurrState());
  }

}