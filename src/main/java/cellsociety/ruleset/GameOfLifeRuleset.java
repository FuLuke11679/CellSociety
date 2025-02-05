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
      if (neighbor instanceof GameOfLifeCell gameCell) {
        if (getState(cell, gameCell) == State.ALIVE) {
          aliveCells++;
        }
      }
    }
    return aliveCells;
  }

  public void updateState(Cell cell, List<Cell> neighbors) {
    if (!(cell instanceof GameOfLifeCell gameCell)) {
      return;
    }

    int aliveCells = countNeighbors(gameCell, neighbors);
    State nextState = gameCell.getCurrState(); // Default to staying the same

    if (gameCell.getCurrState() == State.ALIVE) {
      if (aliveCells < 2 || aliveCells > 3) {
        nextState = State.DEAD; // Overcrowding or underpopulation
      }
    } else {
      if (aliveCells == 3) {
        nextState = State.ALIVE; // Reproduction
      }
    }

    gameCell.setPrevState(gameCell.getCurrState());
    gameCell.setCurrState(nextState);
    gameCell.setColor(nextState == State.ALIVE ? Color.BLACK : Color.WHITE);
  }


  private State getState(Cell cell, GameOfLifeCell neighbor) {
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
