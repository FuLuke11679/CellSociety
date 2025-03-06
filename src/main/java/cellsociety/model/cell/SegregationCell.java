package cellsociety.model.cell;

import cellsociety.model.state.CellState;

/**
 * Author: Daniel Rodriguez-Florido
 * <p>
 * Cell class for the Segregation Simulation
 */
public class SegregationCell extends Cell {

  public enum SegregationState implements CellState {
    RED,
    BLUE,
    EMPTY
  }

  public SegregationCell(int id, CellState currState, CellState nextState) {
    super(id, currState, nextState);
  }

}
