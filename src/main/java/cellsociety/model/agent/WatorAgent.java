package cellsociety.model.agent;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.state.CellState;
import java.util.List;

public interface WatorAgent {
  void move(List<Cell> neighbors);
  boolean getMoved();
  void setMoved(boolean moved);
  int getEnergy();
  int getReproductionTime();
  CellState getState();
}