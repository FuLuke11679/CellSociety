package cellsociety.model.grid;

import cellsociety.model.grid.handler.EdgeHandler;
import cellsociety.model.grid.handler.MirrorEdgeHandler;

public class EdgeFactory {
  public static EdgeHandler createEdgeHandler(String edgeType) {
    try {
      String className = "cellsociety.model.grid.handler." + edgeType + "EdgeHandler";
      System.out.println("Attempting to load EdgeHandler class: " + className);
      return (EdgeHandler) Class.forName(className).getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      System.err.println("Error loading EdgeHandler: " + e.getMessage());
      e.printStackTrace();
      return new MirrorEdgeHandler(); // Fallback handler
    }
  }
}
