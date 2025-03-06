package cellsociety.model.grid.shape;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link CellShape} for a hexagonal grid.
 * <p>
 * This class calculates neighbor offsets for cells in a hexagonal tiling.
 * The offsets differ based on whether the column index is even or odd.
 * </p>
 *
 * @see CellShape
 */
public class HexagonalShape implements CellShape {

  /**
   * Computes neighbor offsets for a cell in a hexagonal grid.
   * <p>
   * For even columns, the neighbor offsets are set accordingly; for odd columns, the offsets are adjusted.
   * </p>
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a list of relative neighbor offsets as integer arrays
   */
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
