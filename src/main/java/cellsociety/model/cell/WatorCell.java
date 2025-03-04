package cellsociety.model.cell;

import cellsociety.model.state.CellState;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Cell class for the WatorWorld Simulation
 */
public class WatorCell extends Cell {

  public enum WatorState implements CellState {
    FISH,
    SHARK,
    WATER
  }

  public WatorCell(int id, CellState currState, CellState nextState) {
    super(id, currState, nextState);
  }

}
