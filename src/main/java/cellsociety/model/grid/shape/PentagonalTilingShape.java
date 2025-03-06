package cellsociety.model.grid.shape;

import java.util.Arrays;
import java.util.List;


/**
 * Implementation of {@link CellShape} for a pentagonal tiling.
 * <p>
 * This class computes neighbor offsets for cells in a pentagonal tiling.
 * The offsets differ based on whether the sum of the row and column indices is even or odd.
 * </p>
 *
 * @see CellShape
 */
public class PentagonalTilingShape implements CellShape {


  /**
   * Computes neighbor offsets for a cell in a pentagonal tiling.
   * <p>
   * For one orientation (when row+col is even), returns a set of offsets for a left-tilted pentagon.
   * For the opposite orientation, returns offsets for a right-tilted pentagon.
   * </p>
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a list of relative neighbor offsets as integer arrays
   */
  @Override
  public List<int[]> getNeighborOffsets(int row, int col) {
    if ((col) % 2 == 0) {
      // Right facing pentagon
      return Arrays.asList(
          new int[]{-1, 0},  // Top
          new int[]{1, 0},   // Bottom
          new int[]{0, -1},  // Left
          new int[]{0, 1},   // Right
          new int[]{1, 1},   // Bottom Right diagonal
          new int []{1, -1}  // Bottom Left
      );
    } else {
      // Left facing pentagon
      return Arrays.asList(
          new int[]{-1, 0},   // Top
          new int[]{1, 0},    // Bottom
          new int[]{0, -1},   // Left
          new int[]{0, 1},    // Right
          new int[]{-1, 1},    // Top Right diagonal
          new int[] {-1, -1} //top left
      );
    }
  }
}
