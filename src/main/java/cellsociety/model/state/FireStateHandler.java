package cellsociety.model.state;

import cellsociety.model.cell.ConwayCell;
import cellsociety.model.cell.FireCell;

public class FireStateHandler implements CellStateHandler {

  /**
   * @return A list of enums that represent the possible states of a Fire Cell
   */
  @Override
  public Enum<?>[] getStateEnums() {
    return FireCell.FireState.values();
  }

  /**
   * @param state The state we wish to turn into an enum from a string
   * @return An enum
   */
  @Override
  public Enum<?> fromString(String state) {
    return Enum.valueOf(FireCell.FireState.class, state.toUpperCase());
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
