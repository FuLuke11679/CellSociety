package cellsociety.model.grid.shape;

import java.util.List;

public interface CellShape {
  public List<int[]> getNeighborOffsets(int row, int col);

}
