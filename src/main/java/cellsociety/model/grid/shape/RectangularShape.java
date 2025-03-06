package cellsociety.model.grid.shape;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link CellShape} for a Rectangular grid.
 * <p>
 * This class calculates neighbor offsets for cells in a rectangular tiling.
 * </p>
 *
 * @see CellShape
 */
public class RectangularShape implements CellShape{

  /**
   * Computes and returns a list of relative neighbor offsets for a cell in a rectangular grid.
   * <p>
   * For even-indexed rows, the offsets include:
   * <ul>
   *   <li>{@code {-1, 0}}: Top neighbor</li>
   *   <li>{@code {-1, 1}}: Top-right neighbor</li>
   *   <li>{@code {0, -1}}: Left neighbor</li>
   *   <li>{@code {0, 1}}: Right neighbor</li>
   *   <li>{@code {1, 0}}: Bottom neighbor</li>
   *   <li>{@code {1, 1}}: Bottom-right neighbor</li>
   * </ul>
   * For odd-indexed rows, the offsets include:
   * <ul>
   *   <li>{@code {-1, -1}}: Top-left neighbor</li>
   *   <li>{@code {-1, 0}}: Top neighbor</li>
   *   <li>{@code {0, -1}}: Left neighbor</li>
   *   <li>{@code {0, 1}}: Right neighbor</li>
   *   <li>{@code {1, -1}}: Bottom-left neighbor</li>
   *   <li>{@code {1, 0}}: Bottom neighbor</li>
   * </ul>
   * </p>
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a list of integer arrays, each representing a relative neighbor offset
   */
  @Override
  public List<int[]> getNeighborOffsets(int row, int col) {
    if (row % 2 == 0) {
      return Arrays.asList(new int[]{-1, 0}, new int[]{-1, 1}, new int[]{0, -1}, new int[]{0, 1}, new int[]{1, 0}, new int[]{1, 1});
    } else {
      return Arrays.asList(new int[]{-1, -1}, new int[]{-1, 0}, new int[]{0, -1}, new int[]{0, 1}, new int[]{1, -1}, new int[]{1, 0});
    }
  }
}
