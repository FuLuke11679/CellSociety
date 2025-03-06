package cellsociety.model.grid;

import cellsociety.model.grid.handler.EdgeHandler;
import cellsociety.model.grid.handler.MirrorEdgeHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class for creating instances of {@link EdgeHandler}.
 * <p>
 * This class dynamically loads a specific {@code EdgeHandler} implementation based on the provided edge type.
 * If the provided edge type is null, empty, or if any error occurs during instantiation,
 * it defaults to returning a {@link MirrorEdgeHandler}.
 * </p>
 */
public class EdgeFactory {

  private static final Logger LOGGER = Logger.getLogger(EdgeFactory.class.getName());

  /**
   * Creates and returns an instance of {@link EdgeHandler} based on the given edge type.
   * <p>
   * The method attempts to dynamically load a class named
   * "cellsociety.model.grid.handler.&lt;edgeType&gt;EdgeHandler" using reflection.
   * If the edge type is null, empty, or if an error occurs during instantiation, a {@link MirrorEdgeHandler} is returned.
   * </p>
   *
   * @param edgeType the type of edge handler to create (e.g., "Infinite", "Mirror")
   * @return an instance of the requested {@code EdgeHandler}, or a default {@code MirrorEdgeHandler} if creation fails
   */
  public static EdgeHandler createEdgeHandler(String edgeType) {
    if (edgeType == null || edgeType.trim().isEmpty()) {
      LOGGER.warning("Edge type is null or empty. Using default MirrorEdgeHandler.");
      return new MirrorEdgeHandler();
    }

    try {
      String className = "cellsociety.model.grid.handler." + edgeType + "EdgeHandler";
      LOGGER.info("Attempting to load EdgeHandler class: " + className);
      return (EdgeHandler) Class.forName(className)
          .getDeclaredConstructor()
          .newInstance();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error loading EdgeHandler for type: " + edgeType + ". Using default MirrorEdgeHandler.", e);
      return new MirrorEdgeHandler();
    }
  }
}
