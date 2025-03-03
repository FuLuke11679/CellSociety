package cellsociety.model.state;

public interface CellStateHandler {
  Enum<?>[] getStateEnums();
  Enum<?> fromString(String state);
  String toString(Enum<?> state);
}
