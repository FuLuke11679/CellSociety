package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.FireCell;
import cellsociety.model.cell.FireCell.FireState;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido
 * Testing file for Forest Fire Simulation
 */
class FireRulesetTest {

  private Cell emptyCell;
  private Cell burningCell;
  private Cell treeCell;

  private List<Cell> neighbors;

  private final Map<String, String> minProbMap = Map.of(
      "probGrow", "0",
      "probCatch", "0"
  );

  private final Map<String, String> maxProbMap = Map.of(
      "probGrow", "1",
      "probCatch", "1"
  );

  private final Map<String, String> garbageProbMap = Map.of(
      "probGrow", "123",
      "probCatch", "-125235"
  );

  private Ruleset rulesetMinProb;
  private Ruleset rulesetMaxProb;
  private Ruleset rulesetGarbageProb;

  @BeforeEach
  void setUp() {
    emptyCell = new FireCell(2, FireState.EMPTY, null);
    burningCell = new FireCell(2, FireState.BURNING, null);
    treeCell = new FireCell(2, FireState.TREE, null);
    rulesetMinProb = new FireRuleset(minProbMap);
    rulesetMaxProb = new FireRuleset(maxProbMap);
    rulesetGarbageProb = new FireRuleset(garbageProbMap);
  }

  @Test
  void updateCellState_EmptyCellFireCatching_EmptyCellDoesNotCatchFire() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.TREE, null),
        new FireCell(1, FireState.TREE, null),
        new FireCell(3, FireState.TREE, null),
        new FireCell(4, FireState.TREE, null)
    ));
    rulesetMinProb.updateCellState(emptyCell, neighbors);
    assertEquals(FireState.EMPTY, emptyCell.getNextState());
  }

  @Test
  void updateCellState_EmptyCellTreeGrowth_EmptyCellGrows() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.TREE, null),
        new FireCell(1, FireState.TREE, null),
        new FireCell(3, FireState.TREE, null),
        new FireCell(4, FireState.TREE, null)
    ));
    rulesetMaxProb.updateCellState(emptyCell, neighbors);
    assertEquals(FireState.TREE, emptyCell.getNextState());
  }

  @Test
  void updateCellState_TreeCellFireCatching_TreeCellDoesNotCatchFire() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.TREE, null),
        new FireCell(1, FireState.TREE, null),
        new FireCell(3, FireState.TREE, null),
        new FireCell(4, FireState.TREE, null)
    ));
    rulesetMinProb.updateCellState(treeCell, neighbors);
    assertEquals(FireState.TREE, treeCell.getNextState());
  }

  @Test
  void updateCellState_TreeCellRandomFireCatching_TreeCellCatchesFire() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.TREE, null),
        new FireCell(1, FireState.TREE, null),
        new FireCell(3, FireState.TREE, null),
        new FireCell(4, FireState.TREE, null)
    ));
    rulesetMaxProb.updateCellState(treeCell, neighbors);
    assertEquals(FireState.BURNING, treeCell.getNextState());
  }

  @Test
  void updateCellState_TreeCellFireCatchingWithBurningNeighbor_TreeCatchesFire() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.BURNING, null),
        new FireCell(1, FireState.TREE, null),
        new FireCell(3, FireState.TREE, null)
    ));
    rulesetMaxProb.updateCellState(treeCell, neighbors);
    assertEquals(FireState.BURNING, treeCell.getNextState());
  }

  @Test
  void updateCellState_TreeCellFireCatchingWithMultipleBurningNeighbor_TreeCatchesFire() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.BURNING, null),
        new FireCell(1, FireState.BURNING, null),
        new FireCell(3, FireState.BURNING, null)
    ));
    rulesetMaxProb.updateCellState(treeCell, neighbors);
    assertEquals(FireState.BURNING, treeCell.getNextState());
  }

  @Test
  void updateCell_BurningCell_BurningCellBecomesEmpty() {
    rulesetMaxProb.updateCellState(burningCell, neighbors);
    assertEquals(FireState.EMPTY, burningCell.getNextState());
  }

  @Test
  void updateCell_InvalidProbabilities_swapsToDefaultVals() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.BURNING, null),
        new FireCell(1, FireState.BURNING, null),
        new FireCell(3, FireState.BURNING, null)
    ));
    assertDoesNotThrow(() -> rulesetGarbageProb.updateCellState(emptyCell, neighbors));
  }

  @Test
  void updateCell_NullNeighbors_DoesNotFail() {
    neighbors = null;
    assertDoesNotThrow(() -> rulesetMinProb.updateCellState(emptyCell, neighbors));
  }

}