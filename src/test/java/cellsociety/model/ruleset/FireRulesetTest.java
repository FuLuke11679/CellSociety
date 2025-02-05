package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.FireCell;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.ruleset.FireRuleset;
import cellsociety.model.ruleset.Ruleset;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FireRulesetTest {

  private static final Color TREE_COLOR = Color.GREEN;
  private static final Color BURNING_COLOR = Color.BROWN;
  private static final Color EMPTY_COLOR = Color.YELLOW;

  private Cell emptyCell;
  private Cell burningCell;
  private Cell treeCell;

  List<Cell> neighbors;

  Ruleset rulesetMinProb = new FireRuleset(0, 0);
  Ruleset rulesetMaxProb = new FireRuleset(1, 1);

  @BeforeEach
  void setUp() {
    emptyCell = new FireCell(2, FireState.EMPTY, FireState.EMPTY);
    burningCell = new FireCell(2, FireState.BURNING, FireState.BURNING);
    treeCell = new FireCell(2, FireState.TREE, FireState.TREE);
  }

  @Test
  void updateStateEmptyCellMinProb() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.TREE, FireState.TREE),
        new FireCell(1, FireState.TREE, FireState.TREE),
        new FireCell(3, FireState.TREE, FireState.TREE),
        new FireCell(4, FireState.TREE, FireState.TREE)
    ));
    rulesetMinProb.updateState(emptyCell, neighbors);
    assertEquals(FireState.EMPTY, emptyCell.getCurrState());
    assertEquals(EMPTY_COLOR, emptyCell.getColor());
  }

  @Test
  void updateStateEmptyCellMaxProb() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.TREE, FireState.TREE),
        new FireCell(1, FireState.TREE, FireState.TREE),
        new FireCell(3, FireState.TREE, FireState.TREE),
        new FireCell(4, FireState.TREE, FireState.TREE)
    ));
    rulesetMaxProb.updateState(emptyCell, neighbors);
    assertEquals(FireState.TREE, emptyCell.getCurrState());
    assertEquals(TREE_COLOR, emptyCell.getColor());
  }

  @Test
  void updateStateTreeCellWithoutBurningNeighborMinProb() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.TREE, FireState.TREE),
        new FireCell(1, FireState.TREE, FireState.TREE),
        new FireCell(3, FireState.TREE, FireState.TREE),
        new FireCell(4, FireState.TREE, FireState.TREE)
    ));
    rulesetMinProb.updateState(treeCell, neighbors);
    assertEquals(FireState.TREE, treeCell.getCurrState());
    assertEquals(TREE_COLOR, treeCell.getColor());
  }

  @Test
  void updateStateTreeCellWithoutBurningNeighborMaxProb() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.TREE, FireState.TREE),
        new FireCell(1, FireState.TREE, FireState.TREE),
        new FireCell(3, FireState.TREE, FireState.TREE),
        new FireCell(4, FireState.TREE, FireState.TREE)
    ));
    rulesetMaxProb.updateState(treeCell, neighbors);
    assertEquals(FireState.BURNING, treeCell.getCurrState());
    assertEquals(BURNING_COLOR, treeCell.getColor());
  }

  @Test
  void updateStateTreeCellWithBurningNeighbor() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.BURNING, FireState.BURNING),
        new FireCell(1, FireState.TREE, FireState.TREE),
        new FireCell(3, FireState.TREE, FireState.TREE)
    ));
    rulesetMaxProb.updateState(treeCell, neighbors);
    assertEquals(FireState.BURNING, treeCell.getCurrState());
    assertEquals(BURNING_COLOR, treeCell.getColor());
  }

  @Test
  void updateStateBurningCell() {
    rulesetMaxProb.updateState(burningCell, neighbors);
    assertEquals(FireState.EMPTY, burningCell.getCurrState());
    assertEquals(EMPTY_COLOR, burningCell.getColor());
  }

}