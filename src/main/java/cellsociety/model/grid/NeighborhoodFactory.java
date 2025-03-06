package cellsociety.model.grid;

import cellsociety.model.grid.neighborhood.ExtendedMooreNeighborhood;
import cellsociety.model.grid.neighborhood.NeighborhoodStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NeighborhoodFactory {

  private static final Logger LOGGER = Logger.getLogger(NeighborhoodFactory.class.getName());

  public static NeighborhoodStrategy createNeighborhoodStrategy(String neighborhoodType) {
    if (neighborhoodType == null || neighborhoodType.trim().isEmpty()) {
      LOGGER.warning(
          "Neighborhood type is null or empty. Using default ExtendedMooreNeighborhood.");
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
