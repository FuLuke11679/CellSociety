package cellsociety.model.grid.shape;

import java.util.ArrayList;
import java.util.List;

public class TriangleShape implements CellShape {
  @Override
  public List<int[]> getNeighborOffsets(int row, int col) {
    List<int[]> offsets = new ArrayList<>();

    if ((row + col) % 2 == 0) {
      offsets.add(new int[]{-1, 0});
      offsets.add(new int[]{0, -1});
      offsets.add(new int[]{0, 1});
      offsets.add(new int[]{1, 0});
      offsets.add(new int[]{1, 1});
      offsets.add(new int[]{-1, -1});
    } else {
      offsets.add(new int[]{-1, 1});
      offsets.add(new int[]{-1, 0});
      offsets.add(new int[]{0, -1});
      offsets.add(new int[]{0, 1});
      offsets.add(new int[]{1, -1});
      offsets.add(new int[]{1, 0});
    }
    return offsets;
  }
}

