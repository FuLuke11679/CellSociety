package cellsociety.model.ruleset;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell.WatorState;
import cellsociety.model.grid.CellShapeFactory;
import cellsociety.model.grid.EdgeFactory;
import cellsociety.model.grid.Grid;
import cellsociety.model.grid.NeighborhoodFactory;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Author: Daniel Rodriguez-Florido
 * Testing class for WatorWorld
 */
class WatorRulesetTest {

  Grid myGrid;
  Ruleset myRuleset;

  @BeforeEach
  void setUp() {
    Map<String, String> params = Map.of(
        "fishBreedTime", "5",
        "fishStarveTime", "4",
        "sharkBreedTime", "5",
        "sharkStarveTime", "4"
    );
    myRuleset = new WatorRuleset(params);
  }

  void setGridParams(Grid grid) {
    myGrid.setEdgeHandler(EdgeFactory.createEdgeHandler(""));
    myGrid.setNeighborhoodStrategy(NeighborhoodFactory.createNeighborhoodStrategy(""));
    myGrid.setCellShape(CellShapeFactory.createCellShape(""));
  }

  @Test
  void updateGridState_SharkMoves_SharkMovesRandomlyToAdjacentSpot() {
    String[] initialStates = {"W","W","W","W","W",
                              "W","W","W","W","W",
                              "W","W","S","W","W",
                              "W","W","W","W","W",
                              "W","W","W","W","W"};
    myGrid = myRuleset.createGrid(5, 5, initialStates);
    setGridParams(myGrid);

    myGrid.update();

    assertEquals(WatorState.WATER, myGrid.getCell(2, 2).getCurrState());

    List<Cell> neighbors = myGrid.getNeighbors(2, 2);
    boolean adjacentSquareContainsShark = false;
    for (Cell c : neighbors) {
      if (c.getCurrState() == WatorState.SHARK) {
        adjacentSquareContainsShark = true;
        break;
      }
    }
    assertTrue(adjacentSquareContainsShark);
  }

  @Test
  void updateGridState_FishMoves_FishMovesRandomlyToAdjacentSpot() {
    String[] initialStates = {"W","W","W","W","W",
                              "W","W","W","W","W",
                              "W","W","F","W","W",
                              "W","W","W","W","W",
                              "W","W","W","W","W"};
    myGrid = myRuleset.createGrid(5, 5, initialStates);
    setGridParams(myGrid);

    myGrid.update();

    assertEquals(WatorState.WATER, myGrid.getCell(2, 2).getCurrState());

    List<Cell> neighbors = myGrid.getNeighbors(2, 2);
    boolean adjacentSquareContainsFish = false;
    for (Cell c : neighbors) {
      if (c.getCurrState() == WatorState.FISH) {
        adjacentSquareContainsFish = true;
        break;
      }
    }
    assertTrue(adjacentSquareContainsFish);
  }

  @Test
  void updateGridState_SharkEatsFish_SharkEatsFishAtRandomlyAtAdjacentSpot() {
    String[] initialStates = {"W","W","W","W","W",
        "W","W","F","W","W",
        "W","F","S","F","W",
        "W","W","F","W","W",
        "W","W","W","W","W"};
    myGrid = myRuleset.createGrid(5, 5, initialStates);
    setGridParams(myGrid);

    myGrid.update();

    assertTrue(myGrid.getCell(2, 2).getCurrState() == WatorState.FISH ||
        myGrid.getCell(2, 2).getCurrState() == WatorState.WATER);

    boolean sharkAteRandomFish = false;
    List<Cell> neighbors = myGrid.getNeighbors(2, 2);
    for (Cell c : neighbors) {
      if (c.getCurrState() == WatorState.SHARK) {
        sharkAteRandomFish = true;
      }
    }
    assertTrue(sharkAteRandomFish);
  }

  @Test
  void updateGridState_SharkMove_SharkDoesNotMoveWhenBlocked() {
    String[] initialStates = {"W","W","W","W","W",
        "W","W","S","W","W",
        "W","S","S","S","W",
        "W","W","S","W","W",
        "W","W","W","W","W"};
    myGrid = myRuleset.createGrid(5, 5, initialStates);
    setGridParams(myGrid);

    myGrid.update();

    assertEquals(WatorState.SHARK, myGrid.getCell(2, 2).getCurrState());
  }

  @Test
  void updateGridState_FishMove_FishDoesNotMoveWhenBlocked() {
    String[] initialStates = {"W","W","W","W","W",
        "W","W","F","W","W",
        "W","F","F","F","W",
        "W","W","F","W","W",
        "W","W","W","W","W"};
    myGrid = myRuleset.createGrid(5, 5, initialStates);
    setGridParams(myGrid);

    myGrid.update();

    assertEquals(WatorState.FISH, myGrid.getCell(2, 2).getCurrState());
  }

  @Test
  void updateGridState_Reproduction_ActiveCellReproduces() {
    Map<String, String> params = Map.of(
        "fishBreedTime", "1",
        "fishStarveTime", "2",
        "sharkBreedTime", "1",
        "sharkStarveTime", "2"
    );
    myRuleset = new WatorRuleset(params);
    String[] initialStates = {"W","W","W","W","W",
                              "W","W","W","W","W",
                              "W","W","S","W","W",
                              "W","W","W","W","W",
                              "W","W","W","W","W"};
    myGrid = myRuleset.createGrid(5, 5, initialStates);
    setGridParams(myGrid);

    myGrid.update();

    assertEquals(WatorState.SHARK, myGrid.getCell(2, 2).getCurrState());

    boolean parentSharkMovedAdjacent = false;
    List<Cell> neighbors = myGrid.getNeighbors(2, 2);
    for (Cell c : neighbors) {
      if (c.getCurrState() == WatorState.SHARK) {
        parentSharkMovedAdjacent = true;
        break;
      }
    }
    assertTrue(parentSharkMovedAdjacent);
  }

}