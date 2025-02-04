package cellsociety.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.cell.Cell;
import cellsociety.cell.FireCell;
import cellsociety.cell.FireCell.FireState;
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

  Ruleset ruleset = new FireRuleset(1, 1);

  @BeforeEach
  void setUp() {
    emptyCell = new FireCell(2, FireState.EMPTY, FireState.EMPTY);
    burningCell = new FireCell(2, FireState.BURNING, FireState.BURNING);
    treeCell = new FireCell(2, FireState.TREE, FireState.TREE);
  }

  @Test
  void updateStateEmptyCell() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.TREE, FireState.TREE),
        new FireCell(1, FireState.TREE, FireState.TREE),
        new FireCell(3, FireState.TREE, FireState.TREE),
        new FireCell(4, FireState.TREE, FireState.TREE)
    ));
    ruleset.updateState(emptyCell, neighbors);
    assertEquals(FireState.TREE, emptyCell.getCurrState());
    assertEquals(TREE_COLOR, emptyCell.getColor());
  }

  @Test
  void updateStateTreeCellWithoutBurningNeighbor() {
    neighbors = new ArrayList<>(List.of(
        new FireCell(0, FireState.TREE, FireState.TREE),
        new FireCell(1, FireState.TREE, FireState.TREE),
        new FireCell(3, FireState.TREE, FireState.TREE),
        new FireCell(4, FireState.TREE, FireState.TREE)
    ));
    ruleset.updateState(treeCell, neighbors);
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
    ruleset.updateState(treeCell, neighbors);
    assertEquals(FireState.BURNING, treeCell.getCurrState());
    assertEquals(BURNING_COLOR, treeCell.getColor());
  }

  @Test
  void updateStateBurningCell() {
    ruleset.updateState(burningCell, neighbors);
    assertEquals(FireState.EMPTY, burningCell.getCurrState());
    assertEquals(EMPTY_COLOR, burningCell.getColor());
  }

}