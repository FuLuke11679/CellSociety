package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.SegregationCell;
import cellsociety.model.cell.SegregationCell.SegregationState;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.SegregationGrid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    myRuleset.updateState(myGrid.getCell(0, 0), neighbors);

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

    assertEquals(SegregationState.RED, myGrid.getCell(0, 1).getPrevState());
    assertEquals(SegregationState.RED, myGrid.getCell(0, 1).getCurrState());
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

    assertEquals(SegregationState.EMPTY, myGrid.getCell(0, 2).getPrevState());
    assertEquals(SegregationState.EMPTY, myGrid.getCell(0, 2).getCurrState());
  }

}