package cellsociety.model.grid.handler;

import cellsociety.model.cell.Cell;
import cellsociety.model.grid.Grid;
import java.util.List;

public interface EdgeHandler {

  List<Cell> handleNeighbors(int row, int col, List<int[]> offsets, Grid grid);
}
