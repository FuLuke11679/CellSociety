package cellsociety.model.ruleset;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.ConwayCell.ConwayState;
import java.util.List;
import cellsociety.model.state.CellState;
import javafx.scene.paint.Color;

public class ConwayRuleset extends Ruleset {

  public ConwayRuleset() {}

  private int countNeighbors(Cell cell, List<Cell> neighbors) {
    int aliveCells = 0;
    for (Cell neighbor : neighbors) {
      if (getState(cell, neighbor) == ConwayState.ALIVE) { //If the cell is alive
        aliveCells++;
      }
    }
    return aliveCells;
  }

  public void updateState(Cell cell, List<Cell> neighbors) {
    int aliveCells = countNeighbors(cell, neighbors);

    if (cell.getCurrState() == ConwayState.ALIVE) {
      // Live cell survives with 2 or 3 neighbors, otherwise it dies
      if (aliveCells < 2 || aliveCells > 3) {
        killCell(cell);
      } else {
        maintainCell(cell);
      }
    } else {
      // Dead cell becomes alive if it has exactly 3 neighbors
      if (aliveCells == 3) {
        birthCell(cell);
      } else {
        maintainCell(cell);  // Explicitly keep it dead if it doesn't meet the condition
      }
    }
  }



  private void killCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(ConwayState.DEAD);
    cell.setColor(Color.WHITE);
  }

  private void birthCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(ConwayState.ALIVE);
    cell.setColor(Color.BLACK);
  }

  private void maintainCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
  }

}
