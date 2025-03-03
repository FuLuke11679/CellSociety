package cellsociety.model.grid.neighborhood;

import java.util.List;
import java.util.stream.Collectors;

public class VonNeumannNeighborhood implements NeighborhoodStrategy{

  @Override
  public List<int[]> selectNeighbors(List<int[]> baseOffsets) {
    return baseOffsets.stream()
        .filter(offset -> Math.abs(offset[0]) + Math.abs(offset[1]) == 1) // Only orthogonal neighbors
        .collect(Collectors.toList());
  }
}
