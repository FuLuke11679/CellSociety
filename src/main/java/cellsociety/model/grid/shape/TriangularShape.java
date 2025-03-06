package cellsociety.model.grid.shape;

import java.util.ArrayList;
import java.util.List;
/**
 * Implementation of {@link CellShape} for a Triangular grid.
 * This class calculates neighbor offsets for cells in a triangular tiling.
 * The offsets differ based on whether the column index is even or odd.
 *
 * @see CellShape
 */
public class TriangularShape implements CellShape {

  /**
   * Computes and returns a list of relative neighbor offsets for a cell in a triangular grid.
   * <p>
   * When the sum of the row and column indices is even, the method returns the following offsets:
   * <ul>
   *   <li>{@code {-1, 0}}</li>
   *   <li>{@code {0, -1}}</li>
   *   <li>{@code {0, 1}}</li>
   *   <li>{@code {1, 0}}</li>
   *   <li>{@code {1, 1}}</li>
   *   <li>{@code {-1, -1}}</li>
   * </ul>
   * When the sum is odd, the offsets returned are:
   * <ul>
   *   <li>{@code {-1, 1}}</li>
   *   <li>{@code {-1, 0}}</li>
   *   <li>{@code {0, -1}}</li>
   *   <li>{@code {0, 1}}</li>
   *   <li>{@code {1, -1}}</li>
   *   <li>{@code {1, 0}}</li>
   * </ul>
   * </p>
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a list of integer arrays, each representing a relative neighbor offset
   */
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

