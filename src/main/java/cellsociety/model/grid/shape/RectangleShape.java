package cellsociety.model.grid.shape;

import java.util.Arrays;
import java.util.List;

public class RectangleShape implements CellShape{

  @Override
  public List<int[]> getNeighborOffsets(int row, int col) {
    if (row % 2 == 0) {
      return Arrays.asList(new int[]{-1, 0}, new int[]{-1, 1}, new int[]{0, -1}, new int[]{0, 1}, new int[]{1, 0}, new int[]{1, 1});
    } else {
      return Arrays.asList(new int[]{-1, -1}, new int[]{-1, 0}, new int[]{0, -1}, new int[]{0, 1}, new int[]{1, -1}, new int[]{1, 0});
    }
  }
}
