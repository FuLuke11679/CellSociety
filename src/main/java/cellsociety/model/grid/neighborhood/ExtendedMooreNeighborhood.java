package cellsociety.model.grid.neighborhood;

import java.util.List;

public class ExtendedMooreNeighborhood implements NeighborhoodStrategy{

  @Override
  public List<int[]> selectNeighbors(List<int[]> baseOffsets) {
    return baseOffsets; // Uses all neighbors, including diagonals
  }
}
