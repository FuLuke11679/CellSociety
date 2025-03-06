package cellsociety.model.grid;

import cellsociety.model.grid.neighborhood.ExtendedMooreNeighborhood;
import cellsociety.model.grid.neighborhood.NeighborhoodStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class for creating instances of {@link NeighborhoodStrategy}.
 * <p>
 * This class dynamically loads and instantiates a {@code NeighborhoodStrategy} implementation based on the provided type.
 * If the neighborhood type is null, empty, or if an error occurs during instantiation,
 * it defaults to using {@link ExtendedMooreNeighborhood}.
 * </p>
 */
public class NeighborhoodFactory {

  private static final Logger LOGGER = Logger.getLogger(NeighborhoodFactory.class.getName());

  /**
   * Creates and returns a {@link NeighborhoodStrategy} instance based on the provided neighborhood type.
   * <p>
   * The method attempts to dynamically load a class with the name
   * "cellsociety.model.grid.neighborhood.&lt;neighborhoodType&gt;Neighborhood".
   * If the neighborhood type is null, empty, or an error occurs, it returns an instance of {@link ExtendedMooreNeighborhood}.
   * </p>
   *
   * @param neighborhoodType the type of neighborhood to instantiate (e.g., "ExtendedMoore", "VonNeumann")
   * @return a {@code NeighborhoodStrategy} corresponding to the specified type, or a default instance if the type is invalid
   */
  public static NeighborhoodStrategy createNeighborhoodStrategy(String neighborhoodType) {
    if (neighborhoodType == null || neighborhoodType.trim().isEmpty()) {
      LOGGER.warning("Neighborhood type is null or empty. Using default ExtendedMooreNeighborhood.");
      return new ExtendedMooreNeighborhood();
    }

    try {
      String className = "cellsociety.model.grid.neighborhood." + neighborhoodType + "Neighborhood";
      LOGGER.info("Attempting to load NeighborhoodStrategy class: " + className);
      return (NeighborhoodStrategy) Class.forName(className)
          .getDeclaredConstructor()
          .newInstance();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error loading NeighborhoodStrategy for type: "
          + neighborhoodType + ". Using default ExtendedMooreNeighborhood.", e);
      return new ExtendedMooreNeighborhood();
    }
  }
}
