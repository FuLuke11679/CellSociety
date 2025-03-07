package cellsociety.model.grid.shape;

import java.util.Arrays;
import java.util.List;


/**
 * Implementation of {@link CellShape} for a pentagonal tiling. This class computes neighbor offsets
 * for cells in a pentagonal tiling. The offsets differ based on whether the sum of the row and
 * column indices is even or odd.
 *
 * @author Luke
 * @see CellShape
 */
public class PentagonalTilingShape implements CellShape {


  /**
   * Computes neighbor offsets for a cell in a pentagonal tiling. For one orientation (when row+col
   * is even), returns a set of offsets for a left-tilted pentagon. For the opposite orientation,
   * returns offsets for a right-tilted pentagon.
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
          new int[]{0, -1},  // Right
          new int[]{0, 1},   // Left
          new int[]{1, 1},   // Bottom Left diagonal
          new int[]{1, -1}  // Bottom Right
      );
    } else {
      // Left facing pentagon
      return Arrays.asList(
          new int[]{-1, 0},   // Top
          new int[]{1, 0},    // Bottom
          new int[]{0, -1},   // Right
          new int[]{0, 1},    // Left
          new int[]{-1, 1},    // Top Left diagonal
          new int[]{-1, -1} //top Right
      );
    }
  }

  /**
   * Computes inner neighbor offsets for a cell in a Pentagonal grid. Since they are the same, it
   * returns the results of getNeighborOffsets
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a list of relative neighbor offsets as integer arrays
   */
  @Override
  public List<int[]> getInnerNeighborOffsets(int row, int col) {
    return getNeighborOffsets(row, col);
  }
}
