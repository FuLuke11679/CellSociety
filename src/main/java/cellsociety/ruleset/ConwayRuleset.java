package cellsociety.ruleset;

import cellsociety.cell.*;
import cellsociety.cell.ConwayCell.GameOfLifeState;
import java.util.List;
import cellsociety.state.CellState;
import javafx.scene.paint.Color;

public class ConwayRuleset extends Ruleset {

  public ConwayRuleset() {}

  private int countNeighbors(Cell cell, List<Cell> neighbors) {
    int aliveCells = 0;
    for (Cell neighbor : neighbors) {
      if (getState(cell, neighbor) == GameOfLifeState.ALIVE) { //If the cell is alive
        aliveCells++;
      }
    }
    return aliveCells;
  }

  public void updateState(Cell cell, List<Cell> neighbors) {
    int aliveCells = countNeighbors(cell, neighbors);
    if (aliveCells == 2 || aliveCells == 3) {
      birthCell(cell);
    } else {
      killCell(cell);
    }
  }

  private CellState getState(Cell cell, Cell neighbor) {
    if (neighbor.getId() < cell.getId()) {
      return neighbor.getPrevState();
    }
    return neighbor.getCurrState();
  }

  private void killCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(GameOfLifeState.DEAD);
    cell.setColor(Color.WHITE);
  }

  private void birthCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(GameOfLifeState.ALIVE);
    cell.setColor(Color.BLACK);
  }

}
