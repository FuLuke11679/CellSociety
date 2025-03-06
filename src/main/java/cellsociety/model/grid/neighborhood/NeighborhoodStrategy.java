package cellsociety.model.grid.neighborhood;

import java.util.List;

public interface NeighborhoodStrategy {

  List<int[]> selectNeighbors(List<int[]> baseOffsets);
}
