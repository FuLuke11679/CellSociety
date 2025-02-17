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
  void updateStateWhenNeedsToSwap() {
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
  void updateStateWhenDoesntNeedsToSwap() {
    List<Cell> neighbors = List.of(
        myGrid.getCell(0, 0),
        myGrid.getCell(1, 0),
        myGrid.getCell(1, 1),
        myGrid.getCell(0, 2),
        myGrid.getCell(1, 2)
    );

    myRuleset.updateState(myGrid.getCell(0, 1), neighbors);

    assertEquals(SegregationState.RED, myGrid.getCell(0, 1).getCurrState());
    assertEquals(SegregationState.RED, myGrid.getCell(0, 1).getNextState());
  }

  @Test
  void updateStateWhenCellIsEmpty() {
    List<Cell> neighbors = List.of(
        myGrid.getCell(0, 1),
        myGrid.getCell(1, 1),
        myGrid.getCell(1, 2),
        myGrid.getCell(0, 3),
        myGrid.getCell(1, 3)
    );

    myRuleset.updateState(myGrid.getCell(0, 2), neighbors);

    assertEquals(SegregationState.EMPTY, myGrid.getCell(0, 2).getCurrState());
    assertEquals(SegregationState.EMPTY, myGrid.getCell(0, 2).getNextState());
  }

}