package cellsociety.model.grid.shape;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link CellShape} for a Rectangular grid. This class calculates neighbor
 * offsets for cells in a rectangular tiling.
 *
 * @author Luke
 * @see CellShape
 */
public class RectangularShape implements CellShape {

  /**
   * Computes and returns a list of relative neighbor offsets for a cell in a rectangular grid. For
   * even-indexed rows, the offsets include:
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a list of integer arrays, each representing a relative neighbor offset
   */
  @Override
  public List<int[]> getNeighborOffsets(int row, int col) {
    return Arrays.asList(
        new int[]{-1, 0},
        new int[]{0, -1},
        new int[]{0, 1},
        new int[]{1, 0},
        new int[]{-1, 1}, //Bottom Right
        new int[]{1, 1}, //Bottom Left
        new int[]{1, -1}, //Top Left
        new int[]{-1, -1});
  }

  /**
   * Computes inner neighbor offsets for a cell in a Pentagonal grid. Returns top, bottom, left, and
   * right direct cells
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a list of relative neighbor offsets as integer arrays
   */
  @Override
  public List<int[]> getInnerNeighborOffsets(int row, int col) {
    return Arrays.asList(new int[]{-1, 0}, new int[]{0, -1}, new int[]{0, 1}, new int[]{1, 0});
  }
}
