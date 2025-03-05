package cellsociety.model.grid;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.grid.neighborhood.ExtendedMooreNeighborhood;
import cellsociety.model.grid.neighborhood.NeighborhoodStrategy;
import org.junit.jupiter.api.Test;

class NeighborhoodFactoryTest {

  @Test
  void testCreateNeighborhoodStrategyValid() {
    // If you have a valid neighborhood type, e.g., "ExtendedMoore", the factory should create that.
    NeighborhoodStrategy strategy = NeighborhoodFactory.createNeighborhoodStrategy("ExtendedMoore");
    assertNotNull(strategy, "The NeighborhoodStrategy instance should not be null.");
    // If the valid type exists, it should be an instance of the expected class.
    // If not, the fallback ExtendedMooreNeighborhood is used.
    assertTrue(strategy instanceof ExtendedMooreNeighborhood, "Expected instance of ExtendedMooreNeighborhood.");
  }

  @Test
  void testCreateNeighborhoodStrategyInvalidFallsBack() {
    // For an invalid neighborhood type, the factory should fall back to ExtendedMooreNeighborhood.
    NeighborhoodStrategy strategy = NeighborhoodFactory.createNeighborhoodStrategy("InvalidNeighborhood");
    assertNotNull(strategy, "The fallback NeighborhoodStrategy instance should not be null.");
    assertTrue(strategy instanceof ExtendedMooreNeighborhood, "Expected fallback instance of ExtendedMooreNeighborhood.");
  }
}
