package cellsociety.ruleset;

import cellsociety.cell.*;
import java.util.List;
import cellsociety.cell.GameOfLifeCell.State;
import javafx.scene.paint.Color;

public class GameOfLifeRuleset {

  public GameOfLifeRuleset() {}

  public int countNeighbors(GameOfLifeCell cell, List<GameOfLifeCell> neighbors) {
    int aliveCells = 0;
    for (GameOfLifeCell neighbor : neighbors) {
      if (getState(cell, neighbor) == State.ALIVE) { //If the cell is alive
        aliveCells++;
      }
    }
    return aliveCells;
  }

  public void updateState(GameOfLifeCell cell, List<GameOfLifeCell> neighbors) {
    int aliveCells = countNeighbors(cell, neighbors);
    if (aliveCells < 2 || aliveCells > 3) {
      killCell(cell);
    } else {
      birthCell(cell);
    }
  }

  private State getState(GameOfLifeCell cell, GameOfLifeCell neighbor) {
    if (neighbor.getId() < cell.getId()) {
      return neighbor.getPrevState();
    }
    return neighbor.getCurrState();
  }

  private void killCell(GameOfLifeCell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(State.DEAD);
    cell.setColor(Color.WHITE);
  }

  private void birthCell(GameOfLifeCell cell) {
    cell.setPrevState(cell.getCurrState());
    cell.setCurrState(State.ALIVE);
    cell.setColor(Color.BLACK);
  }

}
