package cellsociety.model.grid.shape;

import java.util.Arrays;
import java.util.List;

public class PentagonalTilingShape implements CellShape {

  @Override
  public List<int[]> getNeighborOffsets(int row, int col) {
    if ((row + col) % 2 == 0) {
      // Left-tilted pentagon
      return Arrays.asList(
          new int[]{-1, 0},  // Top
          new int[]{1, 0},   // Bottom
          new int[]{0, -1},  // Left
          new int[]{0, 1},   // Right
          new int[]{1, -1}   // Bottom left diagonal
      );
    } else {
      // Right-tilted pentagon
      return Arrays.asList(
          new int[]{-1, 0},   // Top
          new int[]{1, 0},    // Bottom
          new int[]{0, -1},   // Left
          new int[]{0, 1},    // Right
          new int[]{-1, 1}    // Top right diagonal
      );
    }
  }
}
