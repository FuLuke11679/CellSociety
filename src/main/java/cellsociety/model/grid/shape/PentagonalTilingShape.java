package cellsociety.model.grid.shape;

import java.util.ArrayList;
import java.util.List;

public class PentagonalTilingShape implements CellShape {
  @Override
  public List<int[]> getNeighborOffsets(int row, int col) {
    List<int[]> offsets = new ArrayList<>();

    // Standard Moore neighborhood
    offsets.add(new int[]{-1, -1});
    offsets.add(new int[]{-1, 0});
    offsets.add(new int[]{-1, 1});
    offsets.add(new int[]{0, -1});
    offsets.add(new int[]{0, 1});
    offsets.add(new int[]{1, -1});
    offsets.add(new int[]{1, 0});
    offsets.add(new int[]{1, 1});

    // Additional pentagonal tiling rule: Only allow certain diagonal movements
    if ((row + col) % 2 == 0) {
      offsets.removeIf(offset -> offset[0] == offset[1]); // Remove diagonal matches
    } else {
      offsets.removeIf(offset -> offset[0] != offset[1]); // Remove non-diagonal matches
    }

    return offsets;
  }
}

