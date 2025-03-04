package cellsociety.model.cell;

import cellsociety.model.state.CellState;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Cell class for the Fire spreading simulation
 */
public class FireCell extends Cell {

  public enum FireState implements CellState {
    EMPTY,
    TREE,
    BURNING
  }

  public FireCell(int id, CellState currState, CellState nextState) {
    super(id, currState, nextState);
  }

}
