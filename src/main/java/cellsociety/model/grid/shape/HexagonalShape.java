package cellsociety.model.grid.shape;

import java.util.Arrays;
import java.util.List;

public class HexagonalShape implements CellShape {

  @Override
  public List<int[]> getNeighborOffsets(int row, int col) {
    if (col % 2 == 0) {  // Even columns
      return Arrays.asList(
          new int[]{-1, 0}, new int[]{-1, 1},  // Top left, top right
          new int[]{0, -1}, new int[]{0, 1},   // Left, right
          new int[]{1, 0}, new int[]{1, 1}     // Bottom left, bottom right
      );
    } else {  // Odd columns shift neighbors
      return Arrays.asList(
          new int[]{-1, -1}, new int[]{-1, 0},  // Top left, top right
          new int[]{0, -1}, new int[]{0, 1},   // Left, right
          new int[]{1, -1}, new int[]{1, 0}    // Bottom left, bottom right
      );
    }
  }
}
