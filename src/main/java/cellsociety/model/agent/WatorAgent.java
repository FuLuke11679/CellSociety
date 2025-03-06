package cellsociety.model.agent;

import cellsociety.model.cell.Cell;
import cellsociety.model.cell.WatorCell;
import cellsociety.model.state.CellState;
import java.util.List;

/**
 * Author: Daniel Rodriguez-Florido
 *
 * Interface for agents that could be in a WatorCell. Currently
 * implemented by Shark and Fish concrete agents.
 */
public interface WatorAgent {
  void move(List<Cell> neighbors);
  boolean getMoved();
  void setMoved(boolean moved);
  int getEnergy();
  int getReproductionTime();
  CellState getState();
}