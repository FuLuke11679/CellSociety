package cellsociety.model.grid.neighborhood;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link NeighborhoodStrategy} that selects only orthogonal neighbors.
 * This strategy filters the base neighbor offsets to only include neighbors that are directly
 * adjacent (no diagonal neighbors), as determined by a Manhattan distance of 1.
 * @Auhor Luke
 * @see NeighborhoodStrategy
 */
public class VonNeumannNeighborhood implements NeighborhoodStrategy{

  /**
   * Filters the base neighbor offsets to include only orthogonal neighbors.
   *
   * @param baseOffsets the list of base neighbor offsets
   * @return a list of offsets where the sum of absolute row and column offsets is equal to 1
   */
  @Override
  public List<int[]> selectNeighbors(List<int[]> baseOffsets) {
    return baseOffsets.stream()
        .filter(offset -> Math.abs(offset[0]) + Math.abs(offset[1]) == 1) // Only orthogonal neighbors
        .collect(Collectors.toList());
  }
}
