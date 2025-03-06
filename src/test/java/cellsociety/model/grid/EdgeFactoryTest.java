package cellsociety.model.grid;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cellsociety.model.grid.handler.EdgeHandler;
import cellsociety.model.grid.handler.MirrorEdgeHandler;
import org.junit.jupiter.api.Test;

class EdgeFactoryTest {

  @Test
  void testCreateEdgeHandlerValid() {
    // Assuming "Mirror" is a valid type. If your implementation supports other types, add tests for them.
    EdgeHandler edgeHandler = EdgeFactory.createEdgeHandler("Mirror");
    assertNotNull(edgeHandler, "The EdgeHandler instance should not be null.");
    // The factory may return a MirrorEdgeHandler directly if it's the only supported type.
    assertTrue(edgeHandler instanceof MirrorEdgeHandler,
        "Expected instance of MirrorEdgeHandler for valid input.");
  }

  @Test
  void testCreateEdgeHandlerInvalidFallsBack() {
    // For an invalid edge type, the factory should fall back to MirrorEdgeHandler.
    EdgeHandler edgeHandler = EdgeFactory.createEdgeHandler("InvalidType");
    assertNotNull(edgeHandler, "The fallback EdgeHandler instance should not be null.");
    assertTrue(edgeHandler instanceof MirrorEdgeHandler,
        "Expected fallback instance of MirrorEdgeHandler.");
  }
}
