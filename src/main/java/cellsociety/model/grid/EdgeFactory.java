package cellsociety.model.grid;

import cellsociety.model.grid.handler.EdgeHandler;
import cellsociety.model.grid.handler.MirrorEdgeHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdgeFactory {

  private static final Logger LOGGER = Logger.getLogger(EdgeFactory.class.getName());

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
