package cellsociety.model.state;

import cellsociety.model.cell.ConwayCell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.cell.WatorCell.WatorState;

public class WatorStateHandler implements CellStateHandler {

  /**
   * @return A list of enums that represent the possible states of a Wator Cell
   */
  @Override
  public Enum<?>[] getStateEnums() {
    return WatorCell.WatorState.values();
  }

  /**
   * @param state The state we wish to turn into an enum from a string
   * @return An enum
   */
  @Override
  public Enum<?> fromString(String state) {
    return Enum.valueOf(WatorCell.WatorState.class, state.toUpperCase());
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
