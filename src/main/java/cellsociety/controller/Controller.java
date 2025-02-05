package cellsociety.controller;

import cellsociety.Grid;
import cellsociety.GridView;
import cellsociety.model.cell.ConwayCell.ConwayState;
import cellsociety.model.cell.FireCell.FireState;
import cellsociety.model.cell.PercolationCell.PercolationState;
import cellsociety.model.state.CellState;
import java.util.HashMap;
import java.util.Map;

public class Controller {

  private final static Map<String, CellState> stateMap = Map.of(
      "A", ConwayState.ALIVE,
      "D", ConwayState.DEAD,
      "B", FireState.BURNING,
      "T", FireState.TREE,
      "E", FireState.EMPTY,
      "BL", PercolationState.BLOCKED,
      "P", PercolationState.PERCOLATED,
      "O", PercolationState.OPEN
  );

  private Grid myGrid;
  private GridView myGridView;

  public Controller() {

  }

}
