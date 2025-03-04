package cellsociety.model.grid;

import cellsociety.model.grid.neighborhood.ExtendedMooreNeighborhood;
import cellsociety.model.grid.neighborhood.NeighborhoodStrategy;

public class NeighborhoodFactory {
  public static NeighborhoodStrategy createNeighborhoodStrategy(String neighborhoodType) {
    try {
      String className = "cellsociety.model.grid.neighborhood." + neighborhoodType + "Neighborhood";
      System.out.println("Attempting to load NeighborhoodStrategy class: " + className);
      return (NeighborhoodStrategy) Class.forName(className).getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      System.err.println("Error loading NeighborhoodStrategy: " + e.getMessage());
      e.printStackTrace();
      return new ExtendedMooreNeighborhood(); // Default fallback
    }
  }
}
