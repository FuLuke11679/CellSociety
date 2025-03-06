package cellsociety.model.cell;

import cellsociety.model.state.CellState;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Concrete cell class for the Conway Game of Life Simulation
 */
public class ConwayCell extends Cell {

  public enum ConwayState implements CellState {
    ALIVE,
    DEAD
  }

  public ConwayCell(int id, CellState currState, CellState nextState) {
    super(id, currState, nextState);
  }

}
