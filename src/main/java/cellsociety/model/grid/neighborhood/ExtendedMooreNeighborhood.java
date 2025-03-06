package cellsociety.model.grid.neighborhood;

import java.util.List;

/**
 * Implementation of the {@link NeighborhoodStrategy} that selects all neighbors in a Moore neighborhood.
 * <p>
 * This strategy returns all neighbor offsets provided (including diagonal neighbors).
 * </p>
 *
 * @author Luke
 * @see NeighborhoodStrategy
 */
public class ExtendedMooreNeighborhood implements NeighborhoodStrategy{
  /**
   * Filters the base neighbor offsets to include only orthogonal neighbors.
   *
   * @param baseOffsets the list of base neighbor offsets
   * @return a list of offsets pertaining to all possible sides.
   */
  @Override
  public List<int[]> selectNeighbors(List<int[]> baseOffsets) {
    return baseOffsets; // Uses all neighbors, including diagonals
  }
}
