package cellsociety.model.grid.neighborhood;
import java.util.List;

public interface NeighborhoodStrategy {
  public List<int[]> selectNeighbors(List<int[]> baseOffsets);
}
