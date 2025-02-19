package cellsociety.model.state;

public enum SugarscapeState implements CellState {
  PATCH,  // Represents a patch that is not occupied by an agent.
  AGENT   // Represents a patch that currently has an agent.
}