package cellsociety.model.grid.shape;

import java.util.Arrays;
import java.util.List;

public class HexagonalShape implements CellShape{

  @Override
  public List<int[]> getNeighborOffsets(int row, int col) {
    return Arrays.asList(
        new int[]{-1, -1}, new int[]{-1, 0}, new int[]{-1, 1},
        new int[]{0, -1},  /* Current Cell */ new int[]{0, 1},
        new int[]{1, -1}, new int[]{1, 0}, new int[]{1, 1}
    );
  }
}
