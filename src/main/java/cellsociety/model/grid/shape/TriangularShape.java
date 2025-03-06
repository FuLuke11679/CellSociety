package cellsociety.model.grid.shape;

import java.util.ArrayList;
import java.util.List;
/**
 * Implementation of {@link CellShape} for a Triangular grid.
 * This class calculates neighbor offsets for cells in a triangular tiling.
 * The offsets differ based on whether the column index is even or odd.
 *
 * @author Luke
 * @see CellShape
 */
public class TriangularShape implements CellShape {

  /**
   * Computes and returns a list of relative neighbor offsets for a cell in a triangular grid.
   * When the sum of the row and column indices is even, the method returns the following offsets:
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a list of integer arrays, each representing a relative neighbor offset
   */
  @Override
  public List<int[]> getNeighborOffsets(int row, int col) {
    List<int[]> offsets = new ArrayList<>();
    //Upright
    if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
      offsets.add(new int[]{-1, 0}); //Right
      offsets.add(new int[]{1, 0}); //Left
      offsets.add(new int[]{-2, 0}); //Right 2
      offsets.add(new int[]{2, 0}); //Left 2
      offsets.add(new int[]{0, -1}); //Up
      offsets.add(new int[]{0, 1}); //Down
      offsets.add(new int[]{-1, -1}); //Top Right
      offsets.add(new int[]{1, -1}); //Top Left
      offsets.add(new int[]{-1, 1}); //Bottom Right
      offsets.add(new int[]{1, 1}); //Bottom Left
      offsets.add(new int[]{-2, 1}); //Bottom Right 2
      offsets.add(new int[]{2, 1}); //Bottom Left 2

      //Down
    } else {
      offsets.add(new int[]{-1, 0}); //Right
      offsets.add(new int[]{1, 0}); //Left
      offsets.add(new int[]{-2, 0}); //Right 2
      offsets.add(new int[]{2, 0}); //Left 2
      offsets.add(new int[]{0, -1}); //Up
      offsets.add(new int[]{0, 1}); //Down
      offsets.add(new int[]{-1, -1}); //Top Right
      offsets.add(new int[]{1, -1}); //Top Left
      offsets.add(new int[]{-1, -1}); //Top Right 2
      offsets.add(new int[]{1, -1}); //Top Left 2
      offsets.add(new int[]{-1, 1}); //Bottom Right
      offsets.add(new int[]{1, 1}); //Bottom Left
    }
    return offsets;
  }
  /**
   * Computes inner neighbor offsets for a cell in a Pentagonal grid.
   * Returns left, and right and top or bottom depending on if its faced upward or downward
   *
   * @param row the row index of the cell
   * @param col the column index of the cell
   * @return a list of relative neighbor offsets as integer arrays
   */
  @Override
  public List<int[]> getInnerNeighborOffsets(int row, int col) {
    List<int[]> offsets = new ArrayList<>();
    //Upright
    if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
      offsets.add(new int[]{-1, 0}); //Right
      offsets.add(new int[]{1, 0}); //Left
      offsets.add(new int[]{0, 1}); //Down

      //Down
    } else {
      offsets.add(new int[]{-1, 0}); //Right
      offsets.add(new int[]{1, 0}); //Left
      offsets.add(new int[]{0, -1}); //Up
    }
    return offsets;
  }
}
