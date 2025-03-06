package cellsociety.model.cell;

import cellsociety.model.state.CellState;

/**
 * Author: Daniel Rodriguez-Florido
 * <p>
 * Cell class for the Percolation Simulation
 */
public class PercolationCell extends Cell {

  public enum PercolationState implements CellState {
    BLOCKED,
    OPEN,
    PERCOLATED
  }

  public PercolationCell(int id, CellState currState, CellState nextState) {
    super(id, currState, nextState);
  }

}
