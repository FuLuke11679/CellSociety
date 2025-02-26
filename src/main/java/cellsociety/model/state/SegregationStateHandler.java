package cellsociety.model.state;

import cellsociety.model.cell.ConwayCell;
import cellsociety.model.cell.PercolationCell;
import cellsociety.model.cell.PercolationCell.PercolationState;

public class SegregationStateHandler implements CellStateHandler {

  /**
   * @return A list of enums that represent the possible states of a Percolation Cell
   */
  @Override
  public Enum<?>[] getStateEnums() {
    return PercolationCell.PercolationState.values();
  }

  /**
   * @param state The state we wish to turn into an enum from a string
   * @return An enum
   */
  @Override
  public Enum<?> fromString(String state) {
    return Enum.valueOf(PercolationCell.PercolationState.class, state.toUpperCase());
  }

  /**
   * @param state The state we wish to turn to a string
   * @return The name of the enum in lowercase as a string
   */
  @Override
  public String toString(Enum<?> state) {
    return state.name().toLowerCase();
  }

}
