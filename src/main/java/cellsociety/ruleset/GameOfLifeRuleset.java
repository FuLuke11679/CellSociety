package cellsociety.ruleset;

import cellsociety.cell.*;
import java.util.List;
import cellsociety.cell.GameOfLifeCell.State;
import javafx.scene.paint.Color;

public class GameOfLifeRuleset extends Ruleset {

  public GameOfLifeRuleset() {}

  private int countNeighbors(Cell cell, List<Cell> neighbors) {
    int aliveCells = 0;
    for (Cell neighbor : neighbors) {
      if (getState(cell, neighbor) == State.ALIVE) { //If the cell is alive
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

  private State getState(Cell cell, Cell neighbor) {
    if (neighbor.getId() < cell.getId()) {
      return neighbor.getPrevState();
    }
    return neighbor.getCurrState();
  }

  private void killCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(State.DEAD);
    cell.setColor(Color.WHITE);
  }

  private void birthCell(Cell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(State.ALIVE);
    cell.setColor(Color.BLACK);
  }

}
