package cellsociety.model.grid.shape;

import java.util.List;

public interface CellShape {

  List<int[]> getNeighborOffsets(int row, int col);

}
